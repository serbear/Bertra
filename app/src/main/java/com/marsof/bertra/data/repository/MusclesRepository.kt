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
        val repName = "MusclesRepository"

        if (apiKey.isEmpty()) {
            val logMessage = "API key is missing."
            val errorMessage = "API key is missing."

            Log.e(repName, logMessage)
            throw IllegalArgumentException(errorMessage)
        }
        try {
            /* DEBUG: UNCOMMENT IN RELEASE

            val exercises: List<Exercise> = ExercisesApi.retrofitService.getExercises(apiKey)
            // Get unique muscle names and transform them into Muscle objects.
            val muscles = exercises.map { it.muscle }
                .distinct()
                .map { muscleName -> Muscle(name=muscleName) }
            */

            // TODO: DEBUG: emit pre-prepared list so as not to call API.
            val muscles = listOf(
                Muscle("abdominals"),
                Muscle("abductors"),
                Muscle("adductors"),
                Muscle("biceps"),
                Muscle("calves"),
                Muscle("chest"),
                Muscle("forearms"),
                Muscle("glutes"),
                Muscle("hamstrings"),
                Muscle("lats"),
                Muscle("lower_back"),
                Muscle("middle_back"),
                Muscle("neck"),
                Muscle("quadriceps"),
                Muscle("traps"),
                Muscle("triceps"),
            )
            // Send the list of muscles.
            emit(muscles)
        } catch (e: Exception) {
            val logMessage = "Error fetching muscles: ${e.message}"
            val errorMessage = "Failed to fetch data from network"

            Log.e(repName, logMessage)
            throw IOException(errorMessage, e)
        }
    }
        // Make sure that the network request is executed in a background thread.
        .flowOn(Dispatchers.IO)

    /**
     * В текущей простой реализации этот метод не делает ничего,
     * так как getMusclesStream() всегда загружает свежие данные.
     * В более сложной реализации (с базой данных) здесь была бы логика
     * для принудительной загрузки данных из сети и сохранения их в БД.
     */
    override suspend fun refreshMuscles() {
        // Логика для принудительного обновления (если потребуется кеширование)
    }

    override  fun getExerciseForMuscleStream(muscle: Muscle): Flow<List<Exercise>> = flow{
        val repName = "MusclesRepository"

        if (apiKey.isEmpty()) {
            val logMessage = "API key is missing."
            val errorMessage = "API key is missing."

            Log.e(repName, logMessage)
            throw IllegalArgumentException(errorMessage)
        }
        try {
            /* DEBUG: UNCOMMENT IN RELEASE */

            val exercises: List<Exercise> = ExercisesApi.retrofitService.getExercises(apiKey)
                .filter { it.muscle == muscle.name }

            // Send the list of muscles.
            emit(exercises)
        } catch (e: Exception) {
            val logMessage = "Error fetching muscles: ${e.message}"
            val errorMessage = "Failed to fetch data from network"

            Log.e(repName, logMessage)
            throw IOException(errorMessage, e)
        }
    }
}