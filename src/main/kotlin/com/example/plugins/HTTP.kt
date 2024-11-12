package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Duration

fun Application.configureHTTP() {
    routing {
        swaggerUI(path = "openapi", swaggerFile = "src/main/resources/openapi/documentation.yaml.kt")
    }
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowHeader("X-Client-Name")
        allowHost("api.cohere.com")
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
         // @TODO: Don't do this in production if possible. Try to limit it.
    }
}
