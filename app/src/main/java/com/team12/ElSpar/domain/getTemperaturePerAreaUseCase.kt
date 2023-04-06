package com.team12.ElSpar.domain

import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.model.Observation

class GetTemperatureDataPerLocation(
    private val weatherRepository : WeatherRepository
) {
    suspend operator fun invoke(
        station: String,
        time: String,
        element : String
    ):List<Double>{
        val temperatureList : MutableList<Double> = mutableListOf()
        val data = weatherRepository.getWeatherDataPerLocation(
            station = station,
            time = time,
            element = element
            )
        if(data != null){
            for(observation in data.observations){
                temperatureList.add(observation.value)
            }
            return temperatureList
        }
        return temperatureList
    }
}