package com.test.sunnyweather.ui.weather

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.sunnyweather.R
import com.test.sunnyweather.databinding.*
import com.test.sunnyweather.logic.model.Weather
import com.test.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    lateinit var binderFor: ForecastBinding
    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        binderFor = ForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        if (viewModel.locationLon.isEmpty()) {
            viewModel.locationLon = intent.getStringExtra("location_lon") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("placeName") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法读取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipefresh.isRefreshing = false
        })
        binding.swipefresh.setColorSchemeResources(R.color.purple_200)
        refreshWeather()
        binding.swipefresh.setOnRefreshListener {
            refreshWeather()
        }
        val changeBtn = findViewById<Button>(R.id.changeBtn)
        changeBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }
    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLon,viewModel.locationLat)
        binding.swipefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        val placeName=findViewById<TextView>(R.id.placeName)
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        val currentTemp=findViewById<TextView>(R.id.currentTemp)
        val currentAQI=findViewById<TextView>(R.id.currentAQI)
        val currentSky=findViewById<TextView>(R.id.currentSky)
        val nowLayout=findViewById<RelativeLayout>(R.id.nowLayout)
        val currentTemptext = "${realtime.temperature.toInt()}℃"
        currentTemp.text = currentTemptext
        currentSky.text = getSky(realtime.skycon).info
        currentAQI.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        val forecastLayout=findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temp=daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, binderFor.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val iconInfo = view.findViewById(R.id.skyninfo) as TextView
            val tempInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
           val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            iconInfo.text=sky.info
            val tempText="${temp.min.toInt()}~${temp.max.toInt()}℃"
            tempInfo.text=tempText

            forecastLayout.addView(view)
        }
        val lifeIndex=daily.lifeIndex
        val coldRiskText=findViewById<TextView>(R.id.coldRiskText)
        val dressingText=findViewById<TextView>(R.id.dressingText)
        val ultravioletText=findViewById<TextView>(R.id.ultravioletText)
        val carWashingText=findViewById<TextView>(R.id.carWashingText)
        coldRiskText.text=lifeIndex.coldRisk[0].desc
        dressingText.text=lifeIndex.dressing[0].desc
        ultravioletText.text=lifeIndex.ultraviolet[0].desc
        carWashingText.text=lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility= View.VISIBLE
    }
}