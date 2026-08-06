package pl.slaszu.core.api

import kotlinx.serialization.Serializable

interface StockRepository {
    fun getAll(): List<StockDTO>
}