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
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): Chat = json.decodeFromString(document.toJson())
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
)

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
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromDocument(document: Document): LastMessage = json.decodeFromString(document.toJson())
    }
}




