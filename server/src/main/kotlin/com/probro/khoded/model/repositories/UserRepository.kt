package com.probro.khoded.model.repositories

import com.probro.khoded.model.local.KhodedDB
import com.probro.khoded.model.local.datatables.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    suspend fun createUser(
        name: String,
        phone: String,
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        KhodedDB.createNewUser(name, phone, email, password)
    }

    suspend fun updateUserInformation(user: User) {
        KhodedDB.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        KhodedDB.deleteUser(user)
    }

}