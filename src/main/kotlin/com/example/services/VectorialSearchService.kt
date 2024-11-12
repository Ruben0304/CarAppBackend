package com.example.services

import com.example.enums.PineconeCollections


interface VectorialSearchService {
    suspend fun search(query: String, collection: PineconeCollections) : List<String>
}