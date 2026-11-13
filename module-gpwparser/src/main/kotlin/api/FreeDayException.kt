package pl.slaszu.gpwparser.api

import kotlinx.datetime.LocalDate
import pl.slaszu.gpwparser.internal.FreeDaySpecification

class FreeDayException (message: String): Exception(message) {
    companion object {
        fun fromDate(date: LocalDate): FreeDayException {
            return FreeDayException(
                "{$date} is free day !"
            )
        }
    }
}