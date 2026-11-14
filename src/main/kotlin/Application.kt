package pl.slaszu

import io.ktor.server.application.*
import pl.slaszu.module.configureSwaggerUI
import pl.slaszu.module.configureKoin
import pl.slaszu.module.configureRouting
import pl.slaszu.module.configureScheduler
import pl.slaszu.module.configureSerialization

fun Application.module() {
    configureSerialization()
    configureKoin()
    configureRouting()
    configureSwaggerUI()
    configureScheduler()
}