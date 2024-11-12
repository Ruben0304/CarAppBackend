package com.example.services

interface SpanishTransformerService {
    fun extractKeywords(text: String, maxKeywords: Int = 20): String
    fun extractKeywordsWithPhrases(text: String): String
}