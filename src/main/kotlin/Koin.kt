package pl.slaszu

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import pl.slaszu.core.di.coreModule

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            coreModule(
                url = environment.config.property("database.url").getString(),
                user = environment.config.property("database.user").getString(),
                pass = environment.config.property("database.password").getString(),
            ), module {
                single { environment.config }
            })
    }
}
