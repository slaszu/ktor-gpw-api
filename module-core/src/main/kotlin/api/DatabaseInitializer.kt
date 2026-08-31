package pl.slaszu.core.api

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

import pl.slaszu.core.internal.db.StockPricesTable
import pl.slaszu.core.internal.db.StocksTable
import java.util.UUID
import kotlin.random.Random

class DatabaseInitializer(
    private val db: Database
) {
    fun init() {
        transaction(db) {
            SchemaUtils.create(StocksTable, StockPricesTable)

            if (StocksTable.selectAll().count() > 0) return@transaction

            val initialStocks = listOf(
                Triple("PKN", "ORLEN", 65.0f),
                Triple("KGH", "KGHM", 120.0f),
                Triple("PZU", "PZU", 42.0f)
            )

            val nowInstant = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()

            initialStocks.forEach { (stockCode, stockName, basePrice) ->
                val stockUuid = UUID.randomUUID()

                StocksTable.insert {
                    it[id] = stockUuid
                    it[code] = stockCode
                    it[name] = stockName
                }

                var currentPrice = basePrice

                for (dayOffset in 30 downTo 1) {
                    val recordDate = nowInstant
                        .minus(dayOffset, DateTimeUnit.DAY, timeZone)
                        .toLocalDateTime(timeZone)

                    val priceChangePercent = Random.nextDouble(-0.03, 0.03).toFloat()
                    val priceOpenVal = currentPrice
                    val priceCloseVal = (priceOpenVal * (1f + priceChangePercent)).coerceAtLeast(1.0f)

                    val priceHighVal =
                        maxOf(priceOpenVal, priceCloseVal) * (1f + Random.nextDouble(0.001, 0.015).toFloat())
                    val priceLowVal =
                        minOf(priceOpenVal, priceCloseVal) * (1f - Random.nextDouble(0.001, 0.015).toFloat())
                    val dailyVolume = Random.nextInt(10_000, 500_000)

                    StockPricesTable.insert {
                        it[id] = UUID.randomUUID()
                        it[stockId] = stockUuid
                        it[price] = priceCloseVal
                        it[priceOpen] = priceOpenVal
                        it[priceHigh] = priceHighVal
                        it[priceLow] = priceLowVal
                        it[volume] = dailyVolume
                        it[datetime] = recordDate
                    }

                    currentPrice = priceCloseVal
                }
            }
        }
    }
}