package com.team12.ElSpar.api

import android.util.Log
import com.team12.ElSpar.model.ObservationData
import io.ktor.client.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
            val token : String = "be4467b2-932e-4e03-b9ff-60023760c6a2"
            val url = baseURL+"sources=$station&referencetime=$time&elements=$element"
            Log.d("code",authenticate(token, url).toString())
            val response : HttpResponse =  client.get(baseURL){
                headers {
                    //append("X-Gravitee-API-Key", token)
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                }
                parameter("sources", station)
                parameter("referencetime", time)
                parameter("elements", element)
            }
            val responseString: String = response.toString()
            val observationData: ObservationData = Json.decodeFromString(responseString)
            Log.d("observationList", observationData.observations.toString())
            return observationData

        }catch(e: IOException){
            Log.d("METAPI Connection", "Connection failed")
            e.printStackTrace()
            return null
        }
    }

<<<<<<< Updated upstream
     fun validateToken(token : String): Int{
        val command = "curl --user $token: '$url'"
        val processBuilder = ProcessBuilder(command.split(" "))
        val process = processBuilder.start()
=======
    fun authenticate(clientID: String, url: String): Int {
        val command : String = "curl -X GET --user $clientID: '$url'"
        Log.d("Auth", "Running command: $command")

        try {
            val process = Runtime.getRuntime().exec(command)
>>>>>>> Stashed changes

            val inputStream = process.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            var line: String? = null
            while ({ line = bufferedReader.readLine(); line }() != null) {
                Log.d("process", "Output: $line")
            }

            process.waitFor()
            Log.d("process","Authentication Successful")
            return process.exitValue()
        } catch (e: Exception) {
            Log.e("process", "Command execution failed $e")
            return -1
        }
    }

}