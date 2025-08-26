package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.TrainExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainExerciseDao {
    @Query("select * from train_exercises")
    fun getAllTrainExercises(): Flow<List<TrainExercise>>

    @Query("select * from train_exercises where id = :id")
    fun getTrainExercise(id: Long): Flow<TrainExercise>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrainExercise): Long

    @Update
    suspend fun update(item: TrainExercise)

    @Delete
    suspend fun delete(item: TrainExercise)
}