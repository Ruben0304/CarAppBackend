package com.example.routes

import com.example.models.Pieza
import com.example.models.Response
import com.example.routes.definitions.Piezas
import com.example.services.dao.PiezaService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.piezasRoutes() {
    val piezaService: PiezaService by inject()

    routing {
        // Get all piezas
        get<Piezas> {
            try {
                val piezas = piezaService.all()
                call.respond(Response.success(piezas))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error retrieving piezas")
                )
            }
        }

        // Search piezas
        get<Piezas.Search> { params ->
            try {
                val results = piezaService.search(params.query)
                call.respond(Response.success(results))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error searching piezas")
                )
            }
        }

        // Get pieza by id
        get<Piezas.Id> { params ->
            try {
                piezaService.read(params.id)?.let { pieza ->
                    call.respond(Response.success(pieza))
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    Response.error("Pieza not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error retrieving pieza")
                )
            }
        }

        // Create new pieza
        post<Piezas> {
            try {
                val pieza = call.receive<Pieza>()
                val id = piezaService.create(pieza)
                call.respond(
                    HttpStatusCode.Created,
                    Response.success(id)
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error creating pieza")
                )
            }
        }

        // Update pieza
        put<Piezas.Id> { params ->
            try {
                val updatedPieza = call.receive<Pieza>()
                piezaService.update(params.id, updatedPieza)?.let {
                    call.respond(Response.success("Pieza updated successfully"))
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    Response.error("Pieza not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error updating pieza")
                )
            }
        }

        // Delete pieza
        delete<Piezas.Id> { params ->
            try {
                piezaService.delete(params.id)?.let {
                    call.respond(Response.success("Pieza deleted successfully"))
                } ?: call.respond(
                    HttpStatusCode.NotFound,
                    Response.error("Pieza not found")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    Response.error(e.message ?: "Error deleting pieza")
                )
            }
        }
    }
}

