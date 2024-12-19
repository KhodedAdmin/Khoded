package com.probro.khoded.model.repositories

import Project
import com.probro.khoded.model.local.datasources.ProjectDataSource
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.ProjectDTO

object ProjectRepository {
    private val projectDataSource by lazy {
        ProjectDataSource()
    }

    suspend fun getProjectByID(id: String) = projectDataSource.getProjectByID(id)

    suspend fun getProjectsForUserID(userID: String): List<Project> {
        val user = UserRepository.currentUser.value
        return if (user != null && user.id.value.toString() == userID) {
            getProjectsForUser(user)
        } else {
            getProjectsForUser(
                UserRepository.findUserByID(userID)
            )
        }
    }

    suspend fun createProjectForUser(userID: String, project: ProjectDTO): Project {
        val currentUser = UserRepository.currentUser.value
        return if (currentUser != null && currentUser.id.value.toString() == userID) {
            createProjectForUser(currentUser, project)
        } else {
            createProjectForUser(
                UserRepository.findUserByID(userID),
                project
            )
        }
    }

    suspend fun deleteProject(projectID: String): Boolean? {
        val project = getProjectByID(projectID)
        return if (project != null) {
            deleteProject(project)
            getProjectByID(projectID) == null
        } else {
            null
        }
    }

    suspend fun updateProject(projectUpdates: ProjectDTO): Project? =
        projectDataSource.updateProject(projectUpdates)

    private suspend fun getProjectsForUser(user: User): List<Project> =
        projectDataSource.getProjectsForUser(user)

    private suspend fun createProjectForUser(user: User, projectDTO: ProjectDTO): Project =
        projectDataSource.createProjectForUser(user, projectDTO)

    private suspend fun deleteProject(project: Project) =
        projectDataSource.deleteProject(project)


}