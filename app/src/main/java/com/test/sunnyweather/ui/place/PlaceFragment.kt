package com.test.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.test.sunnyweather.MainActivity
import com.test.sunnyweather.R
import com.test.sunnyweather.databinding.FragmentPlaceBinding
import com.test.sunnyweather.ui.weather.WeatherActivity


class PlaceFragment:Fragment() {
   private lateinit var binding:FragmentPlaceBinding
    val viewModel by lazy{
        ViewModelProviders.of(this).get(PlaceViewModel::class.java)
    }
    private lateinit var adapater:PlaceAdapater
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPlaceBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity is MainActivity && viewModel.isPlaceSaved()){
            val place=viewModel.getSavedPlace()
            val intent=Intent(context,WeatherActivity::class.java).apply{
                putExtra("location_lon",place.lon)
                putExtra("location_lat",place.lat)
                putExtra("placeName",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        adapater= PlaceAdapater(this,viewModel.placeList)
        binding.recyclerView.adapter=adapater
        binding.searchEdit.addTextChangedListener{
            editable->
            val content=editable.toString()
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                binding.recyclerView.visibility=View.GONE
                binding.bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapater.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer { result->
            val places=result.getOrNull()
            if(places!=null){
                binding.recyclerView.visibility=View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapater.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到该名称所对应的地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }

        })
    }

}