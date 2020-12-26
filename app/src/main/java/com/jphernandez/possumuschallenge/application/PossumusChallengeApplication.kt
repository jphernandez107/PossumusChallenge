package com.jphernandez.possumuschallenge.application

import android.app.Application

class PossumusChallengeApplication: Application() {

    companion object {
        val appComponent: AppComponent = DaggerAppComponent.create()
    }
}