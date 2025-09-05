package com.marsof.bertra.data.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marsof.bertra.WorkoutSetType
import java.util.Date

@Entity(tableName = "train_exercise_repetitions")
data class TrainExerciseRepetitions(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /**
     * The id of the exercise in the training whom belongs current set.
     */
    @ColumnInfo(name = "train_exercise_id")
    val trainExerciseId: Long,
    /**
     * The sequence number or the set.
     */
    @ColumnInfo(name = "set_number")
    val setNumber: Int,
    /**
     * The weight or the number of weight using during the set.
     */
    @ColumnInfo(name = "weight_or_number")
    val weightOrNumber: Int,
    /**
     * The number of repetitions.
     */
    @ColumnInfo(name = "repetitions_number")
    val repetitionsNumber: Int,
    /**
     * The date when the set was done.
     */
    @ColumnInfo(index = true) val date: Date? = null,
    /**
     * The type of set: warm-up or working.
     */
    @ColumnInfo(name = "set_category")
    val setCategory: WorkoutSetType
)
