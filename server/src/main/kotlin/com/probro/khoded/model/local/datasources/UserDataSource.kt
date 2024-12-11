package com.probro.khoded.model.local.datasources

import com.probro.khoded.model.local.KhodedLocalDataSource
import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.Role
import com.probro.khoded.model.local.dto.UserDTO
import com.probro.khoded.model.remote.auth.GoogleUserInfo
import com.probro.khoded.utils.PasswordGenerator
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UserDataSource : KhodedLocalDataSource() {

    suspend fun getAllUsers(): List<UserDTO> =
        newSuspendedTransaction(db = db) {
            KhodedUsers.selectAll().distinct().map { resultRow ->
                User.wrapRow(resultRow)
                    .toDTO()
            }
        }

    suspend fun findUserByID(id: UUID): User =
        newSuspendedTransaction(db = db) {
            KhodedUsers.selectAll().where { KhodedUsers.id eq id }
                .first()
                .let {
                    User.wrapRow(it)
                }
        }

    suspend fun findUserByUserName(username: String): List<User> =
        newSuspendedTransaction(db = db) {
            KhodedUsers.selectAll().where { KhodedUsers.userName eq username }
                .map {
                    User.wrapRow(it)
                }
        }

    suspend fun findUserByEmail(email: String): List<User> =
        newSuspendedTransaction(db = db) {
            KhodedUsers.selectAll().where { KhodedUsers.email eq email }
                .distinct()
                .map {
                    User.wrapRow(it)
                }
        }

    suspend fun createNewUser(
        newUser: UserDTO
    ) = newSuspendedTransaction(db = db) {
        User.new {
            this.name = newUser.name.toString()
            this.email = newUser.email.toString()
            this.userName = newUser.name.toString()
            this.phone = newUser.phone.toString()
            this.createdAt = Clock.System.now()
            this.role = newUser.role.toString()
        }
    }

    suspend fun updateUser(user: UserDTO) = newSuspendedTransaction(db = db) {
        User
            .findByIdAndUpdate(UUID.fromString(user.id)) { userToUpdate ->
                userToUpdate.apply {
                    name = user.name.toString()
                    email = user.email.toString()
                    phone = user.phone.toString()
                }
            }
    }

    suspend fun deleteUser(userID: UUID) = newSuspendedTransaction(db = db) {
        findUserByID(userID)
            .delete()
    }

    suspend fun createUserFromGoogleInfo(user: GoogleUserInfo, accessToken: String) =
        newSuspendedTransaction(db = db) {
            println("creating new User from user $user")
            User.new {
                name = user.name
                email = user.email
                profilePic = user.picture
                userName = ""
                phone = ""
                password = PasswordGenerator.getRandomPassword()
                createdAt = Clock.System.now()
                role = Role.GUEST.value
                token = accessToken
            }
        }
}

