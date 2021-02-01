package com.test.sunnyweather.logic.network

import com.test.sunnyweather.SunnyWeatherApplication
import com.test.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/city/lookup?key=${SunnyWeatherApplication.KeyCountry}")
    fun searchPlaces(@Query("location") location: String): Call<PlaceResponse>

}