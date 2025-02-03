package com.example.services.dao

import com.example.interfaces.DAOService
import com.example.interfaces.Searchable
import com.example.models.Participant
import com.example.models.Pieza

interface UserService{
    suspend fun findParticipantsbyIds(participantsIds: List<String>): List<Participant>
}