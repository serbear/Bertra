package com.marsof.bertra.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a table contains exercises that are included in a particular train.
 */
@Entity(tableName = "train_exercises")
data class TrainExercise (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /**
     * Id of the train to which the exercise belongs.
     */
    val trainId: Int,
    /**
     * Id of the exercise.
     * From the table "exercises".
     */
    val exerciseId: Int,
    /**
     * Number of repetitions of the exercise.
     */
    val repetitionsNumber: Int,
    /**
     * Id of the measurement unit.
     * From the table "measurement_units".
     */
    val measurementUnitId: Int,
)