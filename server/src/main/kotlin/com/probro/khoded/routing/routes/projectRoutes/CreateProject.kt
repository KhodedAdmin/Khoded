package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.local.dto.ProjectDTO
import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.sessions.sessions

fun Route.createNewProject() {
    post<Projects.CreateNew> {
        val newProject = call.receive<ProjectDTO>()
        val cookie = call.sessions.get(Cookies.USER_COOKIE.value) as UserCookie
        cookie.userID?.let { id ->
            val createdProject = ProjectRepository.createProjectForUser(id, newProject)
            call.respond(HttpStatusCode.Created, createdProject.toDTO())
        } ?: call.respond(
            HttpStatusCode.Unauthorized,
            "Must be signed in to create a project."
        )
    }
}