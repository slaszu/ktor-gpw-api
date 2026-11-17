package pl.slaszu.database.api

interface StockRepository {
    fun getAll(): List<StockDTO>
}