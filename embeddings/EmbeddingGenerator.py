import cohere
import os
import asyncio

# co = cohere.ClientV2(os.getenv("API_KEY_COHERE"))
co = cohere.AsyncClientV2("r2W7dzTlvwxjBFsKXedUVUm08X0RvjAVAHZfDcVX")


async def embed_documents(text):
    response = await co.embed(
        texts=text,
        model="embed-multilingual-v3.0",
        input_type="search_document",
        embedding_types=["float"]
    )
    return response.embeddings


async def embed_queries(text):
    response = await co.embed(
        texts=text,
        model="embed-multilingual-v3.0",
        input_type="search_query",
        embedding_types=["float"]
    )
    return response.embeddings
