package com.probro.khoded.routing.routes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.repositories.UserRepository
import com.probro.khoded.routing.Routes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.applyUserRoutes() {
    routing {
        post(Routes.UserRoutes.LOGIN.route) {
            val user = call.receive<User>()

        }

        post(Routes.UserRoutes.RegisterUser.route) {
            val userDTO = call.receive<UserDTO>()
            val newUser = UserRepository.createNewUser(
                userDTO.name, userDTO.email, userDTO.phone, userDTO.password
            )
            call.respond(newUser)
        }

        authenticate(AuthTypes.BASE_AUTH.name) {
            post(Routes.UserRoutes.UpdateUser.route) {

            }
            post(Routes.UserRoutes.DeleteUser.route) {

            }
        }

    }
}