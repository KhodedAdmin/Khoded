package com.probro.khoded.configurations

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.configureExceptions() {
    install(StatusPages) {
        exception(IllegalStateException::class) { call, cause ->
            call.respondText("Application in Illegal State: ${cause.message}")
        }
        exception(RequestValidationException::class) { call, cause ->
            call.respondText(status = HttpStatusCode.BadRequest, text = cause.reasons.joinToString())
        }
    }
}