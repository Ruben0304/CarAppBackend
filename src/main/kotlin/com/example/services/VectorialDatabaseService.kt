package com.example.services

import com.example.enums.PineconeCollections
import com.example.models.EmbedData
import com.example.models.Vector
import io.pinecone.clients.Pinecone
import org.bson.Document

interface VectorialDatabaseService {
    suspend fun query(query: Vector, collection: PineconeCollections, maxResults: Int? = null) : List<String>
    suspend fun add(collection: PineconeCollections, embedDataList: List<EmbedData>): Boolean
    suspend fun addWithRetry(indexName: PineconeCollections, embedDataList: List<EmbedData>, maxRetries: Int = 3, initialDelayMs: Long = 1000)
    suspend fun addInChunks(indexName: PineconeCollections, embedDataList: List<EmbedData>, chunkSize: Int = 100, maxRetries: Int = 3)
    suspend fun remove(collection: PineconeCollections, ids: List<String>): Boolean
    suspend fun update(collection: PineconeCollections, id: String, embedData: EmbedData): Boolean
}