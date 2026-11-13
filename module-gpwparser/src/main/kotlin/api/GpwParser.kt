package pl.slaszu.gpwparser.api

import kotlinx.datetime.LocalDate

interface GpwParser {
    fun getDataForDate(date: LocalDate): List<StockPriceDTO>
}