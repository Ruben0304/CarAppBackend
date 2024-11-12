package com.example.di

import com.cohere.api.Cohere
import com.example.client.connectToCohere
import com.example.client.connectToMongoDB
import com.example.client.connectToPinecone
import com.mongodb.client.MongoDatabase
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.server.application.*
import io.pinecone.clients.Pinecone
import org.koin.core.module.Module
import org.koin.dsl.module


fun Application.applicationModules(): Module {
    return module {
        single<MongoDatabase> { connectToMongoDB(get()) }
    }
}
