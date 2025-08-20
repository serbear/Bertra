package com.marsof.bertra.data

import android.content.Context
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.TrainDao

class BertraDataContainer(private val context: Context) {
    val trainDao: TrainDao by lazy {
        BertraDatabase.getDatabase(context).trainDao()
    }
    val exerciseDao: ExerciseDao by lazy {
        BertraDatabase.getDatabase(context).exerciseDao()
    }
}