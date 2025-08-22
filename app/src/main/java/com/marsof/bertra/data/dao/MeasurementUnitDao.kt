package com.marsof.bertra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marsof.bertra.data.entites.MeasurementUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementUnitDao {
    @Query("select * from measurement_units")
    fun getAllMeasurementUnits(): Flow<List<MeasurementUnit>>

    @Query("select * from measurement_units where id = :id")
    fun getMeasurementUnit(id: Int): Flow<MeasurementUnit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: MeasurementUnit)

    @Update
    suspend fun update(item: MeasurementUnit)

    @Delete
    suspend fun delete(item: MeasurementUnit)
}