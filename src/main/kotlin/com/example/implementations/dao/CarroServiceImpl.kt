package com.example.implementations.dao

import com.example.models.Carro
import com.example.services.dao.CarroService
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import com.mongodb.client.model.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class CarroServiceImpl : CarroService, KoinComponent {
    private val database: MongoDatabase by inject()
    private val collection = database.getCollection("carros")

    // List of Carros
    override suspend fun all(): List<Carro> = withContext(Dispatchers.IO) {
        collection.find().map { document ->
            Carro.fromDocument(document)
        }.toList()
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

    override suspend fun findMany(ids: List<String>): List<Carro> {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String): List<Carro> {
        TODO("Not yet implemented")
    }
}
