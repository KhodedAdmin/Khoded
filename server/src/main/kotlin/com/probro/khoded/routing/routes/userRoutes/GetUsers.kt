package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.model.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.encodeToString
import model.serialization.json

fun Route.getUsers() {
    get<Users> { users ->
        //Fetch all users according to the appropriate role level.
//        val user = call.receive<User>()
        //fetch users that this user type is allowed to receive.
//        call.respondText("Got the the /users endpoint.")
        try {
            val users = UserRepository.getAllUsers()
            val userJson = json.encodeToString(users)
            println("GET ${Users.Register} with a response \n $userJson")
            call.respond(userJson)
        } catch (ex: Exception) {
            println(ex.message)
            call.respond(HttpStatusCode.InternalServerError, message = ex.localizedMessage)
        }


    }
}