package com.team12.ElSpar.domain

import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.model.Observation

class GetTemperatureDataPerLocation(
    private val weatherRepository : WeatherRepository
) {
    suspend operator fun invoke(
        time: String,
        station: String,
        element : String
    ):List<Double>{
        val temperatureList : MutableList<Double> = mutableListOf()
        for(observation in  weatherRepository.getWeatherDataPerLocation(time,station,element).observations){
            temperatureList.add(observation.value)
        }
        return temperatureList
    }
}