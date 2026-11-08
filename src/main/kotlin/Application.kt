package pl.slaszu

import io.ktor.server.application.*

fun Application.module() {
    configureHttp()
    configureSerialization()
    configureKoin()
    configureRouting()
}