package com.probro.khoded.model.local.datatables

import BASE_VARCHAR_LENGTH
import DECIMAL_PRECISION
import DECIMAL_SCALE
import MAX_VARCHAR_LENGTH
import com.probro.khoded.model.local.dto.Price
import com.probro.khoded.model.local.dto.ServiceDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CompositeColumn
import org.jetbrains.exposed.sql.money.CompositeMoneyColumn
import org.jetbrains.exposed.sql.money.compositeMoney
import org.jetbrains.exposed.sql.money.currency
import java.math.BigDecimal
import java.util.UUID
import javax.money.CurrencyUnit
import javax.money.MonetaryAmount

object Services : UUIDTable("Services") {
    var name = varchar("Name", BASE_VARCHAR_LENGTH)
    var description = varchar("Description", MAX_VARCHAR_LENGTH)
    var duration = varchar("Duration", BASE_VARCHAR_LENGTH)
    var price = decimal("Amount", DECIMAL_PRECISION, DECIMAL_SCALE)
    var currency = varchar("CurrencyType", BASE_VARCHAR_LENGTH)
    var graphic = varchar("Graphic", MAX_VARCHAR_LENGTH)
}

class Service(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Service>(Services)

    var name by Services.name
    var description by Services.description
    var duration by Services.duration
    var price by Services.price
    var currency by Services.currency
    var graphic by Services.graphic

    fun toDTO(): ServiceDTO {
        return ServiceDTO(
            id = id.value.toString(),
            name = name,
            description = description,
            duration = duration,
            price = Price(price.toDouble(), currency),
            graphic = graphic
        )
    }
}