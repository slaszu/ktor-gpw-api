package pl.slaszu.core.internal.repository

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.slaszu.core.api.StockDTO
import pl.slaszu.core.api.StockRepository
import pl.slaszu.core.internal.db.StocksTable

internal class ExposedStockRepository(
    private val db: Database
) : StockRepository {

    override fun getAll(): List<StockDTO> = transaction(db) {
        StocksTable.selectAll().map { row ->
            StockDTO(
                code = row[StocksTable.code],
                name = row[StocksTable.name]
            )
        }
    }
}