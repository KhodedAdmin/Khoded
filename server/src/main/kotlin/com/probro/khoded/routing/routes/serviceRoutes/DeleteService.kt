package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.model.repositories.ServiceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.deleteService(){
    post<Services.ID.Delete> {
        val serviceID = call.pathParameters["id"].toString()
        val isDeleted = com.probro.khoded.model.repositories.ServiceRepository.deleteService(serviceID)
        when (isDeleted) {
            true -> call.respond(
                io.ktor.http.HttpStatusCode.Gone,
                "Successfully deleted service with ID $serviceID"
            )

            false -> call.respond(
                io.ktor.http.HttpStatusCode.ExpectationFailed, "Unable to delete service."
            )

            null -> call.respond(
                io.ktor.http.HttpStatusCode.NotFound,
                "Couldnt find service with id $serviceID"
            )
        }
    }
}