package com.jphernandez.possumuschallenge.data

data class Question(
    val category: String?,
    val type: String?,
    val difficulty: String?,
    val question: String?,
    val correctAnswer: String?,
    val incorrectAnswers: List<String>?
)