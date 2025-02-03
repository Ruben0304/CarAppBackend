package com.example.routes

import com.example.models.Carro
import com.example.models.Response
import com.example.routes.definitions.Carros
import com.example.routes.definitions.Piezas
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

    val carroService: CarroService by inject()

    routing {
        get("/Carros") {
            carroService.all().let {
                call.respond (HttpStatusCode.OK,it)
            }
        }

        get < Carros > { Carro ->
            carroService.read(Carro.id!!)?.let { call.respond(it) } ?: call.respond(
                HttpStatusCode.NotFound
            )
        }

        // Search piezas
        get<Carros.Search> { params ->
            try {
                val results = carroService.search(params.query)
                call.respond(Response.success(results))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error searching piezas")
                )
            }
        }

        post < Carros > {
            val Carro = call.receive<Carro>()
            val id = carroService.create(Carro)
            call.respond (HttpStatusCode.Created, id)
        }

        put < Carros > { Carro ->
            val updatedCarro = call.receive<Carro>()
            carroService.update (Carro.id!!, updatedCarro)?.let {
            call.respond(
                HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
        }
        delete < Carros > { Carro ->
            carroService.delete(Carro.id!!)?.let { call.respond(HttpStatusCode.OK) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
    }

}

