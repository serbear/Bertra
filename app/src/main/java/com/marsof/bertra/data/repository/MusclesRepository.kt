package com.marsof.bertra.data.repository

import android.util.Log
import com.marsof.bertra.api.Exercise
import com.marsof.bertra.api.ExercisesApi
import com.marsof.bertra.data.Muscle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

/**
 * Реализация репозитория для получения данных о мышцах из сети.
 */
class MusclesRepository(private val apiKey: String) : IMusclesRepository {

    /**
     * Получает список мышц из API и отдает его в виде потока.
     * Этот метод выполняет сетевой запрос каждый раз при сборе потока.
     */
    override fun getMusclesStream(): Flow<List<Muscle>> = flow {
        if (apiKey.isEmpty()) {
            Log.e("MusclesRepository", "API key is missing.")
            throw IllegalArgumentException("API key is missing.")
        }
        try {
            val exercises: List<Exercise> = ExercisesApi.retrofitService.getExercises(apiKey)
            // Извлекаем уникальные названия мышц и преобразуем их в объекты Muscle
            val muscles = exercises.map { it.muscle }
                .distinct()
                .map { muscleName -> Muscle(name=muscleName) }
            emit(muscles) // Отправляем список объектов Muscle
        } catch (e: Exception) {
            Log.e("MusclesRepository", "Error fetching muscles: ${e.message}")
            // Преобразуем исключение в более специфичное для слоя данных, если нужно
            throw IOException("Failed to fetch data from network", e)
        }
    }.flowOn(Dispatchers.IO) // Убеждаемся, что сетевой запрос выполняется в фоновом потоке.

    /**
     * В текущей простой реализации этот метод не делает ничего,
     * так как getMusclesStream() всегда загружает свежие данные.
     * В более сложной реализации (с базой данных) здесь была бы логика
     * для принудительной загрузки данных из сети и сохранения их в БД.
     */
    override suspend fun refreshMuscles() {
        // Логика для принудительного обновления (если потребуется кеширование)
    }
}