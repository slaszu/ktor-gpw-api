package pl.slaszu.core.internal.repository

import pl.slaszu.core.api.StockPriceDTO
import pl.slaszu.core.api.StockPriceRepository

class ExposedStockPriceRepository : StockPriceRepository {
    override fun getLatest(
        stockCode: String,
        qty: Int
    ): List<StockPriceDTO> {
        TODO("Not yet implemented")
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