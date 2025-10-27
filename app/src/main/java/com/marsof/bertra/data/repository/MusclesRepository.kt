package com.marsof.bertra.data.repository

import android.util.Log
import androidx.room.ext.capitalize
import com.marsof.bertra.api.Exercise
import com.marsof.bertra.api.ExercisesApi
import com.marsof.bertra.data.Muscle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.util.Locale

/**
 * Repository for fetching muscle data from the network.
 */
class MusclesRepository(private val apiKey: String) : IMusclesRepository {

    private val repName = "MusclesRepository"

    init {
        handleApiKeyError()
    }

    private fun handleApiKeyError() {
        if (apiKey.isEmpty()) {
            val errorMessage = "API key is missing."
            Log.e(repName, errorMessage)
            throw IllegalArgumentException(errorMessage)
        }
    }

    /**
     * Получает список мышц из API и отдает его в виде потока.
     * Этот метод выполняет сетевой запрос каждый раз при сборе потока.
     */
    override fun getMusclesStream(): Flow<List<Muscle>> = flow {
        try {
            /* DEBUG: UNCOMMENT IN RELEASE

            val exercises: List<Exercise> = ExercisesApi.retrofitService.getExercises(apiKey)
            // Get unique muscle names and transform them into Muscle objects.
            val muscles = exercises.map { it.muscle }
                .distinct()
                .map { muscleName -> Muscle(name=muscleName.capitalize(Locale.ROOT)) }
            */

            // TODO: DEBUG: emit pre-prepared list so as not to call API.
            val muscles = listOf(
                Muscle("Abdominals"),
                Muscle("Abductors"),
                Muscle("Adductors"),
                Muscle("Biceps"),
                Muscle("Calves"),
                Muscle("Chest"),
                Muscle("Forearms"),
                Muscle("Glutes"),
                Muscle("Hamstrings"),
                Muscle("Lats"),
                Muscle("Lower_back"),
                Muscle("Middle_back"),
                Muscle("Neck"),
                Muscle("Quadriceps"),
                Muscle("Traps"),
                Muscle("Triceps"),
            )
            // Send the list of muscles.
            emit(muscles)
        } catch (e: Exception) {
            handleApiError(e)
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

    override fun getExerciseForMuscleStream(muscle: Muscle): Flow<List<Exercise>> = flow {

        try {
            /* DEBUG: UNCOMMENT IN RELEASE */

            val exercises: List<Exercise> = ExercisesApi.retrofitService.getExercises(
                apiKey = apiKey,
                muscle = muscle.name,
            )

            // Send the list of muscles.
            emit(exercises)
        } catch (e: Exception) {
            handleApiError(e)
        }
    }
        // Make sure that the network request is executed in a background thread.
        .flowOn(Dispatchers.IO)

    private fun handleApiError(e: Exception) {
        val logMessage = "Error during API call: ${e.message}"
        val errorMessage = "Failed to fetch data from network"

        Log.e(repName, logMessage)
        throw IOException(errorMessage, e)
    }
}