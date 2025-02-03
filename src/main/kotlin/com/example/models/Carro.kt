package com.example.models

import com.example.plugins.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Carro(
    @Serializable(with = ObjectIdSerializer::class)
    @SerialName("_id")
    @BsonId
    val id: ObjectId = ObjectId(),
    val modelo: String,
    @SerialName("tipo_oferta")
    val tipoOferta: String,
    val precio: Double,
    @SerialName("precio_por")
    val precioPor: String,
    val transmision: String,
    val descripcion: String,
    @Serializable(with = ObjectIdSerializer::class)
    @SerialName("user_id")
    val userId: ObjectId,
    @SerialName("caracteristicas_extra")
    val caracteristicasExtra: List<ExtraFeature>
) {
    fun toDocument(): Document {
        return Document().apply {
            put("_id", id)
            put("modelo", modelo)
            put("tipo_oferta", tipoOferta)
            put("precio", precio)
            put("precio_por", precioPor)
            put("transmision", transmision)
            put("descripcion", descripcion)
            put("user_id", userId)
            put("caracteristicas_extra", caracteristicasExtra.map { it.toDocument() })
        }
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): Carro {
            return json.decodeFromString(document.toJson())
        }
    }
}

@Serializable
data class ExtraFeature(
    val clave: String,
    val valor: String
) {
    fun toDocument(): Document {
        return Document().apply {
            put("clave", clave)
            put("valor", valor)
        }
    }
}