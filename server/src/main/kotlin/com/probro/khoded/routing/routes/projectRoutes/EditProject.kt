package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.model.local.dto.ProjectDTO
import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import model.serialization.json

fun Route.editProject() {
    post<Projects.ID.Edit> {
        val projectID = call.request.pathVariables["id"]
        val projectUpdates = call.receiveText().let {
            json.decodeFromString<ProjectDTO>(it)
        }
        if (projectUpdates.projectID == projectID) {
            ProjectRepository.updateProject(projectUpdates)?.let { updatedProject ->
                call.respond(HttpStatusCode.OK, updatedProject.toDTO())
            } ?: call.respond(
                HttpStatusCode.NotFound,
                "Couldn't find project with the given ID."
            )
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                "Project ID does not match path parameter."
            )
        }
    }
}
