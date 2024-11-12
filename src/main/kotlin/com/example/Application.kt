package com.example

import com.example.di.config.configureDI
import com.example.plugins.*
import com.example.routes.config.configureRouting
import com.example.services.VectorialDatabaseService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

//import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
//    embeddedServer(CIO, port = 8081, module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDI()
    configureSockets()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
