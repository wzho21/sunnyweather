package com.test.sunnyweather.logic.network

import com.test.sunnyweather.SunnyWeatherApplication
import com.test.sunnyweather.logic.model.DailyResponse
import com.test.sunnyweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.KeyWeather}/{lon},{lat}/realtime.json ")
    fun getRealtimeWeather(@Path("lon") lon:String,@Path("lat") lat:String): Call<RealTimeResponse>
    @GET("v2.5/${SunnyWeatherApplication.KeyWeather}/{lon},{lat}/daily.json")
    fun getDailytimeWeather(@Path("lon") lon:String,@Path("lat") lat:String):Call<DailyResponse>
}