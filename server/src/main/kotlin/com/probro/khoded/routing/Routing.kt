package com.probro.khoded.routing

import Greeting
import com.probro.khoded.routing.routes.projectRoutes.projectRouting
import com.probro.khoded.routing.routes.serviceRoutes.serviceRouting
import com.probro.khoded.routing.routes.userRoutes.userRouting
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing


fun Application.configureRouting() {
    install(Resources)
    defaultRouting()
    userRouting()
    projectRouting()
    serviceRouting()
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