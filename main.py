# Importamos FastAPI para crear la aplicación web
from typing import List

from fastapi import FastAPI
from dotenv import load_dotenv

# Importamos strawberry para definir el esquema de GraphQL
import strawberry
from pydantic import BaseModel

# Importamos GraphQLRouter de strawberry.fastapi para integrar GraphQL con FastAPI
from strawberry.fastapi import GraphQLRouter

from embeddings.EmbeddingGenerator import embed_documents, embed_queries
# Importamos la clase Query que contiene nuestras consultas (queries) de GraphQL
from querys.Query import Query  # Asegúrate de que este archivo esté en la ruta correcta

# Crear el esquema de GraphQL usando la clase Query que define las consultas
schema = strawberry.Schema(query=Query)

# Carga las variables de entorno del archivo .env
load_dotenv()

# Modelo de entrada para el endpoint POST
class TextInput(BaseModel):
    text: str

# Crear la aplicación FastAPI
app = FastAPI()

# Definir una ruta básica para verificar que el servidor esté funcionando
@app.get("/")
async def root():
    return {"message": "Hello Worddd"}  # Retorna un mensaje simple en formato JSON


# Modelo de entrada para el endpoint POST, acepta una lista de textos
class TextsInput(BaseModel):
    texts: List[str]


# Endpoint para /embed_documents con POST, recibe una lista de textos
@app.post("/embed_documents")
async def embed_documents_route(data: TextsInput):
    embeddings = await embed_documents(data.texts)
    return {"embeddings": embeddings}


# Endpoint para /embed_queries con GET, recibe una lista de textos
@app.post("/embed_queries")
async def embed_queries_route(data: TextsInput):
    embeddings = await embed_queries(data.texts)
    return {"embeddings": embeddings}



# Registrar la ruta para el servicio GraphQL usando el esquema que definimos
# Habilitamos GraphiQL, una interfaz gráfica para hacer consultas GraphQL fácilmente
graphql_app = GraphQLRouter(schema, graphiql=True)

# Incluimos la ruta de GraphQL bajo el prefijo "/graphql", es decir, cuando el usuario
# navegue a /graphql, podrá hacer consultas GraphQL
app.include_router(graphql_app, prefix="/graphql")




if __name__ == "__main__":
    import uvicorn

    # Arranca el servidor FastAPI en la dirección 127.0.0.1 (localhost) y el puerto 8080
    uvicorn.run(app, host="127.0.0.1", port=8080)
