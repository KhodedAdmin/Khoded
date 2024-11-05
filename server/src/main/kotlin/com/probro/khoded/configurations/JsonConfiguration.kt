package com.probro.khoded.configurations

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import model.serialization.json

fun Application.configureJson() {
    install(ContentNegotiation) {
        json
    }
}