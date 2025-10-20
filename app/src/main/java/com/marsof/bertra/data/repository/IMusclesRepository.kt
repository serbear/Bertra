package com.marsof.bertra.data.repository

import com.marsof.bertra.api.Exercise
import com.marsof.bertra.data.Muscle
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для репозитория, отвечающего за работу с данными о мышцах.
 */
interface IMusclesRepository {
    /**
     * Возвращает поток (Flow) со списком названий мышц.
     */
    fun getMusclesStream(): Flow<List<Muscle>>

    /**
     * Принудительно обновляет кеш мышц с удаленного сервера.
     */
    suspend fun refreshMuscles()
    fun getExerciseForMuscleStream(muscle: Muscle): Flow<List<Exercise>>

}