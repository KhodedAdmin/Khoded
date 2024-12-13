package com.probro.khoded.model.local.datatables

import BASE_VARCHAR_LENGTH
import Project
import Projects
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.UUID


object Teams : UUIDTable("Teams") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val focus = varchar("Focus", BASE_VARCHAR_LENGTH)
    val timezone = varchar("Timezone", BASE_VARCHAR_LENGTH)
    val language = varchar("Language", BASE_VARCHAR_LENGTH)
    val maxMembers = integer("MaxMembers")
    val project = optReference("Project", Projects)
}

class Team(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Team>(Teams)

    var name by Teams.name
    var focus by Teams.focus
    var timeZone by Teams.timezone
    var language by Teams.language
    val maxMembers by Teams.maxMembers
    var project by Project optionalReferencedOn Teams.project
}

object TeamMembers : Table() {
    val team = reference("Team", Teams)
    val member = reference("Member", KhodedUsers)
}