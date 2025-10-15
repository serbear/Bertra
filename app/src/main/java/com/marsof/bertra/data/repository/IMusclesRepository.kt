package com.marsof.bertra.data.repository

import com.marsof.bertra.data.Muscle
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для репозитория, отвечающего за работу с данными о мышцах.
 */
interface IMusclesRepository {
    /**
     * Возвращает поток (Flow) со списком названий мышц.
     * Flow позволяет автоматически получать обновления, когда данные изменяются.
     */
    fun getMusclesStream(): Flow<List<Muscle>>

    /**
     * Принудительно обновляет кеш мышц с удаленного сервера.
     */
    suspend fun refreshMuscles()

}