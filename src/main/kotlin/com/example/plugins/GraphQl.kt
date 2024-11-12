package com.example.plugins


import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.expediagroup.graphql.server.operations.Query


class HelloWorldQuery : Query {
    fun hello(): String = "Hello World!"
}




fun Application.graphQLModule() {


}
