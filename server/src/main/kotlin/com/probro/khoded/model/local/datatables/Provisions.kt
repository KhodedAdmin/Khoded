package com.probro.khoded.model.local.datatables

import BASE_VARCHAR_LENGTH
import DECIMAL_PRECISION
import DECIMAL_SCALE
import MAX_VARCHAR_LENGTH
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.money.compositeMoney
import org.jetbrains.exposed.sql.money.currency
import java.util.UUID

object Provisions : UUIDTable("Provisions") {
    var name = varchar("Name", BASE_VARCHAR_LENGTH)
    var description = varchar("Description", MAX_VARCHAR_LENGTH)
    var duration = varchar("Duration", BASE_VARCHAR_LENGTH)
    var price = compositeMoney(
        amountColumn = decimal("Amount", DECIMAL_PRECISION, DECIMAL_SCALE),
        currencyColumn = currency("CurrencyType")
    )
}

class Provision(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Provision>(Provisions)

    val name by Provisions.name
    val description by Provisions.description
    val duration by Provisions.duration
    val price by Provisions.price
}