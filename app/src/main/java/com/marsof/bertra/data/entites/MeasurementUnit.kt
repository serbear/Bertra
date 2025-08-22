package com.marsof.bertra.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurement_units")
data class MeasurementUnit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)
