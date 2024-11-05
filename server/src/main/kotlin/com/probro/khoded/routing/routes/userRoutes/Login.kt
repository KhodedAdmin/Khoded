package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.model.local.dto.LoginDTO
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import model.serialization.json

fun Route.login() {
    post<Users.Login> {
        try {
            val body = call.receiveText()
            val loginString = json.decodeFromString<LoginDTO>(body)


        } catch (ex: JsonConvertException) {
            call.respond(HttpStatusCode.BadRequest, ex.localizedMessage)
        }

    }
}