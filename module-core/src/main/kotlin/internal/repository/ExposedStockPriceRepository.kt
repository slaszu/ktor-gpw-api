package pl.slaszu.core.internal.repository

import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.slaszu.core.api.StockPriceDTO
import pl.slaszu.core.api.StockPriceRepository
import pl.slaszu.core.internal.db.StockPricesTable
import pl.slaszu.core.internal.db.StocksTable

class ExposedStockPriceRepository(
    private val db: Database
) : StockPriceRepository {
    override fun getLatest(
        stockCode: String,
        qty: Int
    ): List<StockPriceDTO> = transaction(db) {
        StockPricesTable.innerJoin(StocksTable)
            .selectAll()
            .where {
                StocksTable.code eq stockCode
            }
            .orderBy(
                StockPricesTable.updatedAt to SortOrder.DESC,
            )
            .limit(qty).map { row ->
                StockPriceDTO(
                    price = row[StockPricesTable.price],
                    priceOpen = row[StockPricesTable.priceOpen],
                    priceHigh = row[StockPricesTable.priceHigh],
                    priceLow = row[StockPricesTable.priceLow],
                    volume = row[StockPricesTable.volume],
                    updatedAt = row[StockPricesTable.updatedAt],
                    date = row[StockPricesTable.date]
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