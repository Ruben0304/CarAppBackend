package com.example.implementations

import com.cohere.api.Cohere
import com.cohere.api.resources.v2.requests.V2EmbedRequest
import com.cohere.api.types.EmbedInputType
import com.cohere.api.types.EmbeddingType
import com.example.enums.EmbeddTypes
import com.example.models.Vector
import com.example.services.EmbeddingService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class EmbeddingServiceImpl : EmbeddingService,KoinComponent {
    private val cohere: Cohere by inject()
    override suspend fun embed(text: List<String>, type: EmbeddTypes): List<Vector>? = withContext(Dispatchers.IO) {
        val request = V2EmbedRequest.builder()
            .model("embed-multilingual-v3.0")
            .texts(text)
            .inputType(EmbedInputType.valueOf(type.name))
            .embeddingTypes(listOf(EmbeddingType.FLOAT))
            .build()

        val response = cohere.v2().embed(request).embeddings.float.map { it -> it.map { Vector(it) } }
        response.orElse(emptyList())
    }
}