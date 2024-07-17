package com.probro.khoded.routing

import Greeting
import com.probro.khoded.routing.routes.applyUserRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.serialization.json


fun Application.configureRouting() {
    install(StatusPages) {
        exception(IllegalStateException::class) { call, cause ->
            call.respondText("Application in Illegal State: ${cause.message}")
        }
    }
    install(ContentNegotiation){
        json
    }
    applyUserRoutes()
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }
    }

}