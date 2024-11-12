package com.example.services

import com.example.enums.OAuthProvider
import com.example.models.User

interface AuthService {
    suspend fun signInWithEmail(email: String, password: String): User?
    suspend fun signInWithPhone(phone: String, password: String): User?
    suspend fun signInWithOAuth(provider: OAuthProvider, redirectUrl: String?): User?
    suspend fun retrieveUser(jwt: String): User?
    suspend fun updateUser(phone: String? = null, email: String?= null , password: String? = null): User?
    suspend fun registerUser(email: String? = null,phone: String? = null,password: String): User?
    suspend fun confirmUser(email: String? = null,phone: String? = null,code: String)
}


