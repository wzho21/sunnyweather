package com.test.sunnyweather.ui.place

import android.view.animation.Transformation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.sunnyweather.logic.Repository
import com.test.sunnyweather.logic.model.Place
import retrofit2.http.Query

class PlaceViewModel:ViewModel (){
    private val searchLiveData= MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData= Transformations.switchMap(searchLiveData){location->
        Repository.searchPlaces(location)
    }
    fun searchPlaces(location: String){
        searchLiveData.value=location
    }
    fun savePlace(place: Place)=Repository.savePlace(place)
    fun getSavedPlace()=Repository.getSavedPlace()
    fun isPlaceSaved()=Repository.isPlaceSaved()
}