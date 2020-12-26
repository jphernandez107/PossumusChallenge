package com.jphernandez.possumuschallenge.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.jphernandez.possumuschallenge.R
import com.jphernandez.possumuschallenge.triviaSearch.TriviaSearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<TriviaSearchFragment>(R.id.fragment_container_view)
        }
    }
}