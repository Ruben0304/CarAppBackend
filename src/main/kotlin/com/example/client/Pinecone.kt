package com.example.client

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.pinecone.clients.Pinecone


fun connectToPinecone(dotenv: Dotenv): Pinecone {
    return Pinecone.Builder(dotenv["DB_PINECONE_API_KEY"]).build()
}