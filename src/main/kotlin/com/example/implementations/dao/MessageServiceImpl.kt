package com.example.implementations.dao

import com.example.enums.MongoCollections
import com.example.models.Message
import com.example.services.dao.MessageService
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MessageServiceImpl : MessageService, KoinComponent {
    private val database: MongoDatabase by inject()
    private val collection = database.getCollection(MongoCollections.CHATS.value)

    override suspend fun getMessagesByConversationId(
        conversationId: String
    ): List<Message> = withContext(Dispatchers.IO) {

        collection.find(Filters.eq("_id", ObjectId(conversationId)))

            .first()
            ?.get("messages", List::class.java)
            ?.map { Message.fromDocument(it as Document) }
//            ?.sortedByDescending { it.timestamp }
            ?: emptyList()
    }

    override suspend fun sendMessage(conversationId: String, message: Message): String = withContext(Dispatchers.IO) {
        val messageDoc = message.toDocument()

        collection.updateOne(
            Filters.eq("_id", ObjectId(conversationId)),
            Updates.combine(
                Updates.push("messages", messageDoc),
                Updates.set("lastMessage", message.toLastMessage().toDocument()),
                Updates.set("updatedAt", message.timestamp)
            )
        )

        message.messageId.toString()
    }

    override suspend fun markAsRead(
        conversationId: String,
        messageIds: List<String>,
        userId: String
    ): Unit = withContext(Dispatchers.IO) {
        val objectIds = messageIds.map { ObjectId(it) }

        collection.updateMany(
            Filters.and(
                Filters.eq("_id", ObjectId(conversationId)),
                Filters.elemMatch("messages",
                    Filters.`in`("messageId", objectIds)
                )
            ),
            Updates.addToSet("messages.$.readBy", ObjectId(userId))
        )
    }

    override suspend fun getUnreadMessages(
        conversationId: String,
        userId: String
    ): List<Message> = withContext(Dispatchers.IO) {
        collection.find(
            Filters.and(
                Filters.eq("_id", ObjectId(conversationId)),
                Filters.elemMatch("messages",
                    Filters.or(
                        Filters.exists("readBy", false),
                        Filters.not(
                            Filters.`in`("readBy", listOf(ObjectId(userId)))
                        )
                    )
                )
            )
        )
            .projection(Projections.slice("messages",1))
            .first()
            ?.get("messages", List::class.java)
            ?.map { Message.fromDocument(it as Document) }
            ?.filter { message ->
                 !message.readByUser
            }
            ?: emptyList()
    }

    override suspend fun search(query: String): List<Message> = withContext(Dispatchers.IO) {
        collection.aggregate(
            listOf(
                Aggregates.match(Filters.text(query)),
                Aggregates.unwind("\$messages"),
                Aggregates.match(
                    Filters.or(
                        Filters.regex("messages.content", query, "i")
                    )
                ),
                Aggregates.replaceRoot("\$messages")
            )
        )
            .map { Message.fromDocument(it) }
            .toList()
    }

    // Métodos base de DAOService
    override suspend fun all(): List<Message> = withContext(Dispatchers.IO) {
        collection.aggregate(
            listOf(
                Aggregates.unwind("\$messages"),
                Aggregates.replaceRoot("\$messages")
            )
        )
            .map { Message.fromDocument(it) }
            .toList()
    }

    override suspend fun create(item: Message): String  = withContext(Dispatchers.IO)  {
        val message = item.toDocument()
        collection.insertOne(message)
        message["_id"].toString()
    }

    override suspend fun read(id: String): Message? = withContext(Dispatchers.IO) {
        collection.aggregate(
            listOf(
                Aggregates.unwind("\$messages"),
                Aggregates.match(Filters.eq("messages.messageId", ObjectId(id))),
                Aggregates.replaceRoot("\$messages")
            )
        )
            .firstOrNull()
            ?.let { Message.fromDocument(it) }
    }

    override suspend fun findMany(ids: List<String>): List<Message> = withContext(Dispatchers.IO) {
        val objectIds = ids.map { ObjectId(it) }
        collection.aggregate(
            listOf(
                Aggregates.unwind("\$messages"),
                Aggregates.match(Filters.`in`("messages.messageId", objectIds)),
                Aggregates.replaceRoot("\$messages")
            )
        )
            .map { Message.fromDocument(it) }
            .toList()
    }

    override suspend fun update(id: String, item: Message): Document? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): Document? {
        TODO("Not yet implemented")
    }
}