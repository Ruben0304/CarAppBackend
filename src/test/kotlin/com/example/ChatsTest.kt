package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.enums.PineconeCollections
import com.example.services.dao.ConversationService
import com.example.services.dao.PiezaService
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ChatsTest: KoinTest {
    private val chatService: ConversationService by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                dotEnvModule(),
                module {
                    single { connectToMongoDatabase(get()) }
                },
                serviceModules()
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testConnectToMongoDB() = runBlocking {
        chatService.all().forEach {
            println(it.lastMessage)
        }

    }

}
