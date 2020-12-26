package com.jphernandez.possumuschallenge.application

import com.jphernandez.possumuschallenge.questions.QuestionFragment
import com.jphernandez.possumuschallenge.repositories.TriviaRepository
import com.jphernandez.possumuschallenge.repositories.TriviaRepositoryImpl
import com.jphernandez.possumuschallenge.services.TriviaService
import com.jphernandez.possumuschallenge.services.TriviaServiceImpl
import com.jphernandez.possumuschallenge.services.TriviaServiceRetrofit
import com.jphernandez.possumuschallenge.triviaSearch.TriviaSearchFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class Providers {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val serverUrl = "https://opentdb.com"
        val httpClient = OkHttpClient.Builder()

        return Retrofit.Builder()
            .baseUrl(serverUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun triviaServiceProvider(retrofit: Retrofit): TriviaService =
        TriviaServiceImpl(TriviaServiceRetrofit.create(retrofit))


    @Provides
    @Singleton
    fun triviaRepositoryProvider(triviaService: TriviaService): TriviaRepository =
        TriviaRepositoryImpl(triviaService)

}

@Singleton
@Component(modules = [Providers::class])
interface AppComponent {
    fun inject(triviaSearchFragment: TriviaSearchFragment)
    fun inject(questionFragment: QuestionFragment)
}