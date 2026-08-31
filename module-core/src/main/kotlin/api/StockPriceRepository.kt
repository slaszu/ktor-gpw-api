package pl.slaszu.core.api

interface StockPriceRepository {
    fun getLatest(stockCode: String, qty: Int): List<StockPriceDTO>
    fun getRange(stockCode: String, dateFrom: String, dateTo: String): List<StockPriceDTO>
    fun getFromDate(stockCode: String, dateFrom: String, qty: Int): List<StockPriceDTO>
    fun getToDate(stockCode: String, dateTo: String, qty: Int): List<StockPriceDTO>
}