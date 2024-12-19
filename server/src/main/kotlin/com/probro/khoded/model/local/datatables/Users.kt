package com.probro.khoded.model.local.datatables

import BASE_VARCHAR_LENGTH
import DECIMAL_PRECISION
import DECIMAL_SCALE
import MAX_VARCHAR_LENGTH
import Project
import Projects
import com.probro.khoded.model.local.dto.UserDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.money.compositeMoney
import org.jetbrains.exposed.sql.money.currency
import java.util.UUID

object Customers : UUIDTable("Customers") {
    val budget = compositeMoney(
        amountColumn = decimal("Amount", DECIMAL_PRECISION, DECIMAL_SCALE),
        currencyColumn = currency("Currency")
    )
    val fieldOfWork = varchar("FieldOfWork", BASE_VARCHAR_LENGTH)
}

class Customer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Customer>(Customers)

    var budget by Customers.budget
    var fieldOfWork by Customers.fieldOfWork
}

object Employees : UUIDTable("Employees") {
    val employeeNumber = varchar("EmpNumber", BASE_VARCHAR_LENGTH).uniqueIndex()
    val role = varchar("Role", BASE_VARCHAR_LENGTH)
}

class Employee(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Employee>(Employees)
    var employeeNumber by Employees.employeeNumber
    var role by Employees.role
}

//User Table
object KhodedUsers : UUIDTable("Users") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val userName = varchar("username", BASE_VARCHAR_LENGTH).uniqueIndex()
    val token = varchar("Token", MAX_VARCHAR_LENGTH).uniqueIndex().nullable()
    val email = varchar("Email", BASE_VARCHAR_LENGTH).uniqueIndex()
    val phone = varchar("Phone", 10)
    val password = varchar("Password", BASE_VARCHAR_LENGTH)
    val profilePic = varchar("profilePic", MAX_VARCHAR_LENGTH)
    val createdAt = timestamp("CreatedAt")
    val role = varchar("Role", BASE_VARCHAR_LENGTH)
}

object KhodedCustomerEmployee : Table() {
    val user = reference("user", KhodedUsers)
    val customer = reference("customer", Customers).nullable()
    val employee = reference("employee", Employees).nullable()
    override val primaryKey: PrimaryKey
        get() = PrimaryKey(user, customer, employee, name = "KhodedID")
}


class User(id: EntityID<UUID>) : UUIDEntity(id) {
    fun toDTO(): UserDTO {
        return UserDTO(
            id = id.value.toString(),
            name = name,
            email = email,
            phone = phone,
            token = token,
            role = role
        )
    }

    companion object : UUIDEntityClass<User>(KhodedUsers)

    var name by KhodedUsers.name
    var userName by KhodedUsers.userName
    var role by KhodedUsers.role
    var email by KhodedUsers.email
    var phone by KhodedUsers.phone
    var createdAt by KhodedUsers.createdAt
    var token by KhodedUsers.token
    var password by KhodedUsers.password
    var profilePic by KhodedUsers.profilePic

    val customerDetails by Customer via KhodedCustomerEmployee
    val employeeDetails by Employee via KhodedCustomerEmployee
    val projects by Project backReferencedOn Projects.customer
}
