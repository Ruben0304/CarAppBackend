package com.example.implementations

import com.example.enums.Distances
import com.example.enums.PineconeCollections
import com.example.models.EmbedData
import com.example.models.Vector
import com.example.services.VectorialDatabaseService
import io.pinecone.clients.Pinecone
import io.pinecone.exceptions.PineconeException
import io.pinecone.proto.QueryResponse
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VectorialDatabaseImpl : VectorialDatabaseService, KoinComponent {

    private val pinecone: Pinecone by inject()

    override suspend fun query(query: Vector, collection: PineconeCollections, maxResults: Int?): List<String> =
        withContext(Dispatchers.IO) {
            val index = pinecone.getIndexConnection("piezas")
            println(index.describeIndexStats())
            val vector = query.values.map { it.toFloat() }
            println(vector)
            try {
                val queryResponse: QueryResponseWithUnsignedIndices = index.queryByVector(maxResults ?: 10, vector);
                queryResponse.matchesList
//                    .filter { it.score >= Distances.SIMILAR.value}
//                    .sortedByDescending { it.score }
                    .map { it.id }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                emptyList()
            }

        }

    override suspend fun add(collection: PineconeCollections, embedDataList: List<EmbedData>): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val index = pinecone.getIndexConnection(collection.value)

                // Añade logs para debug
                println("Conectando a índice: $collection")
                println("Datos a insertar: ${embedDataList.size}")

                embedDataList.forEach { embedData ->
                    val vector = embedData.vector.values.map { it.toFloat() }
                    println("Insertando vector para ID: ${embedData.id}")
                    index.upsert(
                        embedData.id,
                        vector,
                    )
                }
                true
            } catch (e: PineconeException) {
                println("Error detallado: ${e.message}")
                e.printStackTrace()
                false
            }
        }

    override suspend fun addWithRetry(
        indexName: PineconeCollections,
        embedDataList: List<EmbedData>,
        maxRetries: Int,
        initialDelayMs: Long
    ) {
        var currentDelay = initialDelayMs
        var attempts = 0

        while (attempts < maxRetries) {
            try {
                add(indexName, embedDataList)
                return // Si tiene éxito, salimos de la función
            } catch (e: Exception) {
                attempts++

                if (attempts == maxRetries) {
                    throw e // Si alcanzamos el máximo de intentos, lanzamos la excepción
                }

                println("Intento $attempts falló. Reintentando en ${currentDelay}ms...")
                delay(currentDelay)
                currentDelay *= 2 // Backoff exponencial
            }
        }
    }

    override suspend fun addInChunks(
        indexName: PineconeCollections,
        embedDataList: List<EmbedData>,
        chunkSize: Int,
        maxRetries: Int
    ) {
        val chunks = embedDataList.chunked(chunkSize)

        chunks.forEachIndexed { index, chunk ->
            try {
                println("Procesando chunk ${index + 1}/${chunks.size} (${chunk.size} elementos)")
                addWithRetry(indexName, chunk, maxRetries)
                delay(500) // Pequeña pausa entre chunks para no sobrecargar
            } catch (e: Exception) {
                println("Error en chunk ${index + 1}: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun remove(collection: PineconeCollections, ids: List<String>): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(collection: PineconeCollections, id: String, embedData: EmbedData): Boolean {
        TODO("Not yet implemented")
    }

}