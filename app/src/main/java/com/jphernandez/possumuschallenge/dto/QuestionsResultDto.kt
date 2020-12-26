package com.jphernandez.possumuschallenge.dto

data class QuestionsResultDto(
    val response_code: Int?,
    val results: List<QuestionDto>
)