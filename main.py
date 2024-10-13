from fastapi import FastAPI
import strawberry
from strawberry.fastapi import GraphQLRouter

from querys.Query import Query  # Asegúrate de que este archivo esté bien

# Crear el esquema de GraphQL
schema = strawberry.Schema(query=Query)

# Configurar la aplicación FastAPI
app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Hello Worddd"}

@app.get("/test")
async def test():
    return {"message": "This is a test"}

# Registrar la ruta GraphQL con GraphiQL habilitado
graphql_app = GraphQLRouter(schema, graphiql=True)
app.include_router(graphql_app, prefix="/graphql")


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="127.0.0.1", port=8080)
