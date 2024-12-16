package com.probro.khoded.model.repositories

import com.probro.khoded.model.local.datasources.ServiceDataSource
import com.probro.khoded.model.local.dto.ServiceDTO

object ServiceRepository {
    private val serviceDataSource by lazy {
        ServiceDataSource()
    }

    suspend fun createNewService(service: ServiceDTO): ServiceDTO {
        return serviceDataSource.createNewService(service)
            .toDTO()
    }

    suspend fun getAllServices() = serviceDataSource.getAllServices()
        .map { it.toDTO() }

    suspend fun getServiceByID(id: String) = serviceDataSource.getServiceById(id)

    suspend fun updateServiceByID(service: ServiceDTO, serviceID: String) =
        serviceDataSource.updateServiceDetails(service, serviceID)

    suspend fun deleteService(serviceID: String): Boolean? {
        val serviceToDelete = serviceDataSource.getServiceById(serviceID)

        serviceToDelete?.let {
            serviceDataSource.removeService(it)
        } ?: return null
        return serviceDataSource.getServiceById(serviceID) == null
    }

}