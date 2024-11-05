package com.probro.khoded.configurations

import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.repositories.UserRepository
import com.probro.khoded.routing.routes.userRoutes.Users
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.auth.oauth
import io.ktor.server.sessions.sessionId
import io.ktor.server.sessions.sessions
import kotlinx.datetime.Clock

fun Application.configureAuthentication() {
    install(Authentication) {
        basic(AuthTypes.BASE_AUTH.name) {
            skipWhen { call ->
                !call.sessionId.isNullOrEmpty()  //SKIP IF WE ALREADY HAVE A SESSION GOING
            }
            realm = "Access to the '${Users.Edit}' path"
            validate { userPasswordCredential ->
                val result = UserRepository.findUserByName(userPasswordCredential.name)
                    .firstOrNull { local ->
                        local.name == userPasswordCredential.name
                                && local.password == userPasswordCredential.password
                    }?.also { user ->
                        sessions.set(
                            name = Cookies.USER_COOKIE.value, value = UserCookie(
                                userID = user.id.value.toString(),
                                userToken = "",
                                role = user.role,
                                timestamp = Clock.System.now().toString()
                            )
                        )
                    }
                result?.userName?.let {
                    UserIdPrincipal(it)
                } ?: kotlin.run {

                }
            }
        }
    }
}

enum class AuthTypes {
    BASE_AUTH, OAUTH, SESSION_AUTH
}