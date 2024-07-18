package com.probro.khoded.configurations

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.configureExceptions() {
    install(StatusPages) {
        exception(IllegalStateException::class) { call, cause ->
            call.respondText("Application in Illegal State: ${cause.message}")
        }
    }
}