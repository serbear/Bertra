package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("select * from exercises")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("select * from exercises where id = :id")
    fun getExercise(id: Int): Flow<Exercise>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Exercise)

    @Update
    suspend fun update(item: Exercise)

    @Delete
    suspend fun delete(item: Exercise)
}