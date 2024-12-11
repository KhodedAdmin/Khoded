package com.probro.khoded.model.remote

import com.google.api.client.auth.oauth2.Credential
import com.probro.khoded.model.remote.auth.AuthManager
import com.probro.khoded.model.remote.auth.TokenType
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import model.serialization.json

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json
    }
    install(DefaultRequest) {
        header("Accept", "application/json")
    }
    install(Auth) {
        bearer {
            loadTokens {
                val tokens: Credential = AuthManager.getTokens()
                BearerTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken
                )
            }
            refreshTokens {
                val tokens: Credential = AuthManager.refreshTokens()
                BearerTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken
                )
            }
        }
    }
}


