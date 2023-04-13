package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.BufferedReader
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
        station : String, // blindern
        time: String,
        element: String ,

    ): ObservationData? {
        val token : String = "bbf931fb-056d-4d69-bb71-e47e18596d1e"
        if(validateToken(token) == 0){
            val url : String = baseURL+
                    "sources=${station}" +
                    "&referencetime=${time}" +
                    "&elements=${element}"
            //Log.d("URL",endURL)
            val response : HttpResponse =  client.get(url){
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            val responseString: String = response.toString()
            val observationData: ObservationData = Json.decodeFromString(responseString)
            Log.d("observationList", observationData.observations.toString())
            return observationData
        }
        return null
    }

     fun validateToken(token : String): Int{
        val command = "curl --user $token: '$url'"
        val processBuilder = ProcessBuilder(command.split(" "))
        val process = processBuilder.start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()

        var line: String? = reader.readLine()
        while (line != null) {
            output.append(line + "\n")
            line = reader.readLine()
        }

        val exitCode = process.waitFor()
        //val result = output.toString()
        Log.d("exitCode",exitCode.toString())
        return exitCode
    }
}