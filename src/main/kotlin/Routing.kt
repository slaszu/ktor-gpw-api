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
            call.respondText("Hello, World!")
        }
        get("/stocks") {
            call.respond(stockRepository.getAll())
        }
        get("/stock_prices") {
            call.respond(stockPriceRepository.getLatest("xxx", 5))
        }
    }
}