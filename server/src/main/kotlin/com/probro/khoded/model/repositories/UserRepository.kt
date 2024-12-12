package com.probro.khoded.model.repositories

import com.probro.khoded.configurations.UserCookie
import com.probro.khoded.model.local.datasources.UserDataSource
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.remote.auth.AuthManager
import com.probro.khoded.model.remote.auth.GoogleUserInfo
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.server.auth.OAuthAccessTokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import model.serialization.json
import java.util.UUID

object UserRepository {
    private val userDataSource by lazy {
        UserDataSource()
    }

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> get() = _currentUser.asStateFlow()

    suspend fun getUserByUserName(username: String) = userDataSource.findUserByUserName(username)

    fun getUserForToken(
        principal: OAuthAccessTokenResponse.OAuth2,
        cookie: UserCookie
    ): User? = runBlocking {
        println("The received principal is $principal")
        val userJson = AuthManager.getPersonalGreeting(userSession = cookie)
        return@runBlocking try {
            val user = json.decodeFromString<GoogleUserInfo>(userJson)
            val dto = getLocalUserByEmail(user)
                ?.apply {
                    println("found user $this")
                    updateLocalUser(this, principal.accessToken, user)
                } ?: run {
                println("Couldnt find user in Database, creating new local user.")
                userDataSource.createUserFromGoogleInfo(user, principal.accessToken)
            }
            println("Returning dto $dto")
            dto
        } catch (ex: NoTransformationFoundException) {
            println(ex.printStackTrace())
            null
        } catch (ex: Exception) {
            println(ex.printStackTrace())
            null
        }
    }

    private suspend fun getLocalUserByEmail(
        user: GoogleUserInfo
    ) = with(userDataSource) {
        findUserByEmail(user.email)
            .firstOrNull()
    }

    private suspend fun updateLocalUser(
        user: User,
        accessToken: String,
        googleInfo: GoogleUserInfo
    ) = with(user) {
        println("updating user $this")
        userDataSource.updateUser(
            this.toDTO().copy(
                profilePic = profilePic.ifEmpty { googleInfo.picture }.toString(),
                name = name.ifEmpty { googleInfo.name }.toString(),
                token = accessToken  //PROBABLY STARTED BECAUSE TOKEN WAS EXPIRED
            )
        )
        _currentUser.value = this
    }

    suspend fun addTokenToUserProfile(email: String, session: UserCookie): UserDTO? {
        try {
            val updatedUser = with(userDataSource) {
                findUserByEmail(email).firstOrNull()?.let { user ->
                    updateUser(
                        user.toDTO().also { it.token = session.userToken }
                    )
                }
            }
            return updatedUser?.toDTO()
        } catch (ex: Exception) {
            println(ex.localizedMessage)
            throw ex
        }
    }

    suspend fun deleteUser(uuID: UUID) = userDataSource.deleteUser(uuID)
    suspend fun getAllUsers(): List<UserDTO> = userDataSource
        .getAllUsers()

    suspend fun updateUser(user: UserDTO) = userDataSource.updateUser(user)
    suspend fun createNewUser(userDTO: UserDTO) = userDataSource.createNewUser(userDTO)
    suspend fun findUserByID(userID: String): User =
        userDataSource.findUserByID(UUID.fromString(userID))

}