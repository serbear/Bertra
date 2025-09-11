package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.TrainExercise
import com.marsof.bertra.data.entites.TrainExerciseWithExerciseName
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainExerciseDao {
    @Query("select * from train_exercises")
    fun getAllTrainExercises(): Flow<List<TrainExercise>>

    @Query("select * from train_exercises where id = :id")
    fun getTrainExercise(id: Long): Flow<TrainExercise>

    @Query(
        "SELECT te.*, e.name AS exercise_name " +
                "FROM train_exercises te " +
                "INNER JOIN exercises e ON te.exerciseId = e.id " +
                "WHERE te.trainId = :trainId"
    )
    fun getTrainExercisesById(trainId: Long): Flow<List<TrainExerciseWithExerciseName>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrainExercise): Long

    @Update
    suspend fun update(item: TrainExercise)

    @Delete
    suspend fun delete(item: TrainExercise)
    @Query("select count(*) from train_exercises where id = :workoutId")
    fun getWorkoutExerciseCount(workoutId: Long): Flow<Int>
}