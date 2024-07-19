package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.model.local.datatables.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUsers() {
    get<Users> { users ->
        //Fetch all users according to the appropriate role level.
//        val user = call.receive<User>()
        //fetch users that this user type is allowed to receive.
        call.respondText("Got the the /users endpoint.")
    }
}