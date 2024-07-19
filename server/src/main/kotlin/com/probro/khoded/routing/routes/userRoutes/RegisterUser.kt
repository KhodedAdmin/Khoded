package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerUser() {
    post<Users.Register> {
        val userDTO = call.receive<UserDTO>()
//        val newUser = UserRepository.createNewUser(
//            userDTO.name, userDTO.email, userDTO.phone, userDTO.password
//        )
//        call.respond(newUser)
        call.respondText("received user $userDTO")
    }
}
