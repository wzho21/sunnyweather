package com.test.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val KeyCountry= "4b5304c45f05458e9a15ed9a65fff692"
        const val KeyWeather= "m5UWRGbnVDjACUnz"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}