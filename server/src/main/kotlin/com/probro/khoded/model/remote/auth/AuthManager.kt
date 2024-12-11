package com.probro.khoded.model.remote.auth

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.auth.Credentials
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.GoogleCredentials
import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.remote.applicationHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.server.auth.OAuthAccessTokenResponse
import kotlinx.io.IOException
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.sql.Date
import java.time.Instant

object AuthManager {

    val SCOPES: List<String> = listOf(
        "https://www.googleapis.com/auth/userinfo.profile",
        "https://www.googleapis.com/auth/userinfo.email",
        "openid"
    )

    private const val CREDENTIALS_FILE_PATH =
        "server/server_credentials.json" //path to the processed resources file.

    private const val TOKENS_DIRECTORY_PATH = "tokens"

    private val transport = GoogleNetHttpTransport.newTrustedTransport()
    private val JSON_FACTORY = GsonFactory.getDefaultInstance()
    private val fileDataStoreFactory = FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH))
    private lateinit var credential: Credential


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    fun getTokens(httpTransport: NetHttpTransport = transport): Credential {
        // Load client secrets.
        println("Getting credentials.")

        val credentialsFile = File(CREDENTIALS_FILE_PATH)

        println("Got the file created. $credentialsFile")
        val clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(credentialsFile.inputStream()))

        println("Got the client secrets. $clientSecrets")
        println(
            "Building flow with variables httpTransport:$httpTransport,\n" +
                    "jsonFactor:$JSON_FACTORY,\nclientSecrets:$clientSecrets,\nand scopes:$SCOPES"
        )
        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow
            .Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(fileDataStoreFactory)
            .setAccessType("offline")
            .build()
        println("Built the flow object $flow")

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        println("Built the receiver object $receiver")
        credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("me")
        println("Built the credential object $credential")
        //returns an authorized Credential object.
        return credential
    }


//    fun getCredentialsFromToken(principal: OAuthAccessTokenResponse.OAuth2) {
//        val seconds = Instant.now().epochSecond + principal.expiresIn
//        credentials = GoogleCredentials.newBuilder()
//            .setAccessToken(
//                AccessToken(
//                    principal.accessToken,
//                    Date.from(Instant.ofEpochSecond(seconds))
//                )
//            )
//            .build().apply {
//                refreshIfExpired()
//            }
//    }

    fun refreshTokens(): Credential {
        return if (credential.refreshToken()) {
            credential
        } else {
            getTokens()
        }
    }


    suspend fun getPersonalGreeting(
        httpClient: HttpClient = applicationHttpClient,
        userSession: UserCookie
    ): String = httpClient
        .get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${userSession.userToken}")
            }
        }.bodyAsText()

}

enum class TokenType(val value: String) {
    UserToken("user_token"), RefreshToken("refresh_token")
}