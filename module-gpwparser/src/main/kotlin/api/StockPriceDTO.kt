package pl.slaszu.gpwparser.api

import kotlinx.datetime.LocalDate

data class StockPriceDTO(
    val stockCode: String,
    val stockName: String,
    val priceOpen: Float,
    val priceHigh: Float,
    val priceLow: Float,
    val price: Float,
    val volume: Int,
    val amount: Int,
    val date: LocalDate,
)