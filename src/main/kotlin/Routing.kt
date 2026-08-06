package pl.slaszu

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import pl.slaszu.core.api.StockRepository

fun Application.configureRouting() {

    val stockRepository by inject<StockRepository>()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(stockRepository.getAll())
        }
    }
}