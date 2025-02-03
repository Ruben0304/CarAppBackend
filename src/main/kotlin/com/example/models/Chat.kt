package com.example.models

import com.example.plugins.MongoDBTimestampSerializer
import com.example.plugins.ObjectIdSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

@Serializable
data class Chat(
    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId
    val id: ObjectId = ObjectId(),
    val participants: List<Participant>,
    val messages: List<Message>,
    @SerialName("lastMessage")
    val lastMessage: LastMessage?,
    @SerialName("updatedAt")
    @Serializable(with = MongoDBTimestampSerializer::class)
    val updatedAt: Instant
) {
    fun toDocument(): Document {
        return Document().apply {
            put("_id", id) // Asegura que el ID se almacene como ObjectId
            put("participants", participants.map { it.toDocument() }) // Convertir cada participante
            put("messages", messages.map { it.toDocument() }) // Convertir mensajes
            put("lastMessage", lastMessage?.toDocument()) // Convertir lastMessage si no es nulo
            put("updatedAt", updatedAt) // Guardar Instant como timestamp
        }
    }
    companion object {
        fun fromDocument(document: Document): Chat {
            return Chat(
                id = document.getObjectId("_id"), // Recupera el ID como ObjectId
                participants = (document["participants"] as List<Document>).map { Participant.fromDocument(it) },
                messages = (document["messages"] as List<Document>).map { Message.fromDocument(it) },
                lastMessage = document.get("lastMessage")?.let { LastMessage.fromDocument(it as Document) },
                updatedAt = document.getDate("updatedAt")?.toInstant() ?: Instant.now(), // Convertir timestamp a Instant
            )
        }
    }
}


@Serializable
data class Participant(
    @Serializable(with = ObjectIdSerializer::class)
    @SerialName("userId")
    val userId: ObjectId = ObjectId(),
    val username: String,
    @SerialName("displayName")
    val displayName: String
){
    fun toDocument(): Document {
        return Document().apply {
            put("userId", userId) // Mantiene ObjectId
            put("username", username)
            put("displayName", displayName)
        }
    }

    companion object {
        fun fromDocument(document: Document): Participant {
            return Participant(
                userId = document.getObjectId("userId"),
                username = document.getString("username"),
                displayName = document.getString("displayName")
            )
        }
    }
}

@Serializable
data class LastMessage(
    val content: String,
    @Serializable(with = MongoDBTimestampSerializer::class)
    val timestamp: Instant,
    @SerialName("senderId")
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId
    val senderId: ObjectId = ObjectId()
){
    fun toDocument(): Document {
        return Document().apply {
            put("content", content)
            put("timestamp", timestamp) // Guardar como timestamp
            put("senderId", senderId) // Mantener ObjectId
        }
    }

    companion object {
        fun fromDocument(document: Document): LastMessage {
            return LastMessage(
                content = document.getString("content"),
                timestamp = document.getDate("timestamp")?.toInstant() ?: Instant.now(),
                senderId = document.getObjectId("senderId")
            )
        }
    }
}




