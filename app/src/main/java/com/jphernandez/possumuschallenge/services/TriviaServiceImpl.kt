package com.jphernandez.possumuschallenge.services

import com.jphernandez.possumuschallenge.dto.CategoriesResultDto
import com.jphernandez.possumuschallenge.dto.QuestionsResultDto
import io.reactivex.Observable

class TriviaServiceImpl(private val retrofit: TriviaServiceRetrofit) : TriviaService {

    override fun getCategories(): Observable<CategoriesResultDto> = retrofit.getCategories()

    override fun getQuestions(amount: Int, categoryId: Int, type: String) = retrofit.getQuestions(amount, categoryId, type)

    override fun getAnyCategoryQuestions(amount: Int, type: String): Observable<QuestionsResultDto> = retrofit.getAnyCategoryQuestions(amount, type)

}