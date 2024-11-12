package com.example.client

import com.cohere.api.Cohere
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.engine.*

fun connectToCohere(dotenv: Dotenv): Cohere {
    return Cohere.builder()
        .token(dotenv["COHERE_API_KEY"])
        .build()
}