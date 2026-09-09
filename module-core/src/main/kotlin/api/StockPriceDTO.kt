package pl.slaszu.core.api

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class StockPriceDTO(
    val price: Float,
    val priceOpen: Float,
    val priceHigh: Float,
    val priceLow: Float,
    val volume: Int,
    val updatedAt: LocalDateTime,
    val date: LocalDate,
)