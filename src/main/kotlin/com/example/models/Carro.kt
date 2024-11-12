package com.example.models

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

data class Carro(val modelo: String? = null){
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): Carro = json.decodeFromString(document.toJson())
    }
}
