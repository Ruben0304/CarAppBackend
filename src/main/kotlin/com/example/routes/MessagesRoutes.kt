package com.example.routes

import com.example.models.Message
import com.example.models.MessageRequest
import com.example.models.Participant
import com.example.routes.definitions.Messages
import com.example.models.Response
import com.example.services.dao.ConversationService
import com.example.services.dao.MessageService
import com.example.services.dao.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bson.types.ObjectId
import java.time.Instant

fun Application.messagesRoutes() {
    val messageService: MessageService by inject()
    val conversationService: ConversationService by inject()
    val userService: UserService by inject()

    routing {
        //Get all messages from a conversation
        get<Messages.Chat> { params ->
            try {
                val messages = messageService.getMessagesByConversationId(params.chatId)
                call.respond(Response.success(messages))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error retrieving messages")
                )
            }
        }

        post("/messages") {
            try {
                // Obtener el cuerpo como texto
                val requestBody = call.receiveText()

                // Usar kotlinx.serialization para procesar el JSON manualmente
                val jsonElement = Json.parseToJsonElement(requestBody).jsonObject

                // Extraer los campos del cuerpo
                val senderId = jsonElement["senderId"]?.jsonPrimitive?.content ?: error("SenderId no encontrado")
                val receiverId = jsonElement["receiverId"]?.jsonPrimitive?.content ?: error("ReceiverId no encontrado")

                // Extraer el JSON del mensaje
                val messageJson = jsonElement["message"]?.jsonObject ?: error("Mensaje no encontrado")

                // Acceder directamente a los campos dentro del mensaje
                val messageSenderId = messageJson["senderId"]?.jsonPrimitive?.content ?: error("SenderId en message no encontrado")
                print(messageSenderId)
                val content = messageJson["content"]?.jsonPrimitive?.content ?: error("Contenido no encontrado")
                val timestamp = messageJson["timestamp"]?.jsonPrimitive?.content ?: error("Timestamp no encontrado")
                println(1)

                val message = Message(
                    senderId = ObjectId(messageSenderId),
                    content = content,
                    timestamp = Instant.parse(timestamp)
                )
                println(2)

                val conversation = conversationService.findConversationByParticipants(listOf(senderId, receiverId))
                val conversationId = conversation?.id?.toHexString()
                    ?:conversationService.createConversation(userService.findParticipantsbyIds(listOf(senderId, receiverId)))

                println(conversationId)
                messageService.sendMessage(conversationId, message)

                call.respond(HttpStatusCode.Created, Response.success("Mensaje enviado con éxito"))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error al enviar mensaje")
                )
            }
        }


    }
}

