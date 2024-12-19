package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.sessions
import kotlinx.serialization.encodeToString
import model.serialization.json

fun Route.getProjects() {
    get<Projects> {
        val cookie = call.sessions.get(Cookies.USER_COOKIE.value) as UserCookie
        cookie.userID?.let { id ->
            val projects = ProjectRepository.getProjectsForUserID(id)
            call.respond(
                status = HttpStatusCode.OK,
                message = json.encodeToString(projects)
            )
        } ?: call.respond(
            HttpStatusCode.Unauthorized,
            "You need to log in before seeing project details"
        )
    }
    get<Projects.ID> {
        val id = call.request.pathVariables["id"]
        id?.let {
            val project = ProjectRepository.getProjectByID(it)
            project?.let { proj ->
                call.respond(HttpStatusCode.OK, proj.toDTO())
            } ?: call.respond(
                HttpStatusCode.NotFound,
                "Could not find a project with id $id"
            )
        } ?: call.respond(
            HttpStatusCode.BadRequest,
            "Cannot get project with id $id"
        )
    }
}