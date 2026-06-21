package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Login : Screen()
    object MeusTreinos : Screen()
    data class Arquiteto(val treinoId: Int) : Screen()
    data class SessaoAtiva(val treinoId: Int) : Screen()
    object Progressoes : Screen()
}

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository
    val databaseCreated = MutableStateFlow(false)

    // Routing Backstack
    var currentScreen by mutableStateOf<Screen>(Screen.Login)
        private set

    private val backStack = mutableListOf<Screen>()

    // Current mock user login state
    var isLoggedIn by mutableStateOf(false)
        private set

    var isAuthenticating by mutableStateOf(false)
        private set

    // Current Ficha selection
    var selectedFichaId by mutableStateOf("ficha_a")
        private set

    init {
        val database = WorkoutDatabase.getDatabase(application)
        repository = WorkoutRepository(database.workoutDao())
        
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            databaseCreated.value = true
        }
    }

    // Flows
    val fichasFlow: StateFlow<List<FichaEntity>> = repository.allFichas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val logsFlow: StateFlow<List<ProgressoLogEntity>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Treinos for the currently selected Ficha
    val currentTreinosFlow: StateFlow<List<TreinoEntity>> = snapshotFlow { selectedFichaId }
        .flatMapLatest { fichaId -> repository.getTreinosByFicha(fichaId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Backstack / Routing Actions
    fun navigateTo(screen: Screen) {
        backStack.add(currentScreen)
        currentScreen = screen
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeAt(backStack.size - 1)
        } else {
            currentScreen = if (isLoggedIn) Screen.MeusTreinos else Screen.Login
        }
    }

    // Auth
    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isAuthenticating = true
            kotlinx.coroutines.delay(1200) // Aesthetic delay as requested in prototype
            isLoggedIn = true
            isAuthenticating = false
            currentScreen = Screen.MeusTreinos
            onSuccess()
        }
    }

    fun logout() {
        isLoggedIn = false
        backStack.clear()
        currentScreen = Screen.Login
    }

    // Choose Ficha
    fun selectFicha(fichaId: String) {
        selectedFichaId = fichaId
    }

    // Fetch exercises lists
    fun getExerciciosForTreino(treinoId: Int): Flow<List<ExercicioEntity>> {
        return repository.getExerciciosByTreino(treinoId)
    }

    // Modificar / Editar workout names
    fun updateTreino(treino: TreinoEntity) {
        viewModelScope.launch {
            repository.updateTreino(treino)
        }
    }

    fun updateTreinoName(treinoId: Int, newName: String) {
        viewModelScope.launch {
            val treinos = currentTreinosFlow.value
            val target = treinos.find { it.id == treinoId }
            if (target != null) {
                repository.updateTreino(target.copy(name = newName))
            }
        }
    }

    // Modificar / Editar sequence order of workouts
    fun moveTreinoUp(treinoId: Int) {
        viewModelScope.launch {
            val treinos = currentTreinosFlow.value.toMutableList()
            val index = treinos.indexOfFirst { it.id == treinoId }
            if (index > 0) {
                val current = treinos[index]
                val previous = treinos[index - 1]
                
                // Swap sequence orders
                repository.updateTreino(current.copy(sequenceOrder = previous.sequenceOrder))
                repository.updateTreino(previous.copy(sequenceOrder = current.sequenceOrder))
            }
        }
    }

    fun moveTreinoDown(treinoId: Int) {
        viewModelScope.launch {
            val treinos = currentTreinosFlow.value
            val index = treinos.indexOfFirst { it.id == treinoId }
            if (index >= 0 && index < treinos.size - 1) {
                val current = treinos[index]
                val next = treinos[index + 1]
                
                // Swap sequence orders
                repository.updateTreino(current.copy(sequenceOrder = next.sequenceOrder))
                repository.updateTreino(next.copy(sequenceOrder = current.sequenceOrder))
            }
        }
    }

    // Reorder exercises inside Arquiteto Screen
    fun updateExercicio(exercicio: ExercicioEntity) {
        viewModelScope.launch {
            repository.updateExercicio(exercicio)
        }
    }

    fun moveExercicioUp(exercicio: ExercicioEntity, onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            val list = repository.getExerciciosByTreinoList(exercicio.treinoId).toMutableList()
            val index = list.indexOfFirst { it.id == exercicio.id }
            if (index > 0) {
                val current = list[index]
                val tempPrev = list[index - 1]
                
                repository.updateExercicio(current.copy(sequenceOrder = tempPrev.sequenceOrder))
                repository.updateExercicio(tempPrev.copy(sequenceOrder = current.sequenceOrder))
                onCompleted()
            }
        }
    }

    fun moveExercicioDown(exercicio: ExercicioEntity, onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            val list = repository.getExerciciosByTreinoList(exercicio.treinoId).toMutableList()
            val index = list.indexOfFirst { it.id == exercicio.id }
            if (index >= 0 && index < list.size - 1) {
                val current = list[index]
                val tempNext = list[index + 1]
                
                repository.updateExercicio(current.copy(sequenceOrder = tempNext.sequenceOrder))
                repository.updateExercicio(tempNext.copy(sequenceOrder = current.sequenceOrder))
                onCompleted()
            }
        }
    }

    // Add exercise inside Arquiteto
    fun addExercicioToTreino(treinoId: Int, name: String, series: Int, reps: Int, weight: Float) {
        viewModelScope.launch {
            val list = repository.getExerciciosByTreinoList(treinoId)
            val newOrder = if (list.isEmpty()) 0 else list.maxOf { it.sequenceOrder } + 1
            val newEx = ExercicioEntity(
                treinoId = treinoId,
                name = name,
                series = series,
                reps = reps,
                weight = weight,
                sequenceOrder = newOrder
            )
            repository.insertExercicio(newEx)
        }
    }

    // Delete exercise inside Arquiteto
    fun deleteExercicio(exercicio: ExercicioEntity) {
        viewModelScope.launch {
            repository.deleteExercicio(exercicio)
        }
    }

    // Add new custom workout to CURRENT Ficha
    fun createNewTreino(dayName: String, workoutName: String) {
        viewModelScope.launch {
            val list = currentTreinosFlow.value
            val nextSequence = if (list.isEmpty()) 0 else list.maxOf { it.sequenceOrder } + 1
            val newTreino = TreinoEntity(
                fichaId = selectedFichaId,
                dayOfWeek = dayName.uppercase(),
                name = workoutName.uppercase(),
                sequenceOrder = nextSequence,
                isRestDay = false
            )
            val treinoIdCreated = repository.insertTreino(newTreino)
            // Insere um exercicio default para começar
            repository.insertExercicio(ExercicioEntity(
                treinoId = treinoIdCreated,
                name = "EXECUTAR ACELERATION",
                series = 3,
                reps = 10,
                weight = 10f,
                sequenceOrder = 0
            ))
        }
    }

    // Delete entire training block
    fun deleteTreino(treino: TreinoEntity) {
        viewModelScope.launch {
            repository.deleteTreino(treino)
        }
    }

    // Complete / save session logs
    fun saveSessionProgress(treinoId: Int, exerciseName: String, currentWeight: Float, reps: Int) {
        viewModelScope.launch {
            // Seed a progress entry
            val log = ProgressoLogEntity(
                exerciseName = exerciseName.uppercase(),
                dateString = "HOJE",
                previousWeight = currentWeight - 5f,
                currentWeight = currentWeight,
                reps = reps,
                status = "INCREASED"
            )
            repository.insertProgressLog(log)
            
            // Mark workout progress
            val list = currentTreinosFlow.value
            val target = list.find { it.id == treinoId }
            if (target != null) {
                repository.updateTreino(target.copy(progressPercent = 100))
            }
        }
    }
}
