package com.probro.khoded.model.repositories

import com.probro.khoded.model.local.KhodedDB
import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.UserDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

object UserRepository {

    suspend fun getAllUsers(): List<UserDTO> = newSuspendedTransaction(db = KhodedDB.db) {
        KhodedUsers.selectAll().distinct().map { resultRow ->
            User.wrapRow(resultRow)
                .toDTO()
        }
    }

    suspend fun getUserByID(id: UUID): User = newSuspendedTransaction(db = KhodedDB.db) {
        KhodedUsers.selectAll().where { KhodedUsers.id eq id }
            .first()
            .let {
                User.wrapRow(it)
            }
    }

    suspend fun findUserByName(userName: String): List<User> =
        newSuspendedTransaction(db = KhodedDB.db) {
            KhodedUsers.selectAll().where { KhodedUsers.name eq userName }
                .distinct()
                .map {
                    User.wrapRow(it)
                }
        }

    suspend fun createNewUser(
        newUser: UserDTO
    ) = newSuspendedTransaction(db = KhodedDB.db) {
        User.new {
            this.name = newUser.name.toString()
            this.email = newUser.email.toString()
            this.userName = newUser.name.toString()
            this.phone = newUser.phone.toString()
            this.password = newUser.password.toString()
            this.createdAt = Clock.System.now()
            this.role = newUser.role.toString()
        }
    }

    suspend fun updateUser(user: UserDTO) = newSuspendedTransaction(db = KhodedDB.db) {
        User.findByIdAndUpdate(UUID.fromString(user.id)) { userToUpdate ->
            userToUpdate.apply {
                name = user.name.toString()
                email = user.email.toString()
                phone = user.phone.toString()
                password = user.password.toString()
            }
        }
    }

    suspend fun deleteUser(userID: UUID) = newSuspendedTransaction(db = KhodedDB.db) {
        getUserByID(userID)
            .delete()
    }

}