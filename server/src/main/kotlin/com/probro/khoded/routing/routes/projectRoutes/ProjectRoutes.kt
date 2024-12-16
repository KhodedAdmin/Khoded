package com.probro.khoded.routing.routes.projectRoutes

import com.probro.khoded.configurations.AuthTypes
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing

@Resource("projects")
class Projects {
    @Resource("new")
    class CreateNew(val parent: Projects)

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
        authenticate(AuthTypes.BASE_AUTH.name) {
//            createNewProject()
//            getProjects()
//            editProject()
//            deleteProject()
        }
    }
}






