package com.marsof.bertra.data.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "train_exercise_repetitions")
data class TrainExerciseRepetitions(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainExerciseId: Long,
    val setNumber: Int,
    val weightOrNumber: Int,
    val repetitionsNumber: Int,
    @ColumnInfo(index = true) val date: Long?
)
