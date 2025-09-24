package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.Train
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainDao {
    @Query("select * from trains")
    fun getAllTrains(): Flow<List<Train>>

    @Query("select * from trains where id = :id")
    fun getTrain(id: Long): Flow<Train?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Train): Long

    @Update
    suspend fun update(item: Train)

    @Delete
    suspend fun delete(item: Train)

    @Query("SELECT * FROM trains WHERE lastDate IS NOT NULL ORDER BY lastDate DESC LIMIT 1")
    fun getLastTrain(): Flow<Train?>

    @Query("UPDATE trains SET lastDate = :lastDate WHERE id = :id")
    suspend fun updateLastDate(id: Long, lastDate: Long)
}