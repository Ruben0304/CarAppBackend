package com.example.models

import com.example.plugins.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.Document
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId

import org.bson.codecs.pojo.annotations.BsonId


@Serializable
data class Vectorization(
    val palabras_clave: List<String>,
    val frases_clave: List<String>
)

@Serializable
data class Pieza(
    @SerialName("_id") // Importante: mapea el campo _id de MongoDB
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId
    val id: ObjectId = ObjectId(),
    val nombre: String,
    val descripcion: String,
    val foto: String,
    val modelos_compatibles: List<String>,
    val estado: String,
    val precio: Double,
    val informacion_adicional: String,
    val id_vendedor: Int,
    val vectorizacion: Vectorization
) {
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): Pieza = json.decodeFromString(document.toJson())
    }
}

