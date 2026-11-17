package pl.slaszu.database.internal.persister

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
        val stockId = findStock(stockDTO) ?: createStock(stockDTO)

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

    private fun createStock(stockDTO: StockDTO): UUID {
        return transaction(database) {
            StocksTable.insertAndGetId { row ->
                row[StocksTable.code] = stockDTO.code ?: ""
                row[StocksTable.name] = stockDTO.name ?: ""
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

            if (stockDTO.code != null) {
                stockQuery.andWhere {
                    StocksTable.code eq stockDTO.code
                }
            } else if (stockDTO.name != null) {
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