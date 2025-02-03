package com.example.implementations.dao

import com.example.models.Participant
import com.example.services.dao.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class UserServiceImpl: UserService, KoinComponent {

    override suspend fun findParticipantsbyIds(participantsIds: List<String>): List<Participant> = withContext(Dispatchers.IO) {
        participantsIds.map { participantId ->
            Participant(
                userId = ObjectId(participantId),
                username = "user" + Random.nextInt(0, 10),
                displayName = "displayName" + Random.nextInt(11, 20)
            )
        }
    }
}