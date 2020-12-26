package com.jphernandez.possumuschallenge.repositories

import com.jphernandez.possumuschallenge.data.Question
import com.jphernandez.possumuschallenge.data.TriviaCategory
import com.jphernandez.possumuschallenge.dto.CategoriesResultDto
import com.jphernandez.possumuschallenge.dto.QuestionDto
import com.jphernandez.possumuschallenge.dto.QuestionsResultDto
import com.jphernandez.possumuschallenge.dto.TriviaCategoriesDto
import com.jphernandez.possumuschallenge.services.TriviaService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class TriviaRepositoryImpl(private val triviaService: TriviaService): TriviaRepository {

    override fun getCategories(): Observable<List<TriviaCategory>> =
        triviaService.getCategories()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .map {
                convertCategories(it)
            }

    override fun getQuestions(amount: Int, categoryId: Int, type: String): Observable<List<Question>> =
        triviaService.getQuestions(amount, categoryId, type)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .map {
                convertQuestions(it)
            }

    override fun getAnyCategoryQuestions(amount: Int, type: String): Observable<List<Question>> =
        triviaService.getAnyCategoryQuestions(amount, type)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .map {
                convertQuestions(it)
            }


    private fun convertCategories(categoriesResultDto: CategoriesResultDto) =
        categoriesResultDto.trivia_categories.map(::convertCategory)

    private fun convertCategory(categ: TriviaCategoriesDto) =
        TriviaCategory(
            categ.id,
            categ.name
        )

    private fun convertQuestions(questionsResultDto: QuestionsResultDto) =
        questionsResultDto.results.map(::convertQuestion)

    private fun convertQuestion(questionDto: QuestionDto) =
        Question(
            questionDto.category,
            questionDto.type,
            questionDto.difficulty,
            questionDto.question,
            questionDto.correct_answer,
            questionDto.incorrect_answers
        )

}