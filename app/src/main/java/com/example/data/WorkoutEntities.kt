package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas")
data class FichaEntity(
    @PrimaryKey val id: String,
    val name: String
)

@Entity(tableName = "treinos")
data class TreinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fichaId: String,
    val dayOfWeek: String, // e.g. "Segunda", "Terça"
    val name: String,
    val durationMinutes: Int = 45,
    val kcal: Int = 300,
    val isRestDay: Boolean = false,
    val progressPercent: Int = 0,
    val sequenceOrder: Int = 0
)

@Entity(tableName = "exercicios")
data class ExercicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val treinoId: Int,
    val name: String,
    val series: Int,
    val reps: Int,
    val weight: Float,
    val sequenceOrder: Int
)

@Entity(tableName = "logs_progresso")
data class ProgressoLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseName: String,
    val dateString: String, // e.g. "24/Okt"
    val previousWeight: Float,
    val currentWeight: Float,
    val reps: Int,
    val status: String // "INCREASED", "STAGNANT", "DECREASED"
)
