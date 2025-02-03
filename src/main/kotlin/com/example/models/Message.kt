package com.example.models

import com.example.plugins.MongoDBTimestampSerializer
import com.example.plugins.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.types.ObjectId
import java.time.Instant

@Serializable
data class Message(
    @Serializable(with = ObjectIdSerializer::class)
    @SerialName("messageId")
    @BsonId
    val messageId: ObjectId = ObjectId(),
    @Serializable(with = ObjectIdSerializer::class)
    @SerialName("senderId")
    val senderId: ObjectId = ObjectId(),
    val content: String,
    @Serializable(with = MongoDBTimestampSerializer::class)
    val timestamp: Instant,
    @SerialName("readBy")
    val readByUser: Boolean = false,
    @Serializable(with = ObjectIdSerializer::class)
    val respondsTo: ObjectId? = null,
) {
    fun toDocument(): Document {
        return Document().apply {
            put("messageId", messageId) // Mantiene ObjectId
            put("senderId", senderId) // Mantiene ObjectId
            put("content", content)
            put("timestamp", timestamp) // Guardar como timestamp
            put("readBy", readByUser)
            put("respondsTo", respondsTo) // Puede ser nulo
        }
    }

    fun toLastMessage() = LastMessage(
        content = content,
        senderId = senderId,
        timestamp = timestamp
    )

    companion object {
        fun fromDocument(document: Document): Message {
            return Message(
                messageId = document.getObjectId("messageId"),
                senderId = document.getObjectId("senderId"),
                content = document.getString("content"),
                timestamp = document.getDate("timestamp")?.toInstant() ?: Instant.now(),
                readByUser = document.getBoolean("readBy"),
                respondsTo = document.get("respondsTo") as? ObjectId // Manejar nulos
            )
        }
    }
}

