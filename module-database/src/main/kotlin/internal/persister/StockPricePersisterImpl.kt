package pl.slaszu.database.internal.persister

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.slaszu.database.api.StockDTO
import pl.slaszu.database.api.StockPriceDTO
import pl.slaszu.database.api.StockPricePersister
import pl.slaszu.database.internal.db.StockPricesTable
import pl.slaszu.database.internal.db.StocksTable
import java.util.*

internal class StockPricePersisterImpl(
    private val database: Database
) : StockPricePersister {
    override fun persist(stockDTO: StockDTO, stockPriceDTO: StockPriceDTO) {

        if (!stockDTO.isValid()) {
            throw IllegalArgumentException("Invalid stock data. Please provide a valid code or name")
        }

        val stockId = findStock(stockDTO) ?: createStock(stockDTO)

        val stockPriceId = findStockPriceId(stockId, stockPriceDTO.date)

        if (stockPriceId != null) {
            transaction(database) {
                StockPricesTable.update(
                    where = { StockPricesTable.id eq stockPriceId }
                ) { row ->
                    row[StockPricesTable.price] = stockPriceDTO.price
                    row[StockPricesTable.priceOpen] = stockPriceDTO.priceOpen
                    row[StockPricesTable.priceHigh] = stockPriceDTO.priceHigh
                    row[StockPricesTable.priceLow] = stockPriceDTO.priceLow
                    row[StockPricesTable.volume] = stockPriceDTO.volume
                    row[StockPricesTable.updatedAt] = stockPriceDTO.updatedAt
                }
            }
            return
        } else {
            transaction(database) {
                StockPricesTable.insert { row ->
                    row[StockPricesTable.stockId] = stockId
                    row[StockPricesTable.price] = stockPriceDTO.price
                    row[StockPricesTable.priceOpen] = stockPriceDTO.priceOpen
                    row[StockPricesTable.priceHigh] = stockPriceDTO.priceHigh
                    row[StockPricesTable.priceLow] = stockPriceDTO.priceLow
                    row[StockPricesTable.volume] = stockPriceDTO.volume
                    row[StockPricesTable.updatedAt] = stockPriceDTO.updatedAt
                    row[StockPricesTable.date] = stockPriceDTO.date
                }
            }
        }
    }

    private fun findStockPriceId(stockId: UUID, date: LocalDate): UUID? {
        return transaction(database) {
            StockPricesTable
                .select(
                    StockPricesTable.id
                )
                .where {
                    (StockPricesTable.stockId eq stockId) and (StockPricesTable.date eq date)
                }
                .limit(1)
                .firstOrNull()?.get(StockPricesTable.id)?.value
        }
    }

    private fun createStock(stockDTO: StockDTO): UUID {
        return transaction(database) {
            StocksTable.insertAndGetId { row ->
                row[StocksTable.code] = stockDTO.code.takeIf { it.isNullOrBlank().not() }
                row[StocksTable.name] = stockDTO.name.takeIf { it.isNullOrBlank().not() }
            }.value
        }
    }

    private fun findStock(stockDTO: StockDTO): UUID? {
        return transaction(database) {
            val stockQuery = StocksTable
                .select(
                    StocksTable.id
                )
                .limit(1);

            if (stockDTO.code.isNullOrBlank().not()) {
                stockQuery.andWhere {
                    StocksTable.code eq stockDTO.code
                }
            } else if (stockDTO.name.isNullOrBlank().not()) {
                stockQuery.andWhere {
                    StocksTable.name eq stockDTO.name
                }
            } else {
                throw IllegalArgumentException("Invalid stock data. Please provide a valid code or name")
            }

            stockQuery.firstOrNull()?.get(StocksTable.id)?.value
        }
    }
}