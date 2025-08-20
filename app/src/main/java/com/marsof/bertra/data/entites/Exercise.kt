package com.marsof.bertra.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a table contains names of exercises.
 */
@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /**
     * Name of the exercise.
     */
    val name: String,
)
