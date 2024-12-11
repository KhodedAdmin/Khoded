package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.repositories.ProjectRepository
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
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
            get<Projects.CreateNewProject> {
                //TODO: Create a new project associated with this user.
            }
            get<Projects.ID> {
                //TODO: RETURN THE PROJECT WITH THE SPECIFIC ID
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