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

            val qty = call.request.queryParameters["qty"] ?: "90"
            var code = call.parameters["code"] ?: throw IllegalArgumentException("Invalid Code")
            call.respond(stockPriceRepository.getLatest(code, qty.toInt()))
        }
    }
}