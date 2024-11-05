package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.encodeToString
import model.serialization.json
import org.postgresql.util.PSQLException
import java.sql.BatchUpdateException

fun Route.registerUser() {
    post<Users.Register> {
        val role = call.request.queryParameters["role"]
        val rawInput = call.receive<String>()
        println("got raw input as string.")
        val userDTO = json.decodeFromString<UserDTO>(rawInput).also { user ->
            role?.let { user.role = it }
        }
        var returnString = "got to the route and role is $role"
        println(userDTO)
        try {
            val newUser = UserRepository.createNewUser(userDTO)
            val userString = json.encodeToString(newUser.toDTO())
            returnString += " received user $userString"
            println(returnString)
            call.respond(status = HttpStatusCode.OK, message = userString)
        } catch (ex: BatchUpdateException) {
            println(ex.localizedMessage)
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ex.localizedMessage ?: "Unknown error."
            )
        } catch (ex: PSQLException) {
            println(ex.localizedMessage)
            val message = if (ex.localizedMessage.contains("already exists", ignoreCase = true)) {
                "Sorry, this username is already taken."
            } else {
                ex.localizedMessage
            }
            call.respond(
                status = HttpStatusCode.ExpectationFailed,
                message = message
            )
        } catch (ex: Exception) {
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ex.localizedMessage ?: "Unknown error."
            )
        }
    }
}
