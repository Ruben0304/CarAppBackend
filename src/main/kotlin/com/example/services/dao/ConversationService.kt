package com.example.services.dao

import com.example.interfaces.DAOService
import com.example.interfaces.Searchable
import com.example.models.Chat
import com.example.models.Participant

interface ConversationService : DAOService<Chat>, Searchable<Chat> {
    suspend fun getConversationsByUserId(userId: String): List<Chat>
    suspend fun getUnreadCount(userId: String): Int
    suspend fun createConversation(participants: List<Participant>): String
}

