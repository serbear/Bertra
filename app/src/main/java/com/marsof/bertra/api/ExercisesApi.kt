package com.marsof.bertra.api

//import androidx.privacysandbox.tools.core.generator.build
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = "https://api.api-ninjas.com/v1/"

// Создание объекта Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// Интерфейс, определяющий, как Retrofit взаимодействует с веб-сервисом
interface ExercisesApiService {
    @GET("exercises")
    suspend fun getExercises(
        @Header("X-Api-Key") apiKey: String,
        @Query("muscle") muscle: String? = null
    ): List<Exercise>
}

// Открытый объект для доступа к сервису Retrofit
object ExercisesApi {
    val retrofitService: ExercisesApiService by lazy {
        retrofit.create(ExercisesApiService::class.java)
    }
}