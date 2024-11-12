package com.example.routes

import com.example.models.Carro
import com.example.routes.definitions.Carros
import com.example.services.dao.CarroService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.carrosRoutes() {

    val CarroService: CarroService by inject()

    routing {
        get("/Carros") {
            CarroService.all().let {
                call.respond (HttpStatusCode.OK,it)
            }
        }

        get < Carros > { Carro ->
            CarroService.read(Carro.id!!)?.let { call.respond(it) } ?: call.respond(
                HttpStatusCode.NotFound
            )
        }

        post < Carros > {
            val Carro = call.receive<Carro>()
            val id = CarroService.create(Carro)
            call.respond (HttpStatusCode.Created, id)
        }

        put < Carros > { Carro ->
            val updatedCarro = call.receive<Carro>()
            CarroService.update (Carro.id!!, updatedCarro)?.let {
            call.respond(
                HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
        }
        delete < Carros > { Carro ->
            CarroService.delete(Carro.id!!)?.let { call.respond(HttpStatusCode.OK) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
    }

}

