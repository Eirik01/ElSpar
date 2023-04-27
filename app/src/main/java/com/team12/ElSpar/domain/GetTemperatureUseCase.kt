package com.team12.ElSpar.domain

import com.team12.ElSpar.data.WeatherRepository

class GetTemperatureUseCase(
    private val weatherRepository : WeatherRepository,
) {
    suspend operator fun invoke(
        lon: String,
        lat: String,
    ):Map<String,Double>{
        val temperatureMap : MutableMap<String,Double> = mutableMapOf()
        val data = weatherRepository.getWeatherDataPerLocation(
            lon = lon,
            lat = lat,
        )
        if(data != null){
            for(observation in data.timeseries){
                temperatureMap[observation.time] = observation.air_temperature
            }
            //Log.d("temperatureList",temperatureMap.toString())
            return temperatureMap
        }
        return temperatureMap
    }
}