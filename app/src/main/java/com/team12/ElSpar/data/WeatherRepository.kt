package com.team12.ElSpar.data

import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.model.ObservationData
import java.time.LocalDateTime

interface WeatherRepository {
    suspend fun getWeatherDataPerLocation(
        station : String,
        time: String,
        element : String
    ):ObservationData?
}

class DefaultWeatherRepository(
    private val metApiService: MetApiService
):WeatherRepository  {
    override suspend fun getWeatherDataPerLocation(
        station: String,
        time: String,
        element : String
    ): ObservationData? {
        val output = metApiService.getWeatherDataPerLocation(
            station = station,
            time = time,
            element = element
        )
        if(output != null){
            return output
        }
        return null
    }
}