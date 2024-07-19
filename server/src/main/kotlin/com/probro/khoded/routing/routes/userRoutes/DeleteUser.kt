package com.probro.khoded.routing.routes.userRoutes

import io.ktor.server.application.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUser() {
    post<Users.Delete> {
        //Delete a specific user.
        call.respondText("We finna delete a muthafuckkaaaaa!!!")
    }
}