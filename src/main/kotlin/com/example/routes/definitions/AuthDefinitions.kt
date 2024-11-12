package com.example.routes.definitions

import com.example.enums.OAuthProvider
import io.ktor.resources.*
import kotlinx.serialization.Serializable

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

import io.ktor.http.*

@Serializable
@Resource("/auth")
class Auth {
    @Serializable
    @Resource("signin")
    class SignIn(val parent: Auth = Auth()) {
        @Serializable
        @Resource("email")
        data class Email(val parent: SignIn = SignIn())

        @Serializable
        @Resource("phone")
        data class Phone(val parent: SignIn = SignIn())

        @Serializable
        @Resource("oauth")
        data class OAuth(val parent: SignIn = SignIn())
    }

    @Serializable
    @Resource("signup")
    data class SignUp(val parent: Auth = Auth())

    @Serializable
    @Resource("user")
    data class User(val parent: Auth = Auth())
}

@Serializable
data class EmailSignInRequest(
    val email: String,
    val password: String
)

@Serializable
data class PhoneSignInRequest(
    val phone: String,
    val password: String
)

@Serializable
data class OAuthSignInRequest(
    val provider: OAuthProvider,
    val redirectUrl: String?
)

@Serializable
data class SignUpRequest(
    val email: String? = null,
    val phone: String? = null,
    val password: String
)

@Serializable
data class UpdateUserRequest(
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null
)

@Serializable
data class ConfirmUserRequest(
    val email: String? = null,
    val phone: String? = null,
    val code: String
)
