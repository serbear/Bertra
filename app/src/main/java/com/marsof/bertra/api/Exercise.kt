package com.marsof.bertra.api

// Класс данных, представляющий одно упражнение из ответа API.
data class Exercise(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,val difficulty: String,
    val instructions: String
)