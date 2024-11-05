package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.local.dto.Role
import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import model.serialization.json


fun Route.editUser() {
    post<Users.Edit> {
        //Edit a specific user.
        try {
            //Get the session
            val sessionCookie = call.sessions.get<UserCookie>()
            println("sessionCookie was $sessionCookie")

            //get the user to edit
            val user: UserDTO = call.receiveText().let {
                json.decodeFromString<UserDTO>(it)
            }
            //validate user in session has ability to adjust this user.
            sessionCookie?.let { userCookie ->
                if (
                    user.id?.equals(
                        userCookie.userID,
                        ignoreCase = true
                    ) == true ||  // Trying to delete OWN account
                    userCookie.role.equals(Role.EMPLOYEE.value, ignoreCase = true) ||
                    userCookie.role.equals(Role.ADMIN.value, ignoreCase = true)
                ) {
                    //edit the user
                    UserRepository.updateUser(user)?.let { updatedUser ->
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = json.encodeToString(updatedUser.toDTO())
                        )
                    } ?: kotlin.run {
                        call.respond(
                            status = HttpStatusCode.ExpectationFailed,
                            "Cannot find user to update."
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = "Cannot edit accounts other than your own."
                    )
                }
            } ?: run {
                call.respond(
                    status = HttpStatusCode.ExpectationFailed,
                    message = "Expected session cookie but none was found."
                )
            }
        } catch (ex: IllegalStateException) {
            call.respond(
                status = HttpStatusCode.NotAcceptable,
                message = "No cookie available for name 'User-cookie'. \n ${ex.message}"
            )
        } catch (ex: IllegalArgumentException) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Endpoint requires instance of UserDTO sent as body. \n ${ex.message}"
            )
        } catch (ex: SerializationException) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Issue serializing json. \n ${ex.message}"
            )
        }
    }
}

