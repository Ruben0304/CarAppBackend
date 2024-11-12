package com.example.routes.definitions

import io.ktor.resources.*
import kotlinx.serialization.Serializable


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
}

@Serializable
@Resource("/carros")
class Carros(val id: String? = null)



