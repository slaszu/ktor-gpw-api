package pl.slaszu.core.internal.db

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime

internal object StockPricesTable : UUIDTable(name = "stock_price", columnName = "id") {

    val stockId = reference("stock_id", StocksTable)

    val price = float("price")

    val priceOpen = float("price_open")
    val priceHigh = float("price_high")
    val priceLow = float("price_low")

    val volume = integer("volume")

    val updatedAt = datetime("updated_at")

    val date = date("date")
}