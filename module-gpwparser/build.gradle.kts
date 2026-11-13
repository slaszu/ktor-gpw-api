plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.koin.core)

    implementation("de.focus-shift:jollyday-core:2.24.0")
    implementation("de.focus-shift:jollyday-jaxb:2.24.0")

    implementation("org.apache.poi:poi-ooxml:6.0.0")
    implementation("org.jsoup:jsoup:1.24.1")

    implementation(libs.kotlinx.datetime)
}