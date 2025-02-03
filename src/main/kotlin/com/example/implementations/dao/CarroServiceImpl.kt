package com.example.implementations.dao

import com.example.enums.PineconeCollections
import com.example.models.Carro
import com.example.models.Pieza
import com.example.services.VectorialSearchService
import com.example.services.dao.CarroService
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


class CarroServiceImpl : CarroService, KoinComponent {
    private val database: MongoDatabase by inject()
    private val collection = database.getCollection("cars")

    // List of Carros
    override suspend fun all(): List<Carro> = withContext(Dispatchers.IO) {
        try{
            collection.find().map { document ->
                try {
                    val carro = Carro.fromDocument(document)
                    carro
                }catch (e: Exception){
                    throw e
                }
            }.toList()
        } catch (e: Exception){
            throw e
        }
    }


    // Create new Carro
    override suspend fun create(carro: Carro): String = withContext(Dispatchers.IO) {
        val doc = carro.toDocument()
        collection.insertOne(doc)
        doc["_id"].toString()
    }

    // Read a Carro
    override suspend fun read(id: String): Carro? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("_id", ObjectId(id))).first()?.let(Carro::fromDocument)
    }

    // Update a Carro
    override suspend fun update(id: String, carro: Carro): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), carro.toDocument())
    }

    // Delete a Carro
    override suspend fun delete(id: String): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
    }

    override suspend fun findMany(ids: List<String>): List<Carro> = withContext(Dispatchers.IO) {
        val objectIdList = ids.map { ObjectId(it) }
        collection.find(Filters.`in`("_id", objectIdList)).map(Carro::fromDocument).toList()
    }


    override suspend fun search(query: String): List<Carro> = withContext(Dispatchers.IO) {
        val searchService: VectorialSearchService by inject()


        val mongoDeferred = async {
            searchWithMongo(query)
        }

        val vectorialDeferred = async {
            val searchResult = searchService.search(query, PineconeCollections.CARROS)
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
        val combinedResults = mutableListOf<Carro>()
        combinedResults.addAll(mongoResults)

        // Agregar resultados vectoriales que no estén ya en los resultados de MongoDB
        vectorialResults.forEach { vectorialCarro ->
            if (!combinedResults.any { it.id == vectorialCarro.id }) {
                combinedResults.add(vectorialCarro)
            }
        }

        combinedResults
    }

    private suspend fun searchWithMongo(query: String): List<Carro> = withContext(Dispatchers.IO) {
        val pattern = Pattern.compile(".*${query}.*", Pattern.CASE_INSENSITIVE)
        collection.find( Filters.or(
            Filters.regex("descripcion", pattern),
            Filters.regex("modelo", pattern)
        )).map(Carro::fromDocument).toList()
    }
}
