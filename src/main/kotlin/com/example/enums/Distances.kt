package com.example.enums

enum class Distances(val value: Float) {
    // Valores recomendados de similitud coseno
     VERY_SIMILAR(0.55f),      // Casi idénticos
     SIMILAR(0.45f)     ,      // Muy similares
     MODERATELY_SIMILAR(0.35f), // Moderadamente similares
     SOMEWHAT_SIMILAR(0.3f),   // Algo similares
     LOOSELY_SIMILAR(0.2f)    // Vagamente similares
}