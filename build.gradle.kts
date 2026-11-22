
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.graalvm.native)
}

group = "pl.slaszu"
version = "1.0.0"

ktor {
    fatJar {
        archiveFileName.set("ktor-gpw-api.jar")
    }
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}


graalvmNative {
    binaries {
        named("main") {
            verbose.set(true)

            buildArgs.add("--enable-native-access=ALL-UNNAMED")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            buildArgs.add("-H:+AddAllCharsets")
            buildArgs.add("--enable-url-protocols=http,https")
            buildArgs.add("-J-Xmx4g")

            imageName.set("ktor-gpw-api")
        }
    }
    binaries.all {
        resources.autodetect()
    }
    toolchainDetection.set(true)
    metadataRepository {
        enabled = true
    }
}

dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.routingOpenapi)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.server.auth)

    implementation(libs.kotlinx.datetime)

    implementation(libs.koin.ktor)
    implementation(libs.koin.loggerSlf4j)
    implementation(libs.logback.classic)
    implementation(project(":module-database"))
    implementation(project(":module-gpwparser"))

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
