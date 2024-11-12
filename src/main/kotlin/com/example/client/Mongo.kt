package com.example.client

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*

//cliente en la nube
private fun connectToMongoClient(dotenv: Dotenv): MongoClient {
    val user = dotenv["DB_MONGO_USER"]
    val password = dotenv["DB_MONGO_PASSWORD"]
    val host = dotenv["DB_MONGO_HOST"]
    val port = dotenv["DB_MONGO_PORT"]
    val maxPoolSize = dotenv["DB_MONGO_MAX_POOL_SIZE"]
    val credentials = if (user.isNotEmpty() && password.isNotEmpty()) "$user:$password@" else ""
    val uri = "mongodb+srv://$credentials$host/"
    val mongoClient = MongoClients.create(uri)
    return mongoClient
}


//cliente local
private fun connectToMongoClientLocal(dotenv: Dotenv): MongoClient {
    val host = dotenv["DB_MONGO_HOST"]
    val maxPoolSize = dotenv["DB_MONGO_MAX_POOL_SIZE"]
    val port = dotenv["DB_MONGO_PORT"]
    val uri = "mongodb://$host:$port/?maxPoolSize=$maxPoolSize&w=majority"
    val mongoClient = MongoClients.create(uri)
    return mongoClient }


//obtener la base de datos del cliente
private fun connectToMongoDatabase(dotenv: Dotenv, mongoClient: MongoClient): MongoDatabase {
    val databaseName = dotenv["DB_MONGO_DATABASE_NAME"]
    return mongoClient.getDatabase(databaseName)
}

//Para pruebas
fun connectToMongoDatabase(dotenv: Dotenv): MongoDatabase {
    val databaseName = dotenv["DB_MONGO_DATABASE_NAME"]
    return connectToMongoClientLocal(dotenv).getDatabase(databaseName)
}

//Se usa en la app, tambien cierra la conexion
fun Application.connectToMongoDB(dotenv: Dotenv): MongoDatabase {
    val client = connectToMongoClientLocal(dotenv)
    val database = connectToMongoDatabase(dotenv = dotenv, mongoClient = client)

        monitor.subscribe(ApplicationStopped) {
            client.close()
        }

    return database
}
