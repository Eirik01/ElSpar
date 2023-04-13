package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.Observation
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
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
            val json = Json { ignoreUnknownKeys = true }
            val token : String = fetchToken(json) // should really be cached
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
            return parseFrostJson(json, responseString)
        }catch(e: Exception){
            Log.d("METAPI Connection", "Connection failed \n$e")
            e.printStackTrace()
            //dummy data
            return ObservationData("0","0",listOf(Observation("0",0.0, "0","0","0")))
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun fetchToken(json : Json):String{
        val params = listOf(
            "client_id" to "bbf931fb-056d-4d69-bb71-e47e18596d1e",
            "client_secret" to "598596f9-1aaf-44fe-ac3f-fd447f2303e9",
            "grant_type" to "client_credentials"
        )
        val tokenResponse : HttpResponse = runBlocking{
            client.post("https://frost.met.no/auth/accessToken") {
                body = FormDataContent(Parameters.build{
                    params.forEach { (key, value) -> append(key, value) }
                })
            }
        }
        val tokenResponseString :String = tokenResponse.bodyAsText()
        val rootToken = json.decodeFromString<JsonElement>(tokenResponseString)
        val jsonToken = rootToken.jsonObject["access_token"]
        return jsonToken.toString().substring(1,jsonToken.toString().length-1)
    }
    suspend fun parseFrostJson(
        json : Json,
        responseString : String
    ): ObservationData{
        val root = json.decodeFromString<JsonElement>(responseString)
        val dataArray = root.jsonObject["data"] as JsonArray
        return json.decodeFromString<ObservationData>(dataArray[0].toString())
    }
}
