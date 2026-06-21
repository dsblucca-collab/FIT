package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    val allFichas: Flow<List<FichaEntity>> = workoutDao.getAllFichas()
    val allLogs: Flow<List<ProgressoLogEntity>> = workoutDao.getAllProgressLogs()

    fun getTreinosByFicha(fichaId: String): Flow<List<TreinoEntity>> =
        workoutDao.getTreinosByFicha(fichaId)

    fun getExerciciosByTreino(treinoId: Int): Flow<List<ExercicioEntity>> =
        workoutDao.getExerciciosByTreino(treinoId)

    suspend fun getExerciciosByTreinoList(treinoId: Int): List<ExercicioEntity> =
        workoutDao.getExerciciosByTreinoList(treinoId)

    suspend fun updateTreino(treino: TreinoEntity) = workoutDao.updateTreino(treino)
    suspend fun deleteTreino(treino: TreinoEntity) = workoutDao.deleteTreino(treino)
    suspend fun insertTreino(treino: TreinoEntity): Int = workoutDao.insertTreino(treino).toInt()

    suspend fun updateExercicio(exercicio: ExercicioEntity) = workoutDao.updateExercicio(exercicio)
    suspend fun deleteExercicio(exercicio: ExercicioEntity) = workoutDao.deleteExercicio(exercicio)
    suspend fun insertExercicio(exercicio: ExercicioEntity): Int = workoutDao.insertExercicio(exercicio).toInt()
    suspend fun insertProgressLog(log: ProgressoLogEntity) = workoutDao.insertProgressLog(log)

    suspend fun seedInitialDataIfEmpty() {
        val count = workoutDao.getTreinosCount()
        if (count > 0) return

        // 1. Seed Fichas
        val fichas = listOf(
            FichaEntity("ficha_a", "FICHA A"),
            FichaEntity("ficha_b", "FICHA B"),
            FichaEntity("ficha_c", "FICHA C")
        )
        workoutDao.insertFichas(fichas)

        // 2. Seed Treinos for FICHA A
        val treinosA = listOf(
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "SEGUNDA", name = "DIA DE PERNAS", durationMinutes = 65, kcal = 450, sequenceOrder = 0, progressPercent = 30),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "TERÇA", name = "SUPERIOR", durationMinutes = 65, kcal = 420, sequenceOrder = 1, progressPercent = 0),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "QUARTA", name = "DIA DE DESCANSO", durationMinutes = 0, kcal = 0, isRestDay = true, sequenceOrder = 2),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "QUINTA", name = "CARDIO / CORE", durationMinutes = 45, kcal = 600, sequenceOrder = 3, progressPercent = 50),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "SEXTA", name = "COSTAS & BÍCEPS", durationMinutes = 60, kcal = 400, sequenceOrder = 4, progressPercent = 68),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "SÁBADO", name = "FULL BODY BLAST", durationMinutes = 50, kcal = 550, sequenceOrder = 5, progressPercent = 0),
            TreinoEntity(fichaId = "ficha_a", dayOfWeek = "DOMINGO", name = "MOBILITY FLOW", durationMinutes = 30, kcal = 150, sequenceOrder = 6, progressPercent = 10)
        )
        workoutDao.insertTreinos(treinosA)

        // Give Treinos IDs 1 to 7 corresponding to treinosA order
        // 3. Seed Exercicios for DIA DE PERNAS (id = 1)
        workoutDao.insertExercicios(listOf(
            ExercicioEntity(treinoId = 1, name = "AGACHAMENTO", series = 4, reps = 12, weight = 60f, sequenceOrder = 0),
            ExercicioEntity(treinoId = 1, name = "AFUNDO", series = 4, reps = 10, weight = 40f, sequenceOrder = 1),
            ExercicioEntity(treinoId = 1, name = "LEG PRESS", series = 3, reps = 15, weight = 120f, sequenceOrder = 2)
        ))

        // 3. Seed Exercicios for SUPERIOR (id = 2)
        workoutDao.insertExercicios(listOf(
            ExercicioEntity(treinoId = 2, name = "SUPINO", series = 4, reps = 12, weight = 80f, sequenceOrder = 0),
            ExercicioEntity(treinoId = 2, name = "REMADA", series = 4, reps = 12, weight = 60f, sequenceOrder = 1)
        ))

        // 3. Seed Exercicios for QUINTA CARDIO (id = 4)
        workoutDao.insertExercicios(listOf(
            ExercicioEntity(treinoId = 4, name = "CYBER_SUPINO RETO", series = 4, reps = 12, weight = 85f, sequenceOrder = 0),
            ExercicioEntity(treinoId = 4, name = "PULL-UPS", series = 3, reps = 10, weight = 25f, sequenceOrder = 1)
        ))

        // 3. Seed Exercicios for SEXTA (id = 5)
        workoutDao.insertExercicios(listOf(
            ExercicioEntity(treinoId = 5, name = "AGACHAMENTO_NEON", series = 4, reps = 12, weight = 90f, sequenceOrder = 0),
            ExercicioEntity(treinoId = 5, name = "SUPINO_CYBER", series = 3, reps = 15, weight = 70f, sequenceOrder = 1)
        ))

        // 2. Seed Treinos for FICHA B (More options matching screenshots)
        val treinosB = listOf(
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "SEGUNDA", name = "TREINO DE PERNAS", durationMinutes = 60, kcal = 400, sequenceOrder = 0, progressPercent = 0),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "TERÇA", name = "SUPERIOR A", durationMinutes = 65, kcal = 410, sequenceOrder = 1, progressPercent = 100),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "QUARTA", name = "DESCANSO", durationMinutes = 0, kcal = 0, isRestDay = true, sequenceOrder = 2),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "QUINTA", name = "CARDIO HIIT", durationMinutes = 45, kcal = 600, sequenceOrder = 3, progressPercent = 90),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "SEXTA", name = "SUPERIOR B", durationMinutes = 60, kcal = 380, sequenceOrder = 4, progressPercent = 68),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "SÁBADO", name = "HIIT PESADO", durationMinutes = 55, kcal = 570, sequenceOrder = 5, progressPercent = 0),
            TreinoEntity(fichaId = "ficha_b", dayOfWeek = "DOMINGO", name = "RELAXAMENTO", durationMinutes = 40, kcal = 120, sequenceOrder = 6, progressPercent = 0)
        )
        workoutDao.insertTreinos(treinosB)

        // Let's seed initial progress logs matching screenshot
        workoutDao.insertProgressLog(ProgressoLogEntity(exerciseName = "BENCH PRESS", dateString = "12 OCT · 8 reps", previousWeight = 85f, currentWeight = 90f, reps = 8, status = "INCREASED"))
        workoutDao.insertProgressLog(ProgressoLogEntity(exerciseName = "DEADLIFT", dateString = "10 OCT · 3 reps", previousWeight = 180f, currentWeight = 180f, reps = 3, status = "STAGNANT"))
        workoutDao.insertProgressLog(ProgressoLogEntity(exerciseName = "OHP", dateString = "08 OCT · 5 reps", previousWeight = 55f, currentWeight = 57.5f, reps = 5, status = "INCREASED"))
    }
}
