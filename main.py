# main.py
from fastapi import FastAPI, Depends
from fastapi.middleware.cors import CORSMiddleware
import strawberry
from strawberry.fastapi import GraphQLRouter
from typing import Any
from database.MongoConection import get_database, db_connection
# from querys.Mutation import Mutation
from querys.Query import Query

# Crear el esquema de GraphQL
# schema = strawberry.Schema(query=Query, mutation=Mutation)
schema = strawberry.Schema(query=Query)
# Crear la aplicación FastAPI
app = FastAPI()

# Configurar CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    return {"message": "Hello World"}

# Función para obtener el contexto de GraphQL
async def get_context() -> dict[str, Any]:
    async with get_database() as database:
        return {"db": database}

# Configurar el router de GraphQL
graphql_app = GraphQLRouter(
    schema,
    graphiql=True,
    context_getter=get_context
)

app.include_router(graphql_app, prefix="/graphql")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )