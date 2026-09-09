package pl.slaszu.core.api

import kotlinx.datetime.LocalDate

interface StockPriceRepository {
    fun getLatest(stockCode: String, qty: Int): List<StockPriceDTO>
    fun getFromDate(stockCode: String, dateFrom: LocalDate, qty: Int): List<StockPriceDTO>
    fun getToDate(stockCode: String, dateTo: LocalDate, qty: Int): List<StockPriceDTO>
}