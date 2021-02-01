package com.test.sunnyweather.logic.network

import com.test.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetWork {
    private val weatherService=ServiceCreatorWeather.create<WeatherService>()
    suspend fun getDailyWeather(lon:String,lat:String)= weatherService.getDailytimeWeather(lon,lat).await()
    suspend fun getRealtimeWeather(lon:String,lat:String)= weatherService.getRealtimeWeather(lon,lat).await()
    private val placeService=ServiceCreator.create<PlaceService>()
    suspend fun searchPlaces(location:String)= placeService.searchPlaces(location).await()
    private suspend fun <T>Call<T>.await():T{
        return suspendCoroutine {continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null)continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

        }
    }
}