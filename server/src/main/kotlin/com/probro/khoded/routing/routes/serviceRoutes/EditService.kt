package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.model.local.dto.ServiceDTO
import com.probro.khoded.model.repositories.ServiceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.editService() {
    post<Services.ID.Edit> {
        val serviceID = call.pathParameters["id"].toString()
        val newData = call.receive<ServiceDTO>()

        val result = com.probro.khoded.model.repositories.ServiceRepository.updateServiceByID(newData, serviceID)
        result?.let {
            println("Successfully updated object $it")
            call.respond(io.ktor.http.HttpStatusCode.OK, it)
        } ?: call.respond(
            io.ktor.http.HttpStatusCode.NotFound,
            "Couldnt find a service with ID of $serviceID"
        )
    }
}