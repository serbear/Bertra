package com.marsof.bertra.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trains")
data class Train(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val circles: Int = 1,
    val description: String,
    val lastDate: Long?
)
