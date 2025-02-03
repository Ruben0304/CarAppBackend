package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.models.Carro
import com.example.models.ExtraFeature
import com.example.services.dao.CarroService
import com.mongodb.assertions.Assertions.assertNotNull
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class CarrosTest  : KoinTest {
    val carrosService: CarroService by inject()

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
    fun testGetAllCarros() = runBlocking {
        val result = carrosService.all()
        println(result)
    }

    @Test
    fun testCreateCarro() = runBlocking {
        // Datos de prueba
        val userId = ObjectId()
        val carro = Carro(
            modelo = "Toyota Corolla 2022",
            tipoOferta = "renta",
            precio = 500.00,
            precioPor = "día",
            transmision = "automático",
            descripcion = "Excelente estado, mantenimiento al día, con seguro incluido.",
            userId = userId,
            caracteristicasExtra = listOf(
                ExtraFeature("color", "Blanco"),
                ExtraFeature("año", "2022"),
                ExtraFeature("kilometraje", "15000 km"),
                ExtraFeature("combustible", "Gasolina")
            )
        )

        // Llamamos al método create
        val carroId = carrosService.create(carro)

        // Verificamos que el ID devuelto no sea nulo
        assertNotNull(carroId)

        // Verificamos que el carro se haya guardado correctamente
        val carroCreado = carrosService.read(carroId)
        println(carroCreado)
        assertNotNull(carroCreado)
        assertEquals(carro.modelo, carroCreado?.modelo)
        assertEquals(carro.tipoOferta, carroCreado?.tipoOferta)
        carroCreado?.precio?.let { assertEquals(carro.precio, it, 0.01) }
        assertEquals(carro.precioPor, carroCreado?.precioPor)
        assertEquals(carro.transmision, carroCreado?.transmision)
        assertEquals(carro.descripcion, carroCreado?.descripcion)
        assertEquals(carro.userId, carroCreado?.userId)
        assertEquals(carro.caracteristicasExtra.size, carroCreado?.caracteristicasExtra?.size)

//        carrosService.delete(carroId)
//
//        val carroEliminado = carrosService.read(carroId)
//        assertNull(carroEliminado)
    }
}