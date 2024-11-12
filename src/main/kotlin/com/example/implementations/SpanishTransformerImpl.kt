package com.example.implementations

import com.example.services.SpanishTransformerService
import org.apache.lucene.analysis.es.SpanishAnalyzer
import java.util.*

class SpanishTransformerImpl: SpanishTransformerService {
    // Obtener las stopwords predefinidas en español de Lucene
    private val spanishStopWords = SpanishAnalyzer.getDefaultStopSet()


    override fun extractKeywords(text: String, maxKeywords: Int): String {
        return text.lowercase(Locale.getDefault())
            .replace("[^a-záéíóúñ\\s]".toRegex(), " ")
            .split("\\s+".toRegex())
            .filter { it.length > 3 } // Palabras de más de 3 letras
            .filter { it !in spanishStopWords }
            .distinct()
            .take(maxKeywords)
            .joinToString(" ")
    }

    // Versión que mantiene frases importantes
    override fun extractKeywordsWithPhrases(text: String): String {
        val words = text.lowercase(Locale.getDefault()).split("\\s+".toRegex())
        val uniquePhrases = mutableSetOf<String>()
        val keywords = mutableListOf<String>()
        var i = 0

        while (i < words.size) {
            val word = words[i]
            if (word !in spanishStopWords && word.length > 3) {
                // Buscar si forma parte de una frase
                if (i < words.size - 2 &&
                    words[i + 1] in setOf("de", "del", "la", "el") &&
                    words[i + 2] !in spanishStopWords
                ) {
                    // Es una frase tipo "casa de campo"
                    val phrase = "${word} ${words[i + 1]} ${words[i + 2]}"
                    uniquePhrases.add(phrase)
                    i += 3
                } else {
                    uniquePhrases.add(word)
                    i++
                }
            } else {
                i++
            }
        }

        keywords.addAll(uniquePhrases)
        return keywords.joinToString(" ")
    }

}

