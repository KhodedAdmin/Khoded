package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.deleteProject() {
    post<Projects.ID.Delete> {
        val projectID = call.request.pathVariables["id"]
        projectID?.let {
            val deleted: Boolean? = ProjectRepository.deleteProject(projectID)
            when (deleted) {
                true -> call.respond(
                    HttpStatusCode.Gone,
                    "Project Has successfully been deleted."
                )
                false -> call.respond(
                    HttpStatusCode.ExpectationFailed,
                    "Unable To delete project with id $projectID" +
                            ""
                )
                null -> call.respond(
                    HttpStatusCode.NotFound,
                    "Project with id $projectID not found in Db."
                )
            }
        } ?: call.respond(
            HttpStatusCode.BadRequest,
            "No Id sent for project to delete."
        )
    }
}