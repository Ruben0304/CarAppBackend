package com.example.routes.definitions

import com.example.models.Message
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
@Resource("/piezas")
class Piezas {
    @Serializable
    @Resource("search")
    data class Search(val parent: Piezas = Piezas(), val query: String)

    @Serializable
    @Resource("{id}")
    data class Id(val parent: Piezas = Piezas(), val id: String)
}

@Serializable
@Resource("/chats")
class Chats {
    @Serializable
    @Resource("search")
    data class Search(val parent: Chats = Chats(), val query: String)

    @Serializable
    @Resource("user/{userId}")
    data class User(val parent: Chats = Chats(), val userId: String)

    @Serializable
    @Resource("{id}")
    data class Id(val parent: Chats = Chats(), val id: String)

    @Serializable
    @Resource("last_messages")
    data class LastMessages(val parent: Chats = Chats(), val timestamp: String, val userId: String)

}

@Serializable
@Resource("/messages")
class Messages {
    @Serializable
    @Resource("chat")
    data class Chat(val parent: Messages = Messages(), val chatId: String)
}

@Serializable
@Resource("/carros")
class Carros(val id: String? = null) {
    @Serializable
    @Resource("search")
    data class Search(val parent: Carros = Carros(), val query: String)
}




