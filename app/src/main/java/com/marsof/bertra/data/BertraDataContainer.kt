package com.marsof.bertra.data

import android.content.Context
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.dao.TrainDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.dao.TrainExerciseRepetitionsDao

class BertraDataContainer(private val context: Context) {
    val trainDao: TrainDao by lazy {
        BertraDatabase.getDatabase(context).trainDao()
    }
    val exerciseDao: ExerciseDao by lazy {
        BertraDatabase.getDatabase(context).exerciseDao()
    }
    val measurementUnitDao: MeasurementUnitDao by lazy {
        BertraDatabase.getDatabase(context).measurementUnitDao()
    }
    val trainExerciseDao: TrainExerciseDao by lazy {
        BertraDatabase.getDatabase(context).trainExerciseDao()
    }
    val trainExerciseRepetitionsDao: TrainExerciseRepetitionsDao by lazy {
        BertraDatabase.getDatabase(context).trainExerciseRepetitionsDao()
    }
}