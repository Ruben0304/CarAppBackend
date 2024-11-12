package com.example.implementations.dao

import com.example.enums.PineconeCollections
import com.example.models.Pieza
import com.example.services.dao.PiezaService
import com.example.services.VectorialSearchService
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import com.mongodb.client.model.Filters
import kotlinx.coroutines.async
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.regex.Pattern


class PiezaServiceImpl : PiezaService,KoinComponent {
    private val database: MongoDatabase by inject()
    private val collection = database.getCollection("piezas")

    // List of piezas
    override suspend fun all(): List<Pieza> = withContext(Dispatchers.IO) {
        try {
            collection.find().map { document ->
                try {
                    val pieza = Pieza.fromDocument(document)
                    pieza
                } catch (e: Exception) {
                    throw e
                }
            }.toList()
        } catch (e: Exception) {
            throw e
        }
    }


    // Create new pieza
    override suspend fun create(pieza: Pieza): String = withContext(Dispatchers.IO) {
        val doc = pieza.toDocument()
        collection.insertOne(doc)
        doc["_id"].toString()
    }

    // Read a pieza
    override suspend fun read(id: String): Pieza? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("_id", ObjectId(id))).first()?.let(Pieza::fromDocument)
    }

    // Update a pieza
    override suspend fun update(id: String, pieza: Pieza): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), pieza.toDocument())
    }

    // Delete a pieza
    override suspend fun delete(id: String): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
    }


    override suspend fun findMany(ids: List<String>): List<Pieza> = withContext(Dispatchers.IO) {
        val objectIdList = ids.map { ObjectId(it) }
        collection.find(Filters.`in`("_id", objectIdList)).map(Pieza::fromDocument).toList()
    }

    override suspend fun search(query: String): List<Pieza> = withContext(Dispatchers.IO) {
        val searchService: VectorialSearchService by inject()


        val mongoDeferred = async {
            searchWithMongo(query)
        }

        val vectorialDeferred = async {
            val searchResult = searchService.search(query, PineconeCollections.PIEZAS)
            searchResult.forEach {
                println(it)
            }

            if (searchResult.isNotEmpty()) {
                findMany(searchResult)
            } else {
                emptyList()
            }
        }

        // Esperar a que ambas búsquedas terminen
        val mongoResults = mongoDeferred.await()
        val vectorialResults = vectorialDeferred.await()

        // Combinar resultados, priorizando MongoDB
        val combinedResults = mutableListOf<Pieza>()
        combinedResults.addAll(mongoResults)

        // Agregar resultados vectoriales que no estén ya en los resultados de MongoDB
        vectorialResults.forEach { vectorialPieza ->
            if (!combinedResults.any { it.id == vectorialPieza.id }) {
                combinedResults.add(vectorialPieza)
            }
        }

        combinedResults
    }

    private suspend fun searchWithMongo(query: String): List<Pieza> = withContext(Dispatchers.IO) {
        val pattern = Pattern.compile(".*${query}.*", Pattern.CASE_INSENSITIVE)
        collection.find( Filters.or(
            Filters.regex("descripcion", pattern),
            Filters.regex("nombre", pattern)
        )).map(Pieza::fromDocument).toList()
    }


}
