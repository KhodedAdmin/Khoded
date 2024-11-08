package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.model.local.dto.Role
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

@Resource("users")
class Users() {
    @Resource("register")
    class Register(var role: String? = Role.GUEST.value, val parent: Users)

    @Resource("login")
    class Login(var role: String? = Role.GUEST.value, val parent: Users)

    @Resource("callback")
    class Callback(val parent: Users)

    @Resource("edit")
    class Edit(val parent: Users)

    @Resource("delete")
    class Delete(val parent: Users)
}

fun Application.userRouting() {
    routing {
            login()
        registerUser()
        authenticate(AuthTypes.BASE_AUTH.name) {
            getUsers()
            editUser()
            deleteUser()
        }
    }
}