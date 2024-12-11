package com.probro.khoded.model.repositories

import Project
import com.probro.khoded.model.local.datasources.ProjectDataSource
import com.probro.khoded.model.local.datatables.User

object ProjectRepository {
    private val projectDataSource by lazy {
        ProjectDataSource()
    }

    suspend fun getProjectsForUserID(userID: String): List<Project> {
        val user = UserRepository.currentUser.value
        return if (user != null) {
            getProjectsForUser(user)
        } else {
            getProjectsForUser(
                UserRepository.findUserByID(userID)
            )
        }
    }

    private suspend fun getProjectsForUser(user: User): List<Project> {
        return projectDataSource.getProjectsForUser(user)
    }
}