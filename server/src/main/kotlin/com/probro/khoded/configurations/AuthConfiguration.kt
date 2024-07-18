package com.probro.khoded.configurations

import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.repositories.UserRepository
import com.probro.khoded.routing.Routes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*

fun Application.configureAuthentication() {
    install(Authentication) {
        basic(AuthTypes.BASE_AUTH.name) {
            skipWhen { call ->
                !call.sessionId.isNullOrEmpty()  //SKIP IF WE ALREADY HAVE A SESSION GOING
            }
            realm = "Access to the '${Routes.UserRoutes.UpdateUser} path"
            validate { userPasswordCredential ->
                val result = UserRepository.findUserByName(userPasswordCredential.name)
                    .firstOrNull { local ->
                        local[KhodedUsers.name] == userPasswordCredential.name
                                && local[KhodedUsers.password] == userPasswordCredential.password
                    }
                result?.get(KhodedUsers.userName)?.let {
                    UserIdPrincipal(it)
                }
            }
        }
    }
}

enum class AuthTypes {
    BASE_AUTH, OAUTH, SESSION_AUTH
}