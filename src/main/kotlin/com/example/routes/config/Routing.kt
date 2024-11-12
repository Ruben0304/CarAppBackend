package com.example.routes.config

import com.example.routes.authRoutes
import com.example.routes.carrosRoutes
import com.example.routes.chatsRoutes
import com.example.routes.piezasRoutes
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    piezasRoutes()
    carrosRoutes()
    chatsRoutes()
    authRoutes()
}


