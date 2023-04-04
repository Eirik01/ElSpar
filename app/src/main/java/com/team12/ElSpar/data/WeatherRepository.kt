package com.team12.ElSpar.data

import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.model.ObservationData
import java.time.LocalDateTime

interface WeatherRepository {
    suspend fun getWeatherDataPerLocation(
        time: String,
        station : String,
        element : String
    ):ObservationData
}

class DefaultWeatherRepository(
    private val metApiService: MetApiService
):WeatherRepository  {
    override suspend fun getWeatherDataPerLocation(
        time: String,
        station: String,
        element : String
    ): ObservationData {
        return metApiService.getWeatherDataPerLocation(time,station,element)
    }
}