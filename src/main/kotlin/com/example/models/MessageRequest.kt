package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val senderId:String,
    val receiverId:String,
    val message: Message
)