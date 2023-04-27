package com.team12.ElSpar.data

import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.model.ObservationData

interface WeatherRepository {
    suspend fun getWeatherDataPerLocation(
        lon : String,
        lat: String,
    ):ObservationData?
}

class DefaultWeatherRepository(
    private val metApiService: MetApiService
):WeatherRepository  {
    override suspend fun getWeatherDataPerLocation(
        lat: String,
        lon: String,
    ): ObservationData? {
        val output = metApiService.getWeatherDataPerLocation(
            lat = lat,
            lon = lon,
        )
        if(output != null){
            return output
        }
        return null
    }
}