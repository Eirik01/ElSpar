package com.team12.ElSpar.domain

import android.util.Log
import com.team12.ElSpar.data.WeatherRepository

class GetTemperatureUseCase(
    private val weatherRepository : WeatherRepository
) {
    suspend operator fun invoke(
        lon: String,
        lat: String,
        alt : String
    ):List<Double>{
        val temperatureList : MutableList<Double> = mutableListOf()
        val data = weatherRepository.getWeatherDataPerLocation(
            lon = lon,
            lat = lat,
            alt = alt
        )
        if(data != null){
            for(observation in data.timeseries){
                temperatureList.add(observation.air_temperature)
            }
            //Log.d("temperatureList",temperatureList.toString())
            return temperatureList
        }
        //Log.d("temperatureList",temperatureList.toString())
        return temperatureList
    }
}