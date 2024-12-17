package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.model.local.dto.ServiceDTO
import com.probro.khoded.model.repositories.ServiceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerializationException
import model.serialization.json

fun Route.createService() {
    post<Services.CreateNewService> {
        val newServiceDTO = call.receiveText()
        try {
            val newDTO = model.serialization.json.decodeFromString<ServiceDTO>(newServiceDTO)
            val result =
                com.probro.khoded.model.repositories.ServiceRepository.createNewService(newDTO)
            call.respond(io.ktor.http.HttpStatusCode.OK, result)
        } catch (ex: IllegalArgumentException) {
            call.respond(
                io.ktor.http.HttpStatusCode.BadRequest,
                "No valid service sent. $newServiceDTO"
            )
        } catch (ex: SerializationException) {
            call.respond(
                io.ktor.http.HttpStatusCode.BadRequest,
                "Problem parsing the service. $newServiceDTO"
            )
        }
    }
}