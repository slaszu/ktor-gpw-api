package pl.slaszu.core.internal.repository

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.slaszu.core.api.StockPriceDTO
import pl.slaszu.core.api.StockPriceRepository
import pl.slaszu.core.internal.db.StockPricesTable

class ExposedStockPriceRepository(
    private val db: Database
) : StockPriceRepository {
    override fun getLatest(
        stockCode: String,
        qty: Int
    ): List<StockPriceDTO> = transaction(db) {
        StockPricesTable.selectAll().limit(qty).map { row ->
            StockPriceDTO(
                price = row[StockPricesTable.price].toDouble(),
            )
        }
    }

    override fun getRange(
        stockCode: String,
        dateFrom: String,
        dateTo: String
    ): List<StockPriceDTO> {
        TODO("Not yet implemented")
    }

    override fun getFromDate(
        stockCode: String,
        dateFrom: String,
        qty: Int
    ): List<StockPriceDTO> {
        TODO("Not yet implemented")
    }

    override fun getToDate(
        stockCode: String,
        dateTo: String,
        qty: Int
    ): List<StockPriceDTO> {
        TODO("Not yet implemented")
    }
}