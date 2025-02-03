package com.example

import com.example.client.connectToMongoDatabase
import com.example.di.dotEnvModule
import com.example.di.serviceModules
import com.example.implementations.dao.UserServiceImpl
import com.example.services.dao.UserService
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UsersParticipantsTest: KoinTest {

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
    fun testFindParticipantsbyIds() = runBlocking {
        val userId1 = "65b1a1b1c1d1e1f1a1b1c1d5"
        val userId2 = "65b1a1b1c1d1e1f1a1b1c1d6"

        val result = userService.findParticipantsbyIds(listOf(userId1, userId2))

        println(result)
    }

}