package com.probro.khoded.routing.routes.serviceRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.model.local.dto.ServiceDTO
import com.probro.khoded.model.repositories.ServiceRepository
import com.probro.khoded.model.repositories.ServiceRepository.deleteService
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
            getService()
            createService()
            editService()
            deleteService()
        }
    }
}