package com.marsof.bertra.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a table contains names of measurement units which are used in exercises.
 * It could be "kg", "number of weights", "lbs", etc.
 */
@Entity(tableName = "measurement_units")
data class MeasurementUnit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)
