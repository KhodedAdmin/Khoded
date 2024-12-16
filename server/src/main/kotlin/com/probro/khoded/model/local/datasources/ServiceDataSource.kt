package com.probro.khoded.model.local.datasources

import com.probro.khoded.model.local.KhodedLocalDataSource
import com.probro.khoded.model.local.datatables.Service
import com.probro.khoded.model.local.datatables.Services
import com.probro.khoded.model.local.dto.ServiceDTO
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class ServiceDataSource : KhodedLocalDataSource() {

    suspend fun getAllServices() = newSuspendedTransaction(db = db) {
        Services.selectAll().map { Service.wrapRow(it) }
    }

    suspend fun createNewService(service: ServiceDTO) = newSuspendedTransaction(db = db) {
        Service.new(UUID.randomUUID()) {
            name = service.name
            description = service.description
            duration = service.duration
            graphic = service.graphic
            price = service.price.amount.toBigDecimal()
            currency = service.price.currency
        }
    }

    suspend fun getServiceById(id: String) = newSuspendedTransaction(db = db) {
        Services.selectAll().where { Services.id eq UUID.fromString(id) }
            .firstOrNull()
            ?.let { Service.wrapRow(it) }
    }

    suspend fun updateServiceDetails(service: ServiceDTO, id: String) =
        newSuspendedTransaction(db = db) {
            Service.findByIdAndUpdate(id = UUID.fromString(id)) {
                it.name = service.name
                it.description = service.description
                it.duration = service.duration
                it.graphic = service.graphic
                it.price = service.price.amount.toBigDecimal()
                it.currency = service.price.currency
            }
        }

    suspend fun removeService(service: Service) {
        Service.removeFromCache(service)
    }
}


