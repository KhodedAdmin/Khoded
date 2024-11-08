package com.probro.khoded

import SERVER_PORT
import com.probro.khoded.configurations.configureAuthentication
import com.probro.khoded.configurations.configureExceptions
import com.probro.khoded.configurations.configureJson
import com.probro.khoded.configurations.configureNetwork
import com.probro.khoded.configurations.configureSessions
import com.probro.khoded.model.remote.applicationHttpClient
import com.probro.khoded.routing.configureRouting
import io.ktor.client.HttpClient
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(httpClient: HttpClient = applicationHttpClient) {
    configureAuthentication(httpClient)
    configureExceptions()
    configureJson()
    configureNetwork()
    configureSessions()
    configureRouting()
}