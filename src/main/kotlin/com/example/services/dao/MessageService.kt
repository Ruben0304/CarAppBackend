package com.example.services.dao

import com.example.interfaces.DAOService
import com.example.interfaces.Searchable
import com.example.models.Message

interface MessageService : DAOService<Message>, Searchable<Message> {
    suspend fun getMessagesByConversationId(conversationId: String): List<Message>
    suspend fun sendMessage(conversationId: String, message: Message): String
    suspend fun markAsRead(conversationId: String, messageIds: List<String>, userId: String)
    suspend fun getUnreadMessages(conversationId: String, userId: String): List<Message>
}