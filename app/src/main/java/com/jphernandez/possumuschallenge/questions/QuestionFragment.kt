package com.jphernandez.possumuschallenge.questions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jphernandez.possumuschallenge.R
import com.jphernandez.possumuschallenge.application.PossumusChallengeApplication
import com.jphernandez.possumuschallenge.data.Question
import com.jphernandez.possumuschallenge.repositories.TriviaRepository
import com.jphernandez.possumuschallenge.triviaSearch.getViewModel
import javax.inject.Inject

class QuestionFragment: Fragment() {

    @Inject
    lateinit var triviaRepository: TriviaRepository

    private val viewModel: QuestionVM by lazy { getViewModel {
        QuestionVM(
            triviaRepository
        )
    } }

    private val maxAnswers = 15

    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: AnswersAdapter

    private lateinit var questionTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var currentQuestion: Question

    private lateinit var nextButton: Button
    private lateinit var quitButton: Button
    private lateinit var correctQuestionCounter: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        PossumusChallengeApplication.appComponent.inject(this)
        val view = inflater.inflate(R.layout.question_fragment, container, false)
        if (viewModel.questionList.value.isNullOrEmpty()) {
            val categoryId = arguments?.getInt(CATEGORY_ID) ?: -1
            if (categoryId != -1) viewModel.requestQuestionList(amount = maxAnswers, categoryId = categoryId)
            else viewModel.requestAnyCategoryQuestionList(amount = maxAnswers)
        } else {
            viewModel.requestNextQuestion()
        }
        setLoading(true)
        val layoutManager = GridLayoutManager(activity, 1)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = layoutManager

        adapter =
            AnswersAdapter { isCorrectAnswer ->
                onAnswerClick(isCorrectAnswer)
            }
        recyclerView?.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.questionList.observe(viewLifecycleOwner, Observer {
            viewModel.requestNextQuestion()
        })

        viewModel.nextQuestion.observe(viewLifecycleOwner, Observer {
            it?.let { question ->
                currentQuestion = question
                loadView(question, view)
            }
        })

        viewModel.questionsAnsweredCorrectly.observe(viewLifecycleOwner, Observer {
            updateCorrectQuestionsCounter(it)
        })

        quitButton = view.findViewById(R.id.quit_button)
        quitButton.setOnClickListener {
            activity?.onBackPressed()
        }

        nextButton = view.findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            viewModel.requestNextQuestion()
            adapter.isAnswered = false
            setLoading(true)
        }

        correctQuestionCounter = view.findViewById(R.id.correct_question_counter)
    }

    private fun loadView(question: Question, view: View) {
        questionTextView = view.findViewById(R.id.question_text_view)
        categoryTextView = view.findViewById(R.id.category_text_view)

        questionTextView.text = question.question ?: ""
        categoryTextView.text = question.category ?: ""

        val answerList: MutableList<Pair<Boolean,String>> = mutableListOf()
        question.incorrectAnswers?.let {
            it.forEach { incorrect ->
                answerList.add(Pair(false, incorrect))
            }
        }
        question.correctAnswer?.let { answerList.add(Pair(true, it)) }
        answerList.shuffle()
        adapter.submitList(answerList)

        nextButton.isEnabled = false
        if (viewModel.hasNextQuestion()) {
            quitButton.text = getString(R.string.quit)
            nextButton.visibility = View.VISIBLE
        } else {
            quitButton.text = getString(R.string.finish)
            nextButton.visibility = View.GONE
        }
        setLoading(false)
    }

    private fun setLoading(loading: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.loading)?.visibility = if(loading) View.VISIBLE else View.GONE
    }

    private fun onAnswerClick(isCorrectAnswer: Boolean) {
        nextButton.isEnabled = true
        adapter.isAnswered = true
        if (isCorrectAnswer) {
            Log.d("QuestionFragment", "Correct answer!")
            viewModel.addQuestionAnsweredCorrectly()
        } else {
            Log.d("QuestionFragment", "Incorrect answer!")
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateCorrectQuestionsCounter(counter: Int) {
        val text = "$counter/$maxAnswers"
        correctQuestionCounter.text = text
    }

    companion object {
        private const val CATEGORY_ID = "CATEGORY_ID"

        fun getBundle(categoryId: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt(CATEGORY_ID, categoryId)
            return bundle
        }
    }
}