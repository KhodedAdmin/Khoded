package com.probro.khoded.routing.routes.userRoutes

import io.ktor.server.application.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.editUser() {
    post<Users.Edit> {
        //Edit a specific user.
        call.respondText("Reached the edit User endpoint")
    }
}
