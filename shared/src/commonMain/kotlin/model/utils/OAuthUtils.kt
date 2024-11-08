package model.utils

import com.probro.khoded.KhodedConfig


object OAuthUtils {
    val clientID = KhodedConfig.AuthClientIDTest
    val clientSecret = KhodedConfig.AuthClientSecretTest
    val redirects: MutableMap<String, String> = mutableMapOf()
}