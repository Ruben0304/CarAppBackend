package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.bson.types.ObjectId
import java.time.Instant


fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = true
        })
    }
    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toHexString())
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        return when (decoder) {
            is JsonDecoder -> {
                when (val element = decoder.decodeJsonElement()) {
                    is JsonObject -> {
                        // Maneja formato MongoDB {"$oid": "..."}
                        element["\$oid"]?.jsonPrimitive?.content?.let { ObjectId(it) }
                            ?: throw IllegalStateException("No se pudo encontrar el valor del ObjectId en el objeto JSON")
                    }

                    is JsonPrimitive -> {
                        val content = element.content
                        if (ObjectId.isValid(content)) {
                            ObjectId(content)
                        } else {
                            throw IllegalArgumentException("Invalid ObjectId format: $content")
                        }
                    }

                    else -> throw IllegalStateException("Unexpected JSON element for ObjectId: $element")
                }
            }

            else -> ObjectId(decoder.decodeString())
        }
    }
}

object MongoDBTimestampSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MongoDBTimestamp", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Instant
    ) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("This serializer can be used only with Json format")
        val jsonElement = jsonDecoder.decodeJsonElement()
        val jsonObject = jsonElement.jsonObject
        val date = jsonObject["\$date"]?.jsonPrimitive?.content
        return Instant.parse(date)
    }
}
