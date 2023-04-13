package com.team12.ElSpar.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

object KtorClient {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()

        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = "X-Gravitee-API-Key",
                        password = "be4467b2-932e-4e03-b9ff-60023760c6a2"
                    )
                }
                realm = "Access to the '/' path"
            }
        }
    }
}