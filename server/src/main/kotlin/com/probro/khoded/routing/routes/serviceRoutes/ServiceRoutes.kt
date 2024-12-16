package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.model.local.dto.ServiceDTO
import com.probro.khoded.model.repositories.ServiceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.SerializationException
import model.serialization.json

@Resource("services")
class Services {
    @Resource("all")
    class GetAll(val parent: Services)

    @Resource("new")
    class CreateNewService(val parent: Services)

    @Resource("{id}")
    class ID(val id: String, val parent: Services) {
        @Resource("edit")
        class Edit(val parent: ID)

        @Resource("delete")
        class Delete(val parent: ID)
    }
}

fun Application.serviceRouting() {
    routing {
        authenticate(AuthTypes.BASE_AUTH.name) {
            get<Services.GetAll> {
                val services = ServiceRepository.getAllServices()
                println("Got the services $services")
                if (services.isEmpty()) {
                    call.respond(
                        HttpStatusCode.OK,
                        "There are currently no services available"
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        services
                    )
                }
            }
            get<Services.ID> {
                val serviceID = call.pathParameters["id"] ?: kotlin.run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "No ID to look for service."
                    )
                    return@get
                }
                val service = ServiceRepository.getServiceByID(serviceID)
                service?.let {
                    println("Successfully got service $service")
                    call.respond(HttpStatusCode.OK, service)
                } ?: call.respond(
                    HttpStatusCode.NotFound, "Unable to find a service with ID $serviceID"
                )

            }
            post<Services.CreateNewService> {
                val newServiceDTO = call.receiveText()
                try {
                    val newDTO = json.decodeFromString<ServiceDTO>(newServiceDTO)
                    val result = ServiceRepository.createNewService(newDTO)
                    call.respond(HttpStatusCode.OK, result)
                } catch (ex: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "No valid service sent. $newServiceDTO"
                    )
                } catch (ex: SerializationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Problem parsing the service. $newServiceDTO"
                    )
                }
            }
            post<Services.ID.Edit> {
                val serviceID = call.pathParameters["id"].toString()
                val newData = call.receive<ServiceDTO>()

                val result = ServiceRepository.updateServiceByID(newData, serviceID)
                result?.let {
                    println("Successfully updated object $it")
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    "Couldnt find a service with ID of $serviceID"
                )
            }
            post<Services.ID.Delete> {
                val serviceID = call.pathParameters["id"].toString()
                val isDeleted = ServiceRepository.deleteService(serviceID)
                when (isDeleted) {
                    true -> call.respond(
                        HttpStatusCode.Gone,
                        "Successfully deleted service with ID $serviceID"
                    )

                    false -> call.respond(
                        HttpStatusCode.ExpectationFailed, "Unable to delete service."
                    )

                    null -> call.respond(
                        HttpStatusCode.NotFound,
                        "Couldnt find service with id $serviceID"
                    )
                }
            }
        }
    }
}