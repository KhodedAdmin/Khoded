package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.model.repositories.ServiceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.getService(){
    get<Services.GetAll> {
        val services = com.probro.khoded.model.repositories.ServiceRepository.getAllServices()
        println("Got the services $services")
        if (services.isEmpty()) {
            call.respond(
                io.ktor.http.HttpStatusCode.OK,
                "There are currently no services available"
            )
        } else {
            call.respond(
                io.ktor.http.HttpStatusCode.OK,
                services
            )
        }
    }
    get<Services.ID> {
        val serviceID = call.pathParameters["id"] ?: kotlin.run {
            call.respond(
                io.ktor.http.HttpStatusCode.BadRequest,
                "No ID to look for service."
            )
            return@get
        }
        val service = com.probro.khoded.model.repositories.ServiceRepository.getServiceByID(serviceID)
        service?.let {
            println("Successfully got service $service")
            call.respond(io.ktor.http.HttpStatusCode.OK, service)
        } ?: call.respond(
            io.ktor.http.HttpStatusCode.NotFound, "Unable to find a service with ID $serviceID"
        )

    }
}