package com.marsof.bertra.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a table contains names of trains and their description.
 */
@Entity(tableName = "trains")
data class Train(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /**
     * Name of the train.
     */
    val name: String,
    /**
     * Number of repetitions of the train.
     */
    val circles: Int = 1,
    /**
     * Extra description of the train.
     */
    val description: String,
    /**
     * Date of the last train.
     */
    val lastDate: Long?
)
