package com.jphernandez.possumuschallenge.repositories

import com.jphernandez.possumuschallenge.data.Question
import com.jphernandez.possumuschallenge.data.TriviaCategory
import io.reactivex.Observable

interface TriviaRepository {

    fun getCategories(): Observable<List<TriviaCategory>>

    fun getQuestions(amount: Int, categoryId: Int, type: String): Observable<List<Question>>

    fun getAnyCategoryQuestions(amount: Int, type: String): Observable<List<Question>>
}