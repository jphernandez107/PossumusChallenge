package com.jphernandez.possumuschallenge.triviaSearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jphernandez.possumuschallenge.R
import com.jphernandez.possumuschallenge.application.PossumusChallengeApplication
import com.jphernandez.possumuschallenge.data.TriviaCategory
import com.jphernandez.possumuschallenge.questions.QuestionFragment
import com.jphernandez.possumuschallenge.repositories.TriviaRepository
import javax.inject.Inject

class TriviaSearchFragment: Fragment(), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var triviaRepository: TriviaRepository

    private val viewModel: TriviaSearchVM by lazy { getViewModel {
        TriviaSearchVM(
            triviaRepository
        )
    } }

    private var triviaCategoryList: List<TriviaCategory> = emptyList()
    private var selectedCategoryId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        PossumusChallengeApplication.appComponent.inject(this)
        return inflater.inflate(R.layout.trivia_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner?>(R.id.category_select)
        spinner?.onItemSelectedListener = this
        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer { list ->
            if(list.isNullOrEmpty()) {
                Log.e("TriviaSearchFragment", "Error----")
            } else {
                triviaCategoryList = list
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(),
                    R.layout.support_simple_spinner_dropdown_item, getCategoriesName(list))
                spinner?.let {
                    it.adapter = arrayAdapter
                }
            }
            setLoading(false)
        })

        viewModel.requestCategories()
        setLoading(true)

        val searchButton = view.findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val args = QuestionFragment.getBundle(selectedCategoryId)
            val newFragment =
                QuestionFragment()
            newFragment.arguments = args

            val transaction = activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container_view, newFragment)
                addToBackStack(null)
            }
            transaction?.commit()
        }
    }

    private fun getCategoriesName(list: List<TriviaCategory>): ArrayList<String> {
        val array: ArrayList<String> = ArrayList()
        array.add("Any")
        list.forEach { triviaCategory ->
            triviaCategory.name?.let {
                array.add(it)
            }
        }
        return array
    }

    private fun setLoading(loading: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.loading)?.visibility = if(loading) View.VISIBLE else View.GONE
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategoryId = getCategoryIdByName(parent?.getItemAtPosition(position).toString())
    }

    private fun getCategoryIdByName(name: String): Int {
        triviaCategoryList.forEach {
            if (it.name == name && it.id != null) return it.id.toInt()
        }
        return -1
    }
}