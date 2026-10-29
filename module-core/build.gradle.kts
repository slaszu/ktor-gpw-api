plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    implementation(libs.mysql.connector)
    implementation(libs.hikaricp)
}