package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.enums.PineconeCollections
import com.example.models.Message
import com.example.services.dao.ConversationService
import com.example.services.dao.MessageService
import com.example.services.dao.PiezaService
import com.example.services.dao.UserService
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.Instant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ChatsTest : KoinTest {
    private val chatService: ConversationService by inject()
    private val messageService: MessageService by inject()
    private val userService: UserService by inject()


    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                dotEnvModule(),
                module {
                    single { connectToMongoDatabase(get()) }
                },
                serviceModules()
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testConnectToMongoDB() = runBlocking {
        chatService.all().forEach {
            println(it.lastMessage)
        }

    }

    @Test
    fun testSendMessage() = runBlocking {
        val message = Message(
            senderId = ObjectId("65b1a1b1c1d1e1f1a1b1c1d1"),
            content = "TEST MESSAGE",
            timestamp = Instant.now()
        )
        messageService.sendMessage("507f1f77bcf86cd799439011", message)
        assert(true)
    }

    @Test
    fun testGetMessageByConvId() = runBlocking {
        val messages = messageService.getMessagesByConversationId("507f1f77bcf86cd799439011")
        println(messages)
    }

    @Test
    fun testGetLastMessages() = runBlocking {
        val testConversationId = "507f1f77bcf86cd799439011"
        val testUserId = "65b1a1b1c1d1e1f1a1b1c1d1"
        val testTimestamp = Instant.now().minusSeconds(3600).toString() // Hace 1 hora

        // Enviar un mensaje antiguo (no debería ser recuperado)
        val oldMessage = Message(
            senderId = ObjectId(testUserId),
            content = "Mensaje antiguo",
            timestamp = Instant.now().minusSeconds(7200) // Hace 2 horas
        )
        messageService.sendMessage(testConversationId, oldMessage)

        // Enviar un mensaje reciente (debería ser recuperado)
        val newMessage = Message(
            senderId = ObjectId(testUserId),
            content = "Mensaje reciente",
            timestamp = Instant.now().minusSeconds(1800) // Hace 30 minutos
        )
        messageService.sendMessage(testConversationId, newMessage)

        // Obtener mensajes después del testTimestamp
        val result = chatService.getLastMessages(testTimestamp, testUserId)
        println(result)
        // Validar que solo el mensaje reciente está presente
        assertTrue(result.isNotEmpty(), "El resultado no debe estar vacío")
        assertTrue(result.all { chat -> chat.messages.all { it.timestamp.isAfter(Instant.parse(testTimestamp)) } },
            "Todos los mensajes deben ser posteriores al timestamp dado")

        println("Mensajes obtenidos: ${result.flatMap { it.messages.map { msg -> msg.content } }}")
    }

    @Test
    fun testFindConversationByParticipants() = runBlocking {
        val userId1 = "65b1a1b1c1d1e1f1a1b1c1d1"
        val userId2 = "65b1a1b1c1d1e1f1a1b1c1d2"

        val result = chatService.findConversationByParticipants(listOf(userId1, userId2))

        println(result)
    }

    @Test
    fun testCreateConversation() = runBlocking {
        val userId1 = "65b1a1b1c1d1e1f1a1b1c1d5"
        val userId2 = "65b1a1b1c1d1e1f1a1b1c1d6"

        val result = chatService.createConversation(userService.findParticipantsbyIds(listOf(userId1, userId2)))
        println(result)
    }

}
