package pl.slaszu

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import pl.slaszu.core.api.StockPriceRepository
import pl.slaszu.core.api.StockRepository

fun Application.configureRouting() {

    val stockRepository by inject<StockRepository>()
    val stockPriceRepository by inject<StockPriceRepository>()

    routing {
        get("/") {
            call.respondText("/openapi")
        }
        get("/stocks") {
            call.respond(stockRepository.getAll())
        }
        get("/stocks/prices/{code}") {

            val qty = call.getIntQueryParam("qty", 90)
            val code = call.parameters["code"] ?: throw IllegalArgumentException("Invalid Code")
            call.respond(stockPriceRepository.getLatest(code, qty))
        }
        get("/stocks/prices/{code}/from/{dateFrom}") {

            val qty = call.getIntQueryParam("qty", 90)
            val code = call.parameters["code"] ?: throw IllegalArgumentException("Invalid Code")
            val dateFrom = call.getLocalDatePathParam("dateFrom")
            call.respond(stockPriceRepository.getFromDate(code, dateFrom, qty))
        }
        get("/stocks/prices/{code}/to/{dateTo}") {

            val qty = call.getIntQueryParam("qty", 90)
            val code = call.parameters["code"] ?: throw IllegalArgumentException("Invalid Code")
            val dateTo = call.getLocalDatePathParam("dateTo")
            call.respond(stockPriceRepository.getToDate(code, dateTo, qty))
        }
    }
}