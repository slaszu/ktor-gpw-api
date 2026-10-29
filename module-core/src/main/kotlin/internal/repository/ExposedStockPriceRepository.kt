package pl.slaszu.core.internal.repository

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.andWhere
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
                StockPricesTable.date to SortOrder.DESC,
            )
            .limit(qty).map { row ->
                map(row)
            }
    }

    override fun getFromDate(
        stockCode: String,
        dateFrom: LocalDate,
        qty: Int
    ): List<StockPriceDTO> = transaction(db) {
        StockPricesTable.innerJoin(StocksTable)
            .selectAll()
            .where {
                StockPricesTable.date greaterEq dateFrom
            }
            .andWhere {
                StocksTable.code eq stockCode
            }
            .orderBy(
                StockPricesTable.date to SortOrder.ASC,
            )
            .limit(qty).map { row ->
                map(row)
            }
    }

    override fun getToDate(
        stockCode: String,
        dateTo: LocalDate,
        qty: Int
    ): List<StockPriceDTO> = transaction(db) {
        StockPricesTable.innerJoin(StocksTable)
            .selectAll()
            .where {
                StockPricesTable.date lessEq dateTo
            }
            .andWhere {
                StocksTable.code eq stockCode
            }
            .orderBy(
                StockPricesTable.date to SortOrder.DESC,
            )
            .limit(qty).map { row ->
                map(row)
            }
    }

    private fun map(row: ResultRow): StockPriceDTO {
        return StockPriceDTO(
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