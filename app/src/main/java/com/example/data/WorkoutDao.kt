package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Fichas
    @Query("SELECT * FROM fichas")
    fun getAllFichas(): Flow<List<FichaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFichas(fichas: List<FichaEntity>)

    // Treinos
    @Query("SELECT * FROM treinos WHERE fichaId = :fichaId ORDER BY sequenceOrder ASC")
    fun getTreinosByFicha(fichaId: String): Flow<List<TreinoEntity>>

    @Query("SELECT * FROM treinos WHERE id = :treinoId LIMIT 1")
    suspend fun getTreinoById(treinoId: Int): TreinoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreino(treino: TreinoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreinos(treinos: List<TreinoEntity>)

    @Update
    suspend fun updateTreino(treino: TreinoEntity)

    @Delete
    suspend fun deleteTreino(treino: TreinoEntity)

    // Exercicios
    @Query("SELECT * FROM exercicios WHERE treinoId = :treinoId ORDER BY sequenceOrder ASC")
    fun getExerciciosByTreino(treinoId: Int): Flow<List<ExercicioEntity>>

    @Query("SELECT * FROM exercicios WHERE treinoId = :treinoId ORDER BY sequenceOrder ASC")
    suspend fun getExerciciosByTreinoList(treinoId: Int): List<ExercicioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercicio(exercicio: ExercicioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercicios(exercicios: List<ExercicioEntity>)

    @Update
    suspend fun updateExercicio(exercicio: ExercicioEntity)

    @Delete
    suspend fun deleteExercicio(exercicio: ExercicioEntity)

    @Query("DELETE FROM exercicios WHERE treinoId = :treinoId")
    suspend fun deleteExerciciosByTreino(treinoId: Int)

    // Progress Logs
    @Query("SELECT * FROM logs_progresso ORDER BY id DESC")
    fun getAllProgressLogs(): Flow<List<ProgressoLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressLog(log: ProgressoLogEntity)

    @Query("SELECT COUNT(*) FROM treinos")
    suspend fun getTreinosCount(): Int
}
