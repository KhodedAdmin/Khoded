package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.sessions.sessions
import kotlinx.datetime.Clock
import model.utils.OAuthUtils

fun Route.login() {
    authenticate(AuthTypes.OAUTH.name) {
        get<Users.Login> {
            // Redirects to 'authorizeUrl' automatically
        }
        get<Users.Callback> {
            val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            // redirects home if the url is not found before authorization
            currentPrincipal?.let { principal: OAuthAccessTokenResponse.OAuth2 ->
                principal.state?.let { state ->
                    call.sessions.set(
                        name = Cookies.USER_COOKIE.value,
                        UserCookie(
                            oAuthState = state,
                            userToken = principal.accessToken,
                            timestamp = Clock.System.now().toString()
                        )
                    )
                    //TODO: Save the token in the DB.
                    OAuthUtils.redirects[state]?.let { redirect ->
                        call.respondRedirect(redirect)
                        return@get
                    }
                }
            }
            call.respondRedirect("/home")
        }
    }
}