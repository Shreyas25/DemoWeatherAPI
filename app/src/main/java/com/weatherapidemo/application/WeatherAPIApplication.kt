package com.weatherapidemo.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherAPIApplication : Application() {

    companion object {
        var appInstance: WeatherAPIApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}