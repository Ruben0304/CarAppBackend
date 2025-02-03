package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.clientModules
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.enums.PineconeCollections
import com.example.models.EmbedData
import com.example.services.EmbeddingService
import com.example.services.dao.PiezaService
import com.example.services.SpanishTransformerService
import com.example.services.VectorialDatabaseService
import com.example.services.dao.CarroService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

class PineconeTest : KoinTest {

    private val embeddingService: EmbeddingService by inject()
    private val vectorialDatabaseService: VectorialDatabaseService by inject()
    private val piezasService: CarroService by inject()
    private val spanishTransformerService: SpanishTransformerService by inject()

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
    fun testUpsertPinecone(): Unit = runBlocking {
        val inputTexts = listOf("Este es un texto de prueba")

        // Act
        val result = embeddingService.embed(inputTexts)

        if (result != null)
            println(vectorialDatabaseService.add(PineconeCollections.PIEZAS, listOf(EmbedData("kjkj", result[0]))))

//        // Assert
        assertNotNull(result)

    }

    @Test
    fun `insertar una coleccion entera desde mongo a pinecone`(): Unit = runBlocking {
        val piezas = piezasService.all()

        // Crear los pares de ID y texto
        val textsWithIds = piezas.map { item ->
            val texto = buildString {
                append("${item.modelo} ,")
                append("${item.descripcion} ,")
            }
//            val textoTransformado = buildString {
//                append("${item.nombre} ,")
//                append(spanishTransformerService.extractKeywordsWithPhrases(texto))
//            }
            item.id.toString() to texto
        }

        val (ids, texts) = textsWithIds.unzip()

        println(texts[0])
        println(texts[0].length)

//         Obtener vectores
        val vectors =
            embeddingService.embed(texts) ?: throw IllegalStateException("No se pudieron obtener los vectores")

        // Usar assert en lugar de require ya que es una clase de prueba
        assert(vectors.size == ids.size) {
            "Cantidad de vectores (${vectors.size}) diferente a la cantidad de IDs (${ids.size})"
        }

        // Podemos agregar más assertions para validar
        val embedDataList = ids.zip(vectors) { id, vector ->
            EmbedData(id = id, vector = vector)
        }

        // Assertions adicionales para verificar la correspondencia
        assert(embedDataList.size == ids.size) {
            "La lista final tiene un tamaño diferente al esperado"
        }

        embedDataList.forEachIndexed { index, embedData ->
            assert(embedData.id == ids[index]) {
                "Error de correspondencia en índice $index"
            }
        }

        // Verifica que ningún vector sea nulo
        assert(embedDataList.none { it.vector.values.any { value -> value.isNaN() } }) {
            "Se encontraron valores NaN en los vectores"
        }

// Verifica que todos los IDs sean válidos
        assert(embedDataList.none { it.id.isBlank() }) {
            "Se encontraron IDs vacíos"
        }

// Verifica la dimensionalidad de los vectores
        val dimensionEsperada = embedDataList.first().vector.values.size
        assert(embedDataList.all { it.vector.values.size == dimensionEsperada }) {
            "No todos los vectores tienen la misma dimensión"
        }


        try {
            vectorialDatabaseService.addInChunks(
                indexName = PineconeCollections.CARROS,
                embedDataList = embedDataList,
                chunkSize = 3, // Reducido a 3 vectores por chunk
                maxRetries = 3
            )
        } catch (e: Exception) {
            println("Error final después de todos los reintentos: ${e.message}")
            throw e
        }

    }

}
