package com.example.implementations

import com.example.enums.EmbeddTypes
import com.example.enums.PineconeCollections
import com.example.models.Carro
import com.example.models.Pieza
import com.example.services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VectorialSearchImpl : VectorialSearchService, KoinComponent {
    private val embeddingService: EmbeddingService by inject()
    private val vectorialDatabaseService: VectorialDatabaseService by inject()
    private val spanishTransformerService: SpanishTransformerService by inject()

    override suspend fun search(query: String, collection: PineconeCollections): List<String> = withContext(Dispatchers.IO) {

        try {
           val vectores = embeddingService.embed(listOf(spanishTransformerService.extractKeywordsWithPhrases(query)), type = EmbeddTypes.SEARCH_QUERY)!!

            if (vectores.isNotEmpty()) {
                vectorialDatabaseService.query(query = vectores[0], collection = collection)
            }
            else {
                throw Exception("Fallo al hacer embedding")
            }

        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }

    }

}