package pl.slaszu.module

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import pl.slaszu.database.api.StockPriceRepository
import pl.slaszu.database.api.StockRepository
import pl.slaszu.getIntQueryParam
import pl.slaszu.getLocalDatePathParam

fun Application.configureRouting() {

    val stockRepository by inject<StockRepository>()
    val stockPriceRepository by inject<StockPriceRepository>()

    routing {
        get("/") {
            call.respondText("/swagger")
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