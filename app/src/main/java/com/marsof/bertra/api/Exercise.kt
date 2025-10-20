package com.marsof.bertra.api

data class Exercise(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
)