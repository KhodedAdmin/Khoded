package com.probro.khoded.model.repositories

import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.local.datatables.User
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserRepository {

    suspend fun findUserByName(userName: String): List<ResultRow> {
        return KhodedUsers.selectAll().where { KhodedUsers.name eq userName }
            .distinct()
    }

    suspend fun createNewUser(
        name: String, email: String, phone: String, password: String
    ) = newSuspendedTransaction {
        User.new {
            this.name = name
            this.email = email
            this.phone = phone
            this.password = password
            this.createdAt = Clock.System.now()
        }
    }

    suspend fun updateUser(user: User) = newSuspendedTransaction {
        User.findByIdAndUpdate(user.id.value) { userToUpdate ->
            userToUpdate.apply {
                name = user.name
                email = user.email
                phone = user.phone
                password = user.password
            }
        }
    }

    suspend fun deleteUser(user: User) = newSuspendedTransaction {
        User.removeFromCache(user)
    }

}