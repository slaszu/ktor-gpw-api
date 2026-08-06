package pl.slaszu.core.internal.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.*

// Zakładam, że klucz główny w SQL nazywa się "id" (default Hibernate)
internal object StockPricesTable : UUIDTable(name = "stock_price", columnName = "id") {

    // Pole 'private Stock stock' w JPA generuje domyślnie 'stock_id'
    val stockId = reference("stock_id", StocksTable)

    val price = float("price") // Pole 'private Float price;'

    // ✅ Poprawione nazwy kolumn (CamelCase, dokładnie jak pola w Javie)
    val priceOpen = float("priceOpen") // Pole 'private Float priceOpen;'
    val priceHigh = float("priceHigh") // Pole 'private Float priceHigh;'
    val priceLow = float("priceLow")   // Pole 'private Float priceLow;'

    val volume = integer("volume") // Pole 'private Integer volume;'

    // W Javie utrwalane było pole 'datetime', pole 'date' było @Transient
    val datetime = datetime("datetime") // Pole 'private Date datetime;'
}