package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.Observation
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


interface MetApiService {
    suspend fun getWeatherDataPerLocation(
        station : String,
        time: String,
        element: String ,
    ):ObservationData?
}


class DefaultMetApiService(
    private val client: HttpClient,
    private val baseURL: String = "https://frost.met.no/observations/v0.jsonld?",
):MetApiService{
    override suspend fun getWeatherDataPerLocation(
        station : String,
        time: String,
        element: String ,
    ): ObservationData? {
        try{
            val clientIdAndSecret : String = "bbf931fb-056d-4d69-bb71-e47e18596d1e:598596f9-1aaf-44fe-ac3f-fd447f2303e9"
            val json = Json { ignoreUnknownKeys = true }
            // curl -d 'client_id=bbf931fb-056d-4d69-bb71-e47e18596d1e&client_secret=598596f9-1aaf-44fe-ac3f-fd447f2303e9&grant_type=client_credentials' 'https://frost.met.no/auth/accessToken'
            val tokenResponse : HttpResponse = client.get("https://frost.met.no/auth/accessToken") {
                headers{
                    append(HttpHeaders.Authorization,"Basic $clientIdAndSecret")
                }
            }.body()
            Log.d("tokenResponse",tokenResponse.toString())
            val tokenResponseString :String = tokenResponse.bodyAsText()
            val rootToken = json.decodeFromString<JsonElement>(tokenResponseString)
            val jsonToken = rootToken.jsonObject["access_token"]
            val token = jsonToken.toString()
            //val token : String = "-2qxydmNjQX5xN-645naGdomBvF-uY5bj61NRfQ8yj0=|AAAAAAAAKUIAAAGHf_VwbQAAAAI="
            val response : HttpResponse =  client.get(baseURL){
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                }
                parameter("sources", station)
                parameter("referencetime", time)
                parameter("elements", element)
            }.body()

            val responseString: String = response.bodyAsText()
            val root = json.decodeFromString<JsonElement>(responseString)
            val dataArray = root.jsonObject["data"] as JsonArray
            val observationData = json.decodeFromString<ObservationData>(dataArray[0].toString())
            Log.d("data",observationData.toString())
            Log.d("observationList", observationData.observations.toString())
            return observationData


        }catch(e: Exception){
            Log.d("METAPI Connection", "Connection failed \nException: $e")
            e.printStackTrace()
            //dummy data

            return ObservationData("1","2",listOf(Observation("1",69.00, "","1","")))
        }
    }
}
