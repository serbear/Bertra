package com.marsof.bertra.data.entites

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a table contains exercises that are included in a particular train.
 */
@Entity(tableName = "train_exercises")
data class TrainExercise (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /**
     * Id of the train to which the exercise belongs.
     */
    var trainId: Long,
    /**
     * Id of the exercise.
     * From the table "exercises".
     */
    val exerciseId: Long,
    /**
     * Id of the measurement unit.
     * From the table "measurement_units".
     */
    // NOTE: Consider to create a separate measure unit for each exercise set in the future release.
    val measurementUnitId: Long,
    /**
     * Order of the exercise in the workout.
     */
    var exerciseOrder: Int,
)

data class TrainExerciseWithExerciseName(
    @Embedded val trainExercise: TrainExercise,
    @ColumnInfo(name = "exercise_name") val exerciseName: String
)