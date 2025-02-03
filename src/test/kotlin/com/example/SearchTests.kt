package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.clientModules
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.routes.definitions.Carros
import com.example.services.dao.CarroService
import com.example.services.dao.PiezaService
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test

class SearchTests: KoinTest {



    @BeforeTest
    fun setUp() {
        org.koin.core.context.startKoin {
            modules(
                dotEnvModule(),
                clientModules(),
                module {
                    single { connectToMongoDatabase(get()) }
                },
                serviceModules()
            )
        }
    }

    @Test
    fun searchTestCarro() = runBlocking {
        val piezasService: CarroService by inject()


        piezasService.search("honda").forEach {
            println(it.modelo)
        }
        assert(true)
    }
    @Test
    fun searchTest() = runBlocking {
        val piezasService: PiezaService by inject()


        piezasService.search("juego de silenciadores").forEach {
            println(it.nombre)
        }
        assert(true)
    }
}