package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.local.dto.Role
import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receiveText
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlinx.serialization.SerializationException
import model.serialization.json
import java.util.UUID

fun Route.deleteUser() {
    post<Users.Delete> {
        //Delete a specific user.
        try {
            val requestBody = call.receiveText()
            val userToDelete = json.decodeFromString<UserDTO>(requestBody)
            val sessionData = call.sessions.get<UserCookie>()
            sessionData?.let { data ->
                if (
                    userToDelete.id?.equals(
                        data.userID,
                        ignoreCase = true
                    ) == true ||  // Trying to delete OWN account
                    data.role.equals(Role.EMPLOYEE.value, ignoreCase = true) ||
                    data.role.equals(Role.ADMIN.value, ignoreCase = true)
                ) {
                    println("Looking for user to delete.")
                    UserRepository.deleteUser(
                        UUID.fromString(userToDelete.id)
                    )
                    call.respond(
                        status = HttpStatusCode.Gone,
                        message = "User has successfully been deleted."
                    )
                }
            } ?: kotlin.run {
                call.respond(
                    HttpStatusCode.ExpectationFailed,
                    message = "No session data found for this user."
                )
            }
        } catch (ex: BadRequestException) {
            ex.printStackTrace()
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ex.message ?: ex.localizedMessage
            )
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            call.respond(
                status = HttpStatusCode.BadGateway,
                message = "Endpoint requires instance of UserDTO sent as body. \n ${ex.message}"
            )
        } catch (ex: SerializationException) {
            ex.printStackTrace()
            call.respond(
                status = HttpStatusCode.NotAcceptable,
                message = "Issue serializing json. \n ${ex.message}"
            )
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
            call.respond(
                status = HttpStatusCode.ExpectationFailed,
                message = "No provider set up for UserCookie type. \n ${ex.message}"
            )
        } catch (ex: NotFoundException) {
            ex.printStackTrace()
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "Sorry, couldn't find the user to delete. \n ${ex.message}"
            )
        }
    }
}