package com.example.interfaces

import org.bson.Document


interface DAOService<T> {
    suspend fun all(): List<T>
    suspend fun create(item: T): String
    suspend fun read(id: String): T?
    suspend fun update(id: String, item: T): Document?
    suspend fun delete(id: String): Document?
    suspend fun findMany(ids: List<String>): List<T>
}