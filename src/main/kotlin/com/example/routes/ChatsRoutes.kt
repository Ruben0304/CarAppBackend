package com.example.routes

import com.example.models.Chat

import com.example.models.Response
import com.example.routes.definitions.Chats

import com.example.services.dao.ConversationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.chatsRoutes() {

    val chatService: ConversationService by inject()

    routing {
        // Get all chatss
        get<Chats> {
            try {
                val chats = chatService.all()
                call.respond(Response.success(chats))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error retrieving chatss")
                )
            }
        }


        get<Chats.Search> { params ->
            try {
                val results = chatService.search(params.query)
                call.respond(Response.success(results))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error searching chatss")
                )
            }
        }

        get<Chats.User> { params ->
            try {
                val results = chatService.getConversationsByUserId(params.userId)
                call.respond(Response.success(results))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error finding chatss")
                )
            }
        }

        // Get chats by id
        get<Chats.Id> { params ->
            try {
                chatService.read(params.id)?.let { chats ->
                    call.respond(Response.success(chats))
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    Response.error("chats not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error retrieving chats")
                )
            }
        }


        // Delete chats
        delete<Chats.Id> { params ->
            try {
                chatService.delete(params.id)?.let {
                    call.respond(Response.success("chats deleted successfully"))
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    Response.error("chats not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error deleting chats")
                )
            }
        }
    }
}

