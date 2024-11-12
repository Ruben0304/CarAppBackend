package com.example.services

import com.example.enums.EmbeddTypes
import com.example.models.Vector


interface EmbeddingService {
    suspend fun embed(text: List<String>, type: EmbeddTypes = EmbeddTypes.SEARCH_DOCUMENT): List<Vector>?
}

