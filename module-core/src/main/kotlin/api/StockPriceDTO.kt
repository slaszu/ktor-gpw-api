package pl.slaszu.core.api

import kotlinx.serialization.Serializable

@Serializable
data class StockPriceDTO(
    val price: Double
)