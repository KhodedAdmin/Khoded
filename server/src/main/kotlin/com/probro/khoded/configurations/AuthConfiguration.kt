package com.probro.khoded.configurations

import com.probro.khoded.model.remote.auth.AuthManager
import com.probro.khoded.model.repositories.UserRepository
import com.probro.khoded.routing.routes.userRoutes.Users
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.auth.oauth
import io.ktor.server.sessions.sessionId
import io.ktor.server.sessions.sessions
import kotlinx.datetime.Clock
import model.utils.OAuthUtils

fun Application.configureAuthentication(httpClient: HttpClient) {
    install(Authentication) {
        basic(AuthTypes.BASE_AUTH.name) {
            skipWhen { call ->
                !call.sessionId.isNullOrEmpty()  //SKIP IF WE ALREADY HAVE A SESSION GOING
            }
            realm = "Access to the '${Users.Edit}' path"
            validate { userPasswordCredential ->

                val result = UserRepository.getUserByUserName(userPasswordCredential.name)
                    .firstOrNull { local ->
                        local.name == userPasswordCredential.name
                                && local.password == userPasswordCredential.password
                    }?.also { user ->
                        sessions.set(
                            name = Cookies.USER_COOKIE.value,
                            value = UserCookie(
                                userID = user.id.value.toString(),
                                userToken = "",
                                role = user.role,
                                timestamp = Clock.System.now().toString()
                            )
                        )
                    }
                result?.userName?.let {
                    UserIdPrincipal(it)
                }
            }
        }
        oauth(AuthTypes.OAUTH.name) {
            urlProvider = { OAuthUtils.callbackURL }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = OAuthUtils.clientID,
                    clientSecret = OAuthUtils.clientSecret,
                    // Basic name, email, token and the ability to associate user w/ profile
                    defaultScopes = AuthManager.SCOPES,
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            OAuthUtils.redirects[state] = it
                        }
                    }
                )
            }
            client = httpClient
        }
    }
}

enum class AuthTypes {
    BASE_AUTH, OAUTH, SESSION_AUTH
}