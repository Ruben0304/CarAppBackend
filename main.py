from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import strawberry
from strawberry.fastapi import GraphQLRouter
from contextlib import asynccontextmanager
from database.MongoConection import connect_to_mongo, close_mongo_connection,db  # Importamos las funciones para la conexión a MongoDB
from querys.Mutation import Mutation
from querys.Query import Query

# Gestor de contexto para el ciclo de vida de la aplicación
@asynccontextmanager
async def lifespan(app: FastAPI):
    await connect_to_mongo()   # Inicializamos la conexión a MongoDB al arrancar
    yield
    await close_mongo_connection()  # Cerramos la conexión cuando la app se detiene

# Crear el esquema de GraphQL con las Querys y Mutations
schema = strawberry.Schema(query=Query, mutation=Mutation)

# Crear la aplicación FastAPI con el gestor de ciclo de vida
app = FastAPI(lifespan=lifespan)

# Configurar CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # En producción, especifica los orígenes permitidos
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Endpoint raíz para verificar que el servidor está activo
@app.get("/")
async def root():
    return {"message": "Hello World"}

# Configurar el router de GraphQL con manejo de contexto
async def get_context():
    return {
        "db": db  # Añadimos la base de datos al contexto, útil si la necesitas en los resolvers
    }

# Router de GraphQL en la ruta /graphql
graphql_app = GraphQLRouter(
    schema,
    graphiql=True,
    context_getter=get_context
)

# Añadir el router a la app
app.include_router(graphql_app, prefix="/graphql")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )
