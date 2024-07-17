package com.probro.khoded.routing.routes

import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.routing.Routes
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*


fun Application.applyUserRoutes() {
    routing {
        post(Routes.UserRoutes.LOGIN) {
            val user = call.receive<User>()

        }

    }
}