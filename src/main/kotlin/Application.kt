package pl.slaszu

import io.ktor.server.application.*
import pl.slaszu.module.configureHttp
import pl.slaszu.module.configureKoin
import pl.slaszu.module.configureRouting
import pl.slaszu.module.configureSerialization

fun Application.module() {
    configureHttp()
    configureSerialization()
    configureKoin()
    configureRouting()
}