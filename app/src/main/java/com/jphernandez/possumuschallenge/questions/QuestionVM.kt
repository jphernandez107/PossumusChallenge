package com.jphernandez.possumuschallenge.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jphernandez.possumuschallenge.data.Question
import com.jphernandez.possumuschallenge.repositories.TriviaRepository

class QuestionVM(private val triviaRepository: TriviaRepository): ViewModel() {

    val questionList: MutableLiveData<List<Question>> = MutableLiveData()
    val nextQuestion: MutableLiveData<Question?> = MutableLiveData()
    private val nextQuestionCounter: MutableLiveData<Int> = MutableLiveData()
    val questionsAnsweredCorrectly: MutableLiveData<Int> = MutableLiveData()

    fun requestQuestionList(amount: Int = 15, categoryId: Int, type: String = "") {
        nextQuestionCounter.postValue(0)
        questionsAnsweredCorrectly.postValue(0)
        triviaRepository.getQuestions(amount, categoryId, type).subscribe {
            if (it is List<Question>) questionList.postValue(it)
        }
    }

    fun requestAnyCategoryQuestionList(amount: Int = 15, type: String = "") {
        nextQuestionCounter.postValue(0)
        questionsAnsweredCorrectly.postValue(0)
        triviaRepository.getAnyCategoryQuestions(amount, type).subscribe {
            if (it is List<Question>) questionList.postValue(it)
        }
    }

    fun requestNextQuestion() {
        nextQuestionCounter.value?.let { counter ->
            questionList.value?.let { list ->
                if (counter < list.size) nextQuestion.postValue(list[counter]) else nextQuestion.postValue(null)
                nextQuestionCounter.postValue(counter + 1)
            }
        }
    }

    fun hasNextQuestion(): Boolean {
        nextQuestionCounter.value?.let { counter ->
            questionList.value?.let { list ->
                return counter < list.size - 1
            }
        }
        return false
    }

    fun addQuestionAnsweredCorrectly() {
        questionsAnsweredCorrectly.value?.let {
            questionsAnsweredCorrectly.postValue(it + 1)
        }
    }
}