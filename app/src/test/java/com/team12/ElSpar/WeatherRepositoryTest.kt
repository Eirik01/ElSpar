package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.fake.FakeMetApiService
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherRepositoryTest {
    val weatherRepository = DefaultWeatherRepository(
        FakeMetApiService(),
        FakeMetApiService()
    )
    @Test
    fun weatherRepository_priceAreaToWeatherLocation_correctWeatherLocation(){

    }
}