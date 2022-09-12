package com.itomise.com.itomise.infrastructure.utils

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/*
 * Base class for entities with string id
 */
abstract class StringEntityClass<out E : Entity<String>>(table: IdTable<String>, entityType: Class<E>? = null) :
    EntityClass<String, E>(table, entityType)

/*
 * Base class for table objects with string id
 */
open class StringIdTable(name: String = "", columnName: String = "id", columnLength: Int = 10) : IdTable<String>(name) {
    override val id: Column<EntityID<String>> = varchar(columnName, columnLength).entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}
