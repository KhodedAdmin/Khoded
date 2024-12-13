package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.local.dto.ProjectDTO
import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.sessions
import kotlinx.serialization.encodeToString
import model.serialization.json

@Resource("projects")
class Projects {
    @Resource("new")
    class CreateNewProject(val parent: Projects)

    @Resource("{id}")
    class ID(val id: String, val parent: Projects = Projects()) {
        @Resource("edit")
        class Edit(val parent: ID)

        @Resource("delete")
        class Delete(val parent: ID)
    }
}

fun Application.projectRouting() {
    routing {
        authenticate(AuthTypes.OAUTH.name) {
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
            post<Projects.CreateNewProject> {
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
            post<Projects.ID.Edit> {
                //TODO: Update the project with this Id according to incoming data.s
            }
            post<Projects.ID.Delete> {
                //TODO: Delete the project with this ID from the Database.
            }
        }
    }
}