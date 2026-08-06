package pl.slaszu.core.internal.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

internal object StocksTable : UUIDTable(name = "stock", columnName = "id") {
    // columnName = "id" (lub "uuid" - zależnie czy w bazie nazwałeś klucz 'id' czy 'uuid')
    
    val code = varchar("code", 255).uniqueIndex()
    val name = varchar("name", 255)
}