package pl.slaszu.service

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.slf4j.Logger
import pl.slaszu.database.api.StockPricePersister
import pl.slaszu.getStockDTOForDB
import pl.slaszu.getStockPriceDTOForDB
import pl.slaszu.gpwparser.api.FreeDayException
import pl.slaszu.gpwparser.api.GpwParser
import kotlin.math.log

class ImportService(
    private val gpwParser: GpwParser,
    private val stockPricePersister: StockPricePersister,
    private val logger: Logger
) {
    fun importData(dateFor: LocalDate) {
        logger.info(">>> Importing data for: $dateFor")
        gpwParser.getDataForDate(dateFor).forEach {
            val stockDTO = it.getStockDTOForDB()
            val stockPriceDTO = it.getStockPriceDTOForDB()
            logger.info(">>> Stock: $stockDTO")
            logger.info(">>> Stock price: $stockPriceDTO")
            if (!stockDTO.isValid()) {
                logger.warn(">>> StockDTO is not valid: $stockDTO")
                return@forEach
            }
            stockPricePersister.persist(stockDTO, stockPriceDTO)
        }
    }

    fun importData(dateFrom: LocalDate, dateTo: LocalDate) {
        if (dateFrom > dateTo) return

        var current = dateFrom
        while (current <= dateTo) {
            try {
                importData(current)
            } catch (e: FreeDayException) {
                logger.error(">>> Free day: $current")
            }
            current = current.plus(DatePeriod(days = 1))
        }
    }
}