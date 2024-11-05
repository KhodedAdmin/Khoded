package com.probro.khoded.routing

import Greeting
import com.probro.khoded.routing.routes.userRoutes.userRouting
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    install(Resources)
    defaultRouting()
    userRouting()
}

fun Application.defaultRouting() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }
    }
}