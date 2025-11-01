package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.TrainExerciseRepetitions
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainExerciseRepetitionsDao {
    @Query("select * from train_exercise_repetitions")
    fun getAllTrainExerciseRepetitions(): Flow<List<TrainExerciseRepetitions>>

    @Query("select * from train_exercise_repetitions where train_exercise_id = :id")
    fun getTrainExerciseRepetitions(id: Long): Flow<List<TrainExerciseRepetitions>?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrainExerciseRepetitions): Long

    @Update
    suspend fun update(item: TrainExerciseRepetitions)

    @Delete
    suspend fun delete(item: TrainExerciseRepetitions)

    @Query("DELETE FROM train_exercise_repetitions")
    suspend fun deleteAll()

}