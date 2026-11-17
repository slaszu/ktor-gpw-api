package pl.slaszu.database.api

interface StockPricePersister {
    fun persist(stockDTO: StockDTO, stockPriceDTO: StockPriceDTO)
}