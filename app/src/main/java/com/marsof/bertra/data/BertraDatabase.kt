package com.marsof.bertra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marsof.bertra.data.dao.ExerciseDao
import com.marsof.bertra.data.dao.MeasurementUnitDao
import com.marsof.bertra.data.dao.TrainDao
import com.marsof.bertra.data.dao.TrainExerciseDao
import com.marsof.bertra.data.dao.TrainExerciseRepetitionsDao
import com.marsof.bertra.data.entites.Exercise
import com.marsof.bertra.data.entites.MeasurementUnit
import com.marsof.bertra.data.entites.Train
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseRepetitions

@Database(
    entities = [
        Train::class,
        Exercise::class,
        MeasurementUnit::class,
        TrainExercise::class,
        TrainExerciseRepetitions::class,
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class BertraDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun measurementUnitDao(): MeasurementUnitDao
    abstract fun trainExerciseDao(): TrainExerciseDao
    abstract fun trainExerciseRepetitionsDao(): TrainExerciseRepetitionsDao

    companion object {
        @Volatile
        private var Instance: BertraDatabase? = null

        fun getDatabase(context: Context): BertraDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = BertraDatabase::class.java,
                    name = "bertra_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}