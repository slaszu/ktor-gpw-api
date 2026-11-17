package pl.slaszu

import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pl.slaszu.database.api.StockDTO
import pl.slaszu.database.api.StockPriceDTO
import java.time.format.DateTimeParseException
import kotlin.time.Clock
import pl.slaszu.gpwparser.api.StockPriceDTO as StockPriceDTOGPWParser

// Pobiera parametr i parsuje do LocalDate (domyślny format ISO: yyyy-MM-dd)
fun ApplicationCall.getLocalDatePathParam(name: String): LocalDate {
    val value = parameters[name] ?: throw IllegalArgumentException("Parameter not exists: $name")
    return try {
        LocalDate.parse(value)
    } catch (_: DateTimeParseException) {
        throw IllegalArgumentException("Parameter $name must has format YYYY-MM-DD")
    }
}

// Pobiera query parameter jako Int z domyślną wartością
fun ApplicationCall.getIntQueryParam(name: String, default: Int): Int {
    return request.queryParameters[name]?.toIntOrNull() ?: default
}

fun now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun StockPriceDTOGPWParser.getStockDTOForDB(): StockDTO {
    return StockDTO(
        code = this.stockCode,
        name = this.stockName
    )
}

fun StockPriceDTOGPWParser.getStockPriceDTOForDB(updatedAt: LocalDateTime = now()): StockPriceDTO {
    return StockPriceDTO(
        priceOpen = this.priceOpen,
        priceHigh = this.priceHigh,
        priceLow = this.priceLow,
        price = this.price,
        volume = this.volume,
        date = this.date,
        updatedAt = updatedAt
    )
}