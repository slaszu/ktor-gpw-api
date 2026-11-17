package pl.slaszu.module

import io.ktor.server.application.*
import kotlinx.coroutines.*
import kotlinx.datetime.DayOfWeek.FRIDAY
import kotlinx.datetime.DayOfWeek.MONDAY
import org.koin.ktor.ext.inject
import pl.slaszu.database.api.StockPricePersister
import pl.slaszu.getStockDTOForDB
import pl.slaszu.getStockPriceDTOForDB
import pl.slaszu.gpwparser.api.GpwParser
import pl.slaszu.now
import java.time.ZoneId
import kotlin.time.Duration.Companion.minutes

fun Application.configureScheduler() {

    val gpwParser by inject<GpwParser>()
    val stockPricePersister by inject<StockPricePersister>()

    println(">> Scheduler configured")

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val timeZone = ZoneId.of("Europe/Warsaw")

    scope.launch {
        while (isActive) {

            val now = now()
            val isWeekday = now.dayOfWeek in MONDAY..FRIDAY
            val isWorkingHour = now.hour in 9..17

            // run the task only on weekdays and during working hours (9:00 - 17:00)
            // and in period of 15 minutes (e.g., 9:00, 9:15, 9:30, 9:45, 10:00, etc.)

            if (isWeekday && isWorkingHour && now.minute % 15 == 0) {
                try {
                    println("Wykonywanie zadania dla: $now")
                    gpwParser.getDataForDate(now.date).forEach {
                        val stockDTO = it.getStockDTOForDB()
                        val stockPriceDTO = it.getStockPriceDTOForDB()
                        println(">>> Stock: $stockDTO")
                        println(">>> Stock price: $stockPriceDTO")
                        stockPricePersister.persist(stockDTO, stockPriceDTO)
                    }
                } catch (e: Exception) {
                    log.error("Błąd podczas wykonywania zadania", e)
                }
            }

            // Odstęp między kolejnymi uruchomieniami (np. co 15 minut)
            delay(1.minutes)
        }
    }

    monitor.subscribe(ApplicationStopped) {
        scope.cancel()
    }
}
