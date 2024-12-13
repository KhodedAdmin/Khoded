import com.probro.khoded.model.local.datatables.Consultation
import com.probro.khoded.model.local.datatables.Consultations
import com.probro.khoded.model.local.datatables.Customers
import com.probro.khoded.model.local.datatables.Employee.Companion.backReferencedOn
import com.probro.khoded.model.local.datatables.Employee.Companion.optionalBackReferencedOn
import com.probro.khoded.model.local.datatables.Employee.Companion.referrersOn
import com.probro.khoded.model.local.datatables.Employees
import com.probro.khoded.model.local.datatables.KhodedCustomerEmployee.nullable
import com.probro.khoded.model.local.datatables.KhodedUsers
import com.probro.khoded.model.local.datatables.Team
import com.probro.khoded.model.local.datatables.Teams
import com.probro.khoded.model.local.datatables.User
import com.probro.khoded.model.local.dto.ProjectDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.time
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object Projects : UUIDTable("Projects") {
    val name = varchar("Name", BASE_VARCHAR_LENGTH)
    val description = varchar("Description", MAX_VARCHAR_LENGTH)
    val consultations = optReference("Consultations", Consultations)
    val customer = reference("Customers", KhodedUsers)
    val createdAt = timestamp("CreatedAt")
}

class Project(id: EntityID<UUID>) : UUIDEntity(id) {
    fun toDTO(): ProjectDTO {
        return ProjectDTO(
            name = name,
            description = description,
            customer = customer.name,
            consultationID = consultations?.id?.value.toString()
        )
    }

    companion object : UUIDEntityClass<Project>(Projects)

    var name by Projects.name
    var description by Projects.description
    var createdAt by Projects.createdAt
    val consultations by Consultation optionalReferencedOn Projects.consultations
    var customer by User referencedOn Projects.customer
    val team by Team backReferencedOn Teams.project
}
