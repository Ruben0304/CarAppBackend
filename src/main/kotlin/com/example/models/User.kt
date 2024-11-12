package com.example.models

import com.auth0.jwt.JWT

data class User(
    val id: String,
    val email: String? = null,
    val phone: String? = null,
    val jwt: JWT? = null,
)

