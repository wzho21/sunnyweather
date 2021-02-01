package com.test.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val code:String,val location:List<Place>)
data class Place(val name:String,val id:String,val lat:String,val lon:String,val adm2:String,val adm1:String,val country:String,val other:Other)
data class Other(val tz:String,val utcOFFset:String,val isDst:String,val type:String,val rank:String,val fxLink:String)
data class Location(val lon:String,val lat: String)