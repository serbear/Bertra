package com.marsof.bertra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Train::class],
    version = 1,
    exportSchema = false
)
abstract class BertraDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao

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