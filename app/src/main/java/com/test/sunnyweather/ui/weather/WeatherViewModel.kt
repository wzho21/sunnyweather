package com.test.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.sunnyweather.logic.Repository
import com.test.sunnyweather.logic.model.Location


import retrofit2.Response

class WeatherViewModel: ViewModel() {
    private val locationLiveData= MutableLiveData<Location>()
    var locationLon=""
    var locationLat=""
    var placeName=""
    var weatherLiveData=Transformations.switchMap(locationLiveData){
        location->
        Repository.refreshWeather(location.lon,location.lat)
    }
    fun refreshWeather(lon:String,lat:String){
        locationLiveData.value= Location(lon,lat)
    }
}