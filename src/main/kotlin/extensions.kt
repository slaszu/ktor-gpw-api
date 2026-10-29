package pl.slaszu

import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import java.time.format.DateTimeParseException

// Pobiera parametr i parsuje do LocalDate (domyślny format ISO: yyyy-MM-dd)
fun ApplicationCall.getLocalDatePathParam(name: String): LocalDate {
    val value = parameters[name] ?: throw IllegalArgumentException("Parameter not exists: $name")
    return try {
        LocalDate.parse(value)
    } catch (_: DateTimeParseException) {
        throw IllegalArgumentException("Parameter $name must has format YYYY-MM-DD")
    }
}

// Pobiera query parameter jako Int z domyślną wartością
fun ApplicationCall.getIntQueryParam(name: String, default: Int): Int {
    return request.queryParameters[name]?.toIntOrNull() ?: default
}