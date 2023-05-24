package com.team12.ElSpar.fake

import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.model.ObservationData

//Mock up for tests. NOT IMPLEMENTED YET.
class FakeMetApiService: MetApiService {
    override suspend fun getWeatherDataPerLocation(lat: String, lon: String): ObservationData? {
        return null
    }

}