package com.example.implementations.dao

import com.example.enums.MongoCollections
import com.example.models.Chat
import com.example.models.Participant
import com.example.services.dao.ConversationService
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class ConversationServiceImpl : ConversationService, KoinComponent {
    private val database: MongoDatabase by inject()
    private val collection = database.getCollection(MongoCollections.CHATS.value)

    override suspend fun all(): List<Chat> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.descending("updatedAt"))
            .map { Chat.fromDocument(it) }
            .toList()
    }

    override suspend fun create(item: Chat): String = withContext(Dispatchers.IO) {
        val doc = item.toDocument()
        collection.insertOne(doc)
        doc.getObjectId("_id").toString()
    }

    override suspend fun read(id: String): Chat? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("_id", ObjectId(id)))
            .firstOrNull()
            ?.let { Chat.fromDocument(it) }
    }

    override suspend fun findMany(ids: List<String>): List<Chat> = withContext(Dispatchers.IO) {
        val objectIds = ids.map { ObjectId(it) }
        collection.find(Filters.`in`("_id", objectIds))
            .map { Chat.fromDocument(it) }
            .toList()
    }

    override suspend fun getConversationsByUserId(userId: String): List<Chat> = withContext(Dispatchers.IO) {
        collection.find(
            Filters.elemMatch("participants",
                Filters.eq("userId", ObjectId(userId))
            )
        )
            .sort(Sorts.descending("updatedAt"))
            .map { Chat.fromDocument(it) }
            .toList()
    }

    override suspend fun getUnreadCount(userId: String): Int = withContext(Dispatchers.IO) {
        collection.countDocuments(
            Filters.and(
                Filters.elemMatch("participants",
                    Filters.eq("userId", ObjectId(userId))
                ),
                Filters.not(
                    Filters.elemMatch("messages",
                        Filters.and(
                            Filters.exists("readBy"),
                            Filters.`in`("readBy", listOf(ObjectId(userId)))
                        )
                    )
                )
            )
        ).toInt()
    }

    override suspend fun createConversation(participants: List<Participant>): String {
        val chat = Chat(
            id = ObjectId(),
            participants = participants,
            messages = emptyList(),
            lastMessage = null,
            updatedAt = Instant.now()
        )
        return create(chat)
    }

    override suspend fun search(query: String): List<Chat> = withContext(Dispatchers.IO) {
        collection.find(
            Filters.or(
                Filters.text(query),
                Filters.elemMatch(
                    "participants",
                    Filters.or(
                        Filters.regex("username", query, "i"),
                        Filters.regex("displayName", query, "i")
                    )
                )
            )
        )
            .map { Chat.fromDocument(it) }
            .toList()
    }


    override suspend fun update(id: String, item: Chat): Document? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): Document? {
        TODO("Not yet implemented")
    }
}