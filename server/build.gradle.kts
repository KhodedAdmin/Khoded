import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.flyway)
    application
}

group = "com.probro.khoded"
version = "1.0.0"
application {
    mainClass.set("com.probro.khoded.ApplicationKt")
    applicationDefaultJvmArgs =
        listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.test)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cors)
    implementation(libs.ktor.resources)
    implementation(libs.ktor.validation)
    implementation(libs.ktor.autoHeader)
    implementation(libs.ktor.sessions)
    implementation(libs.ktor.statusPages)
    implementation(libs.ktor.content.negotiation)

    //Google Apis
    implementation(libs.google.api.client)
    implementation(libs.google.auth.library.credentials)
    implementation(libs.google.auth.library.oauth2.http)
    implementation(libs.google.api.services.people)
    implementation(libs.google.oauth.client.jetty)


    // Postgresdb
    api(libs.postgresql)
    // Hikari (for Connection pooling)
    api(libs.hikaricp)


    //Exposed (Jetbrains library for database connection)
    api(libs.exposed.core)
    api(libs.exposed.jdbc)
    // use a dao to have it be similar to android logic
    api(libs.exposed.dao)
    api(libs.exposed.money)
    api(libs.exposed.kotlin.datetime)
    api(libs.exposed.crypt)
    api(libs.exposed.json)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}

ktor {
    fatJar {
        archiveFileName.set("khodedBackend.jar")
    }
//Set up the docker image.
    docker {
        jreVersion.set(JavaVersion.VERSION_19)
        localImageName.set("khodedBackend")
        imageTag.set("0.0.01")
        portMappings.set(
            listOf(
                DockerPortMapping(
                    outsideDocker = 8080,
                    insideDocker = 8080,
                    protocol = DockerPortMappingProtocol.TCP
                )
            )
        )
    }
}

flyway {
    driver = "org.postgresql.driver"
    url = "jdbc:postgresql://localhost:5432/khodedBackend"
    user = "khoeded_dev"
    password = "a97toUMhqtaFLthRcv7iysmLqtv5HGrR"
    schemas = arrayOf("khoded_base_state")
    defaultSchema = "khoded_base_state"
}