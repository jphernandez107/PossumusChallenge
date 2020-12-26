package com.jphernandez.possumuschallenge.triviaSearch

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jphernandez.possumuschallenge.data.TriviaCategory
import com.jphernandez.possumuschallenge.repositories.TriviaRepository

class TriviaSearchVM(private val triviaRepository: TriviaRepository): ViewModel(){

    val categoryLiveData: MutableLiveData<List<TriviaCategory>> = MutableLiveData()

    fun requestCategories() {
        triviaRepository.getCategories().subscribe {
            if (it is List<TriviaCategory>)categoryLiveData.postValue(it)
        }
    }
}


inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this,
            ViewModelFactory(
                creator
            )
        ).get(T::class.java)
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }

}