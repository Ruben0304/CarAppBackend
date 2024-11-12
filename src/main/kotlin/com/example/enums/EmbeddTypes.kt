package com.example.enums

enum class EmbeddTypes {
    SEARCH_QUERY, //Used for embeddings stored in a vector database for search use-cases
    SEARCH_DOCUMENT, // Used for embeddings of search queries run against a vector DB to find relevant documents.
    CLASSIFICATION, // Used for embeddings passed through a text classifier.
    CLUSTERING, // Used for the embeddings run through a clustering algorithm.
    IMAGE // Used for embeddings with image input.
}
