package pl.slaszu.module

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {

    val user = environment.config.property("authentication.username").getString()
    val pass = environment.config.property("authentication.password").getString()

    install(Authentication) {
        basic("auth-basic") {

            skipWhen { call ->
                val host = call.request.local.remoteHost

                host == "127.0.0.1" ||
                        host == "localhost" ||
                        host == "0.0.0.0" ||
                        isDockerPrivateIp(host)
            }

            realm = "Access to the '/' path"
            validate { credentials ->
                val isValid = credentials.name == user && credentials.password == pass
                if (isValid) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}

fun isDockerPrivateIp(ip: String): Boolean {
    if (!ip.startsWith("172.")) return false
    val parts = ip.split(".")
    if (parts.size != 4) return false
    val secondOctet = parts[1].toIntOrNull() ?: return false

    // Prywatna pula to 172.16.0.0 do 172.31.255.255
    return secondOctet in 16..31
}