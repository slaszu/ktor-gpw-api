package pl.slaszu

import io.ktor.server.application.*
import pl.slaszu.module.*

fun Application.module() {
    configureSerialization()
    configureKoin()

    configureAuthentication()
    configureRouting()

    configureScheduler()
}