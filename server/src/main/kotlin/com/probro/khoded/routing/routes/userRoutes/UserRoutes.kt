package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.AuthTypes
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

@Resource("/users")
class Users() {
    @Resource("register")
    class Register(val parent: Users)

    @Resource("edit")
    class Edit(val parent: Users)

    @Resource("delete")
    class Delete(val parent: Users)
}

fun Application.userRouting() {
    routing {
        registerUser()
        authenticate(AuthTypes.BASE_AUTH.name) {
            getUsers()
            editUser()
            deleteUser()
        }
    }
}