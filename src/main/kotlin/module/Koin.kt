package pl.slaszu.module

import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory
import pl.slaszu.database.api.StockDTO
import pl.slaszu.database.api.StockPriceDTO
import pl.slaszu.database.api.StockPriceRepository
import pl.slaszu.database.di.coreModule
import pl.slaszu.gpwparser.di.gpwparserModule
import pl.slaszu.now
import pl.slaszu.service.ImportService

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        val dburl = environment.config.property("database.url").getString()
        println(">>> Database: $dburl")
        val gpwToday = environment.config.property("gpw.url-today").getString()
        println(">>> Gpw today url: $gpwToday")
        modules(
            coreModule(
                url = environment.config.property("database.url").getString(),
                user = environment.config.property("database.user").getString(),
                pass = environment.config.property("database.password").getString(),
            ),
            module {
                single { environment.config }
            },
            gpwparserModule(
                gpwToday = environment.config.property("gpw.url-today").getString(),
                gpwArchive = environment.config.property("gpw.url-archive").getString(),
                tmpDir = environment.config.property("gpw.dir-temp").getString(),
            ),
            module {
                single<ImportService> {
                    ImportService(
                        gpwParser = get(),
                        stockPricePersister = get(),
                        logger = LoggerFactory.getLogger(ImportService::class.java)
                    )
                }
            }
        )


//        try {
//            val gpwParser = koin.get<GpwParser>()
//            val data = gpwParser.getDataForDate(now())
//            println(">>> Gpw data")
//            println(data)
//
//        } catch (e: Exception) {
//            println(">>GPWParser `error: ${e.localizedMessage}")
//        }

        println(">>> Warmup db")
        try {
            val stocks = koin.get<StockPriceRepository>()
            val latest = stocks.getLatest("PZU", 1)
            println("Latest price for PZU")
            println(latest)
        } catch (e: Exception) {
            println(">>> DB ERROR: ${e.message}")
        }

        println(">>> Warmup serializer")
        val stockPrice = StockPriceDTO(
            price = 100.0f,
            date = now().date,
            priceOpen = 99.0f,
            priceHigh = 102.0f,
            priceLow = 98.0f,
            volume = 1000,
            updatedAt = now()
        )
        Json.encodeToString(stockPrice)
        Json.encodeToString(listOf(stockPrice))

        val stock = StockDTO(
            code = "PZU",
            name = "PZU SA"
        )
        Json.encodeToString(stock)
        Json.encodeToString(listOf(stock))
    }
}
