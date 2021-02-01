package com.test.sunnyweather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.test.sunnyweather.logic.dao.PlaceDao
import com.test.sunnyweather.logic.model.Place
import com.test.sunnyweather.logic.model.Weather
import com.test.sunnyweather.logic.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import retrofit2.http.Query
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {
    fun savePlace(place: Place)=PlaceDao.savePlace(place)
    fun getSavedPlace()= PlaceDao.getSavedPlace()
    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
    fun searchPlaces(location: String)= fire(Dispatchers.IO){
            val placeResponse=SunnyWeatherNetWork.searchPlaces(location)
            if(placeResponse.code=="200"){
                val locations=placeResponse.location
                Result.success(locations)
            }else{
                Result.failure(RuntimeException("response code is ${placeResponse.code}"))
            }

    }
    fun refreshWeather(lon:String,lat:String)= fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime=async {
                    SunnyWeatherNetWork.getRealtimeWeather(lon,lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetWork.getDailyWeather(lon,lat)
                }
                val realtimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()
                if(realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){
                    val weather=Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                            RuntimeException(
                                    "realtime response code is ${realtimeResponse.status}"+
                                            " daily response code is ${dailyResponse.status}"
                            )
                    )
                }
            }

    }
    private fun <T>fire(context:CoroutineContext,block:suspend ()->Result<T>)= liveData<Result<T>>(context) {
        val result=try{
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}