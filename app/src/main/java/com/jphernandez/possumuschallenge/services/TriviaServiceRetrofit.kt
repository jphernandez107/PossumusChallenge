package com.jphernandez.possumuschallenge.services

import com.jphernandez.possumuschallenge.dto.CategoriesResultDto
import com.jphernandez.possumuschallenge.dto.QuestionsResultDto
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaServiceRetrofit {

    @GET("/api_category.php")
    fun getCategories(): Observable<CategoriesResultDto>

    @GET("/api.php?")
    fun getQuestions(@Query("amount") amount: Int, @Query("category") categoryId: Int, @Query("type") type: String): Observable<QuestionsResultDto>

    @GET("/api.php?")
    fun getAnyCategoryQuestions(@Query("amount") amount: Int, @Query("type") type: String): Observable<QuestionsResultDto>

    companion object {
        fun create(retrofit: Retrofit): TriviaServiceRetrofit =
            retrofit.create(TriviaServiceRetrofit::class.java)
    }

}