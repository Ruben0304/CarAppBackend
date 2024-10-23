# Importamos FastAPI para crear la aplicación web
import asyncio
from contextlib import asynccontextmanager

from fastapi import FastAPI

# Importamos strawberry para definir el esquema de GraphQL
import strawberry
from motor.motor_asyncio import AsyncIOMotorClient

# Importamos GraphQLRouter de strawberry.fastapi para integrar GraphQL con FastAPI
from strawberry.fastapi import GraphQLRouter
from querys.Mutation import Mutation
# Importamos la clase Query que contiene nuestras consultas (queries) de GraphQL
from querys.Query import Query  # Asegúrate de que este archivo esté en la ruta correcta

# Crear el esquema de GraphQL usando la clase Query que define las consultas
schema = strawberry.Schema(query=Query, mutation=Mutation)

# Definir una ruta básica para verificar que el servidor esté funciona

app = FastAPI()
@app.get("/")
async def root():
    return {"message": "Hello Worddd"}  # Retorna un mensaje simple en formato JSON

# Registrar la ruta para el servicio GraphQL usando el esquema que definimos
# Habilitamos GraphiQL, una interfaz gráfica para hacer consultas GraphQL fácilmente
graphql_app = GraphQLRouter(schema, graphiql=True)

# Incluimos la ruta de GraphQL bajo el prefijo "/graphql", es decir, cuando el usuario
# navegue a /graphql, podrá hacer consultas GraphQL
app.include_router(graphql_app, prefix="/graphql")

@asynccontextmanager
async def lifespan(app: FastAPI):
    loop = asyncio.get_event_loop()
    if loop.is_closed():
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
    yield
    await loop.shutdown_asyncgens()

app.lifespan = lifespan

def start_server():
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

if __name__ == '__main__':
    start_server()
# if __name__ == "__main__":
#     import uvicorn
#
#     # Arranca el servidor FastAPI en la dirección 127.0.0.1 (localhost) y el puerto 8080
#     uvicorn.run(app, host="0.0.0.0", port=8000)
