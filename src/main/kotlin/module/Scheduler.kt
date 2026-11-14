package pl.slaszu.module

import io.ktor.server.application.*
import kotlinx.coroutines.*
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

fun Application.configureScheduler() {

    println(">> Scheduler configured")

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val timeZone = ZoneId.of("Europe/Warsaw")

    scope.launch {
        while (isActive) {
            val now = ZonedDateTime.now(timeZone)
            val isWeekday = now.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
            val isWorkingHour = now.toLocalTime() in LocalTime.of(9, 0)..LocalTime.of(17, 0)

            if (isWeekday && isWorkingHour) {
                try {
                    println("Wykonywanie zadania o: $now")


                } catch (e: Exception) {
                    log.error("Błąd podczas wykonywania zadania", e)
                }
            }

            // Odstęp między kolejnymi uruchomieniami (np. co 15 minut)
            delay(15.minutes)
        }
    }

    monitor.subscribe(ApplicationStopped) {
        scope.cancel()
    }
}
