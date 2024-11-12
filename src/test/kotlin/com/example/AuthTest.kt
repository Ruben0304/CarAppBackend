package com.example

import com.example.client.connectToMongoDatabase
import com.example.client.connectToSupabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.services.AuthService
import com.example.services.dao.ConversationService
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthTest : KoinComponent {
    private val authService: AuthService by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                dotEnvModule(),
                module {
                    single { connectToSupabase(get()) }
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
    fun testSupabaseRegister(): Unit = runBlocking {
        println(authService.registerUser(email = "w@w.com", password = "Zixelowe1"))
    }

    @Test
    fun testSupabaseLogin(): Unit = runBlocking {
        println(authService.signInWithEmail(email = "w@w.com", password = "Zixelowe1"))
    }

}