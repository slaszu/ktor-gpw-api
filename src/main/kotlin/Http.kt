package pl.slaszu

import io.ktor.http.ContentType
import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.OpenApiDocSource

fun Application.configureHttp() {
    routing {
        swaggerUI("openapi") {
            info = OpenApiInfo("GPW Api", "1.0")
            source = OpenApiDocSource.Routing(ContentType.Application.Json) {
                routingRoot.descendants()
            }
        }
    }
}
