package com.probro.khoded.model.local.datasources

import Project
import com.probro.khoded.model.local.KhodedLocalDataSource
import com.probro.khoded.model.local.datatables.User
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ProjectDataSource : KhodedLocalDataSource() {

    suspend fun getProjectsForUser(user: User): List<Project> = newSuspendedTransaction(db = db) {
        Projects.selectAll().where { Projects.customer eq user.id }
            .distinct()
            .map { Project.wrapRow(it) }
    }

    suspend fun createProjectForUser(
        user: User,
        name: String,
        description: String
    ) = newSuspendedTransaction(db = db) {
        Project.new {
            this.name = name
            this.description = description
            this.customer = user
            this.createdAt = Clock.System.now()
        }
    }

    suspend fun updateProject(project: Project) = newSuspendedTransaction(db = db) {
        Project.findByIdAndUpdate(project.id.value) { oldProj ->
            oldProj.name = project.name
            oldProj.description = project.description
        }
    }

    suspend fun deleteProject(project: Project) = newSuspendedTransaction(db = db) {
        Project.removeFromCache(project)
    }

}