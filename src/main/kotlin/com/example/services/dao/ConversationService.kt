package com.example.services.dao

import com.example.interfaces.DAOService
import com.example.interfaces.Searchable
import com.example.models.Chat
import com.example.models.Participant
import java.time.Instant

interface ConversationService : DAOService<Chat>, Searchable<Chat> {
    suspend fun getConversationsByUserId(userId: String): List<Chat>
    suspend fun findConversationByParticipants(participantIds: List<String>): Chat?
    suspend fun getUnreadCount(userId: String): Int
    suspend fun createConversation(participants: List<Participant>): String
    suspend fun getLastMessages(timestamp: String, userId: String): List<Chat>
}

