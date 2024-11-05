package com.probro.khoded.model.local

import Project
import Projects
import com.probro.khoded.model.local.datatables.Consultation
import com.probro.khoded.model.local.datatables.Consultations
import com.probro.khoded.model.local.datatables.Customers
import com.probro.khoded.model.local.datatables.Employees
import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.local.datatables.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import model.utils.PostgresUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

const val IS_PROD: Boolean = false
const val POSTGRES_DRIVER = "org.postgresql.Driver"

class KhodedDB {

    companion object {
        val db by lazy {
            getExposedDB().also {
                setUpSchemaTables()
            }
        }
        private var _db: Database? = null
        private val config by lazy {
            HikariConfig().apply {
                jdbcUrl = if (IS_PROD) PostgresUtils.prodURI else PostgresUtils.devURI
                driverClassName = POSTGRES_DRIVER
                username = if (IS_PROD) PostgresUtils.prodUserName else PostgresUtils.devUserName
                password = if (IS_PROD) PostgresUtils.prodPassword else PostgresUtils.devPassword
                maximumPoolSize = 10
                println(
                    "Uri: ${PostgresUtils.devURI} \nUsername: ${PostgresUtils.devUserName} " +
                            "\nPassword: ${PostgresUtils.devPassword}"
                )
            }
        }

        private fun getExposedDB(): Database {
            return _db ?: synchronized(this) {
                _db ?: run {
                    val datasource = HikariDataSource(config)
                    Database.connect(datasource = datasource)
                }
            }
        }

        private fun setUpSchemaTables() = transaction {
            SchemaUtils.create(Customers)
            SchemaUtils.create(Employees)
            SchemaUtils.create(KhodedUsers)
            SchemaUtils.create(Projects)
            SchemaUtils.create(Consultations)
        }

    }

    private val dataScope =
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        })


    suspend fun createProjectForUser(
        user: User,
        name: String,
        description: String
    ) = newSuspendedTransaction {
        Project.new {
            this.name = name
            this.description = description
            this.customer = user
            this.createdAt = Clock.System.now()
        }
    }

    suspend fun updateProject(project: Project) = newSuspendedTransaction {
        Project.findByIdAndUpdate(project.id.value) { oldProj ->
            oldProj.name = project.name
            oldProj.description = project.description
        }
    }

    suspend fun deleteProject(project: Project) = newSuspendedTransaction {
        Project.removeFromCache(project)
    }

    suspend fun createNewConsultation(
        message: String,
        suggestedTimes: List<LocalDateTime>,
        meetingMedium: String
    ) = newSuspendedTransaction {
        Consultation.new {
            this.message = message
            this.meetingMedium = meetingMedium
            this.suggestedTimes = suggestedTimes.joinToString()
            this.processed = false
        }
    }

    suspend fun updateConsultation(consultation: Consultation) = newSuspendedTransaction {
        Consultation.findByIdAndUpdate(consultation.id.value) { oldJawn ->
            with(oldJawn) {
                message = consultation.message
                meetingTime = consultation.meetingTime
                suggestedTimes = consultation.suggestedTimes
                processed = consultation.processed
                meetingMedium = consultation.meetingMedium
            }
        }
    }

    suspend fun removeConsultation(consultation: Consultation) = newSuspendedTransaction {
        Consultation.removeFromCache(consultation)
    }
}