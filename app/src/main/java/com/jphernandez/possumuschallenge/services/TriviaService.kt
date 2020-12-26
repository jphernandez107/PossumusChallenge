package com.jphernandez.possumuschallenge.services

import com.jphernandez.possumuschallenge.dto.CategoriesResultDto
import com.jphernandez.possumuschallenge.dto.QuestionsResultDto
import io.reactivex.Observable

interface TriviaService {

    fun getCategories(): Observable<CategoriesResultDto>

    fun getQuestions(amount: Int, categoryId: Int, type: String): Observable<QuestionsResultDto>

    fun getAnyCategoryQuestions(amount: Int, type: String): Observable<QuestionsResultDto>

}