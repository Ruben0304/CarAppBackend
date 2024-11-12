package com.example.interfaces

interface Searchable<T> {
    suspend fun search(query: String): List<T>
}