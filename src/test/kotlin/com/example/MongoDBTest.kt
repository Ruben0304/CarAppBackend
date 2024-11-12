package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.enums.PineconeCollections
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

class MongoDBTest : KoinTest {
    private val mongoDatabase: MongoDatabase by inject()
    private val piezaService: PiezaService by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(dotEnvModule(),
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
        val coleccion = mongoDatabase.getCollection(PineconeCollections.PIEZAS.value)
        assertNotNull(coleccion)
        piezaService.all().forEach {
            println(it.nombre)
        }

    }

}
