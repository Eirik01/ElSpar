package com.team12.ElSpar.domain

import android.util.Log
import com.team12.ElSpar.data.SettingsRepository
import com.team12.ElSpar.data.WeatherRepository

class GetTemperatureUseCase(
    private val weatherRepository : WeatherRepository,
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
            Log.d("temperatureList",temperatureList.toString())
            return temperatureList
        }
        Log.d("temperatureList",temperatureList.toString())
        return temperatureList
    }
}