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
    val readByUser: Boolean
) {
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))
    fun toLastMessage() =
        LastMessage(content = content, senderId = senderId, timestamp = timestamp)

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        fun fromDocument(document: Document): Message = json.decodeFromString(document.toJson())
    }

}

