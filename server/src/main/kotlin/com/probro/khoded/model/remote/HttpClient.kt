package com.probro.khoded.model.remote

import com.probro.khoded.configurations.UserCookie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import model.serialization.json

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json
    }
}

private suspend fun getPersonalGreeting(
    httpClient: HttpClient,
    userSession: UserCookie
): String = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
    headers {
        append(HttpHeaders.Authorization, "Bearer ${userSession.userToken}")
    }
}.bodyAsText()

