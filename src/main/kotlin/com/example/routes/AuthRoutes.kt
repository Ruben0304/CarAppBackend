package com.example.routes


import com.example.routes.definitions.*
import com.example.services.AuthService
import io.github.jan.supabase.auth.exception.AuthRestException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.apache.http.auth.InvalidCredentialsException
import org.koin.ktor.ext.inject


fun Application.authRoutes() {
    val authService: AuthService by inject()

    routing {
        post("/auth/signin/email") {
            try {
                val request = call.receive<EmailSignInRequest>()
                val user = authService.signInWithEmail(request.email, request.password)
                call.respond(HttpStatusCode.OK, "Logueado correctamente")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error de autenticación")
            }
        }

        post("/auth/signin/phone") {
            try {
                val request = call.receive<PhoneSignInRequest>()
                val user = authService.signInWithPhone(request.phone, request.password)
                call.respond(HttpStatusCode.OK, "Logueado correctamente")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error de autenticación")
            }
        }

        post("/auth/signin/oauth") {
            try {
                val request = call.receive<OAuthSignInRequest>()
                val user = authService.signInWithOAuth(request.provider, request.redirectUrl)
                call.respond(HttpStatusCode.OK, "Logueado correctamente")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error de autenticación")
            }
        }

        post("/auth/signup") {
            try {
                val request = call.receive<SignUpRequest>()
                val user = authService.registerUser(
                    email = request.email,
                    phone = request.phone,
                    password = request.password
                )
                call.respond(HttpStatusCode.Created, "Registrado correctamente")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error en el registro")
            }
        }

        post("/auth/user") {
            try {
                val request = call.receive<UpdateUserRequest>()
                val user = authService.updateUser(
                    phone = request.phone,
                    email = request.email,
                    password = request.password
                )
                call.respond(HttpStatusCode.OK, "Actualizado correctamente")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error actualizando el usuario")
            }
        }

        post("/auth/confirm/mail") {
            try {
                val request = call.receive<ConfirmUserRequest>()
                val user = authService.confirmUser(
                    email = request.email,
                    code = request.code
                )
                call.respond(HttpStatusCode.OK, "Confirmado")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error confirmado")
            }
        }
        post("/auth/confirm/phone") {
            try {
                val request = call.receive<ConfirmUserRequest>()
                val user = authService.confirmUser(
                    phone = request.phone,
                    code = request.code
                )
                call.respond(HttpStatusCode.OK, "Confirmado")
            } catch (e: AuthRestException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error confirmado")
            }
        }
    }
}