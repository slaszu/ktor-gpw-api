package pl.slaszu.module

import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import pl.slaszu.core.api.StockPriceRepository
import pl.slaszu.core.di.coreModule
import pl.slaszu.gpwparser.api.GpwParser
import pl.slaszu.gpwparser.di.gpwparserModule
import kotlin.time.Clock

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
            )
        )

        try {
            val gpwParser = koin.get<GpwParser>()
            val data = gpwParser.getDataForDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
            println(">>> Gpw data")
            println(data)

        } catch (e: Exception) {
            println(">>GPWParser `error: ${e.localizedMessage}")
        }


        try {
            val stocks = koin.get<StockPriceRepository>()
            val latest = stocks.getLatest("PZU", 1)
            println(">>> Latest price for PZU")
            println(latest)
        } catch (e: Exception) {
            println(">>> DB ERROR: ${e.message}")
        }
    }
}
