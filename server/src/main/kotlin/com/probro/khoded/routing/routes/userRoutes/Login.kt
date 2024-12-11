package com.probro.khoded.routing.routes.userRoutes

import com.probro.khoded.configurations.AuthTypes
import com.probro.khoded.configurations.Cookies
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.sessions.sessions
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import model.serialization.json
import model.utils.OAuthUtils

fun Route.login() {
    authenticate(AuthTypes.OAUTH.name) {
        get<Users.Login> {
            // Redirects to 'authorizeUrl' automatically
        }
        get<Users.Callback> {
            val currentPrincipal: OAuthAccessTokenResponse.OAuth2? =
                call.principal<OAuthAccessTokenResponse.OAuth2>()

            println("Got principle of $currentPrincipal")
            // redirects home if the url is not found before authorization
            currentPrincipal?.let { principal: OAuthAccessTokenResponse.OAuth2 ->
                principal.state?.let { state ->
                    val cookie = UserCookie(
                        oAuthState = state,
                        userToken = principal.accessToken,
                        timestamp = Clock.System.now().toString()
                    )
                    val user = UserRepository.getUserForToken(principal, cookie)
                    if (user != null) {
                        cookie.apply {
                            role = user.role
                            userID = user.id
                        }
                        println("user:$user,\ncookie:$cookie")
                        call.sessions.set(name = Cookies.USER_COOKIE.value, cookie)
                        OAuthUtils.redirects[state]
//                            ?.let { redirect ->
//                            call.respondRedirect(redirect)
//                            return@get
//                        }
                    } else {
                        println("No valid user found")
                        call.respond(
                            HttpStatusCode.ExpectationFailed,
                            "Sorry, unable to get  user for the token."
                        )
                    }
                }
            }
            val currentUser = UserRepository.currentUser.value
            val cookie = call.sessions.get(Cookies.USER_COOKIE.value) as UserCookie
            println("current user is $currentUser\ncookie is $cookie.")
            currentUser?.let { user ->
                if (cookie.userID == user.id) {
                    val userjson = json.encodeToString(user)
                    println("return json of $userjson")
                    call.respond(HttpStatusCode.OK, userjson)
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        "Cannot Access User Data that is not your own."
                    )
                }
            } ?: kotlin.run {
                call.respond(
                    HttpStatusCode.ExpectationFailed,
                    "There seems to be no user currently logged in."
                )
            }
        }
    }
}