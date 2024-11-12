package com.example.di.config

import com.example.di.applicationModules
import com.example.di.clientModules
import com.example.di.dotEnvModule
import com.example.di.serviceModules


import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(
            dotEnvModule(),
            applicationModules(),
            clientModules(),
            serviceModules()
        )
    }
}
