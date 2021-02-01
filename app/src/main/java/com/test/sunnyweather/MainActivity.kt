package com.test.sunnyweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.sunnyweather.databinding.ActivityMainBinding
import com.test.sunnyweather.logic.network.PlaceService
import com.test.sunnyweather.logic.network.ServiceCreator

class MainActivity : AppCompatActivity() {
    private lateinit var bind:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }
}