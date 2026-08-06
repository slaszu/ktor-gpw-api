package pl.slaszu

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import pl.slaszu.core.di.coreModule
import org.koin.ktor.ext.get
import pl.slaszu.core.api.DatabaseInitializer

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            coreModule,
            module {
                single<HelloService> {
                    HelloService {
                        println(environment.log.info("Hello, World!"))
                    }
                }
            }
        )
    }
    
    // Wyciągamy instancję DOPIERO PO zainstalowaniu wtyczki
    val dbInitializer = get<DatabaseInitializer>() 
    
    // Jeśli potrzebujesz korutyn w Ktorze:
    launch {
        dbInitializer.init()
    }
}
