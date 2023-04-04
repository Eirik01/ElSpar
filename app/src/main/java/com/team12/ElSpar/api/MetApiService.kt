package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*



interface MetApiService {
    suspend fun getWeatherDataPerLocation(
        time: String,
        station : String,
        element: String ,
    ):ObservationData
}


class DefaultMetApiService(
    private val client: HttpClient,
    private val baseURL: String = "https://frost.met.no/observations/v0.jsonld?",
):MetApiService{
    override suspend fun getWeatherDataPerLocation(
        station : String, // blindern
        time: String,
        element: String ,

    ): ObservationData {
        val url : String = baseURL+
                "sources=${station}" +
                "&referencetime=${time}" +
                "&elements=${element}"
        //Log.d("URL",endURL)
        val response : HttpResponse =  client.get(url){
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        val responseString: String = response.toString()
        val observationData: ObservationData = Json.decodeFromString(responseString)
        Log.d("observationList", observationData.observations.toString())
        return observationData
    }
}