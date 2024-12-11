package model.utils

import com.probro.khoded.KhodedConfig


object OAuthUtils {
    val callbackURL: String = "http://localhost:8080/users/callback"
    val clientID = KhodedConfig.AuthClientIDTest
    val clientSecret = KhodedConfig.AuthClientSecretTest
    val redirects: MutableMap<String, String> = mutableMapOf()
}