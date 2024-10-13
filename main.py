from fastapi import FastAPI

import strawberry
from strawberry.fastapi import GraphQLRouter

from querys.Query import Query

# Crear el esquema de GraphQL
schema = strawberry.Schema(query=Query)

# Configurar la aplicación FastAPI
app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello Worddd"}


@app.get("/test")
async def roota():
    return {"message": "Hello Worddd"}


# Registrar la ruta GraphQL con GraphiQL habilitado
graphql_app = GraphQLRouter(schema, graphiql=True)  # Habilitar GraphiQL
app.include_router(graphql_app, prefix="/graphql")  # Sin el prefijo "/graphql"
