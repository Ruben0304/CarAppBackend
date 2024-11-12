package com.example

import com.cohere.api.Cohere
import com.example.client.connectToCohere
import com.example.implementations.EmbeddingServiceImpl
import com.example.services.EmbeddingService
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class EmbeddingServiceTest {

//    private val embeddingService: EmbeddingService = EmbeddingServiceImpl(connectToCohere("r2W7dzTlvwxjBFsKXedUVUm08X0RvjAVAHZfDcVX"))
//
//
//    @Test
//    fun `test embed single text returns valid vector`(): Unit = runBlocking {
//        // Arrange
//        val inputTexts = listOf("Este es un texto de prueba")
//
//        // Act
//        val result = embeddingService.embed(inputTexts)
//
//        println(result)
//        // Assert
//        assertNotNull(result)
//        assertEquals(1, result.size)
//        assertTrue(result[0].values.isNotEmpty())
//    }
}