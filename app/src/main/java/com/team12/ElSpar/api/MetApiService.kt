package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.Observation
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*


interface MetApiService {
    suspend fun getWeatherDataPerLocation(
        lat : String, //latitude
        lon: String, // longtitude
        alt: String ,// altitude
    ):ObservationData?
}

class DefaultMetApiService(
    private val client: HttpClient,
    private val baseURL: String = "https://api.met.no/weatherapi/locationforecast/2.0/compact?",
):MetApiService{
    override suspend fun getWeatherDataPerLocation(
        lat : String, //latitude
        lon: String, // longtitude
        alt: String ,// altitude
    ): ObservationData {
        try{
            val json = Json { ignoreUnknownKeys = true }
            val response : HttpResponse =  client.get(baseURL){
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                }
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("altitude", alt)
            }.body()
            val responseString: String = response.bodyAsText()
            Log.d("responseString",responseString)
            val observationData = parseJson(json, responseString)
            if(observationData != null){
                return observationData
            }else{
                //dummy data
                return ObservationData(listOf(Observation(0.0,0.0)))
            }
        }catch(e: Exception){
            Log.d("METAPI Connection", "Connection failed \n$e")
            e.printStackTrace()
            //dummy data
            return ObservationData(listOf(Observation(0.0,0.0)))
        }
    }

    fun parseJson(
        json : Json,
        responseString : String
    ): ObservationData?{
        val root : JsonObject = json.decodeFromString<JsonObject>(responseString)
        val properties = root.jsonObject["properties"]
        val timeseriesArray: JsonArray? = properties?.jsonObject?.get("timeseries")?.jsonArray
        val observationList : MutableList<Observation> = mutableListOf()
        if (timeseriesArray != null) {
            timeseriesArray.forEach {
                val data: JsonObject? = it.jsonObject["data"]?.jsonObject
                val instant: JsonObject? = data?.jsonObject?.get("instant")?.jsonObject
                val details: JsonObject? = instant?.jsonObject?.get("details")?.jsonObject
                val obs: Observation = json.decodeFromString(details.toString())
                observationList.add(obs)
            }
            return ObservationData(observationList)
        }
        return null
    }
}

fun parseFrostJson(
    json : Json,
    responseString : String
): ObservationData{
    val root = json.decodeFromString<JsonElement>(responseString)
    val dataArray = root.jsonObject["data"] as JsonArray
    return json.decodeFromString<ObservationData>(dataArray[0].toString())
}