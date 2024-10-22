from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import strawberry
from strawberry.fastapi import GraphQLRouter
from contextlib import asynccontextmanager
import asyncio
from querys.Mutation import Mutation
from querys.Query import Query

# Función para inicializar el estado de la aplicación
async def init_app():
    # Aquí puedes inicializar conexiones a bases de datos u otros recursos
    pass

# Función para cerrar recursos
async def shutdown_app():
    # Aquí puedes cerrar conexiones a bases de datos u otros recursos
    pass

# Gestor de contexto para el ciclo de vida de la aplicación
@asynccontextmanager
async def lifespan(app: FastAPI):
    # Inicialización
    await init_app()
    yield
    # Limpieza
    await shutdown_app()

# Crear el esquema de GraphQL
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

@app.get("/")
async def root():
    return {"message": "Hello World"}

# Configurar el router de GraphQL con manejo de contexto
async def get_context():
    # Aquí puedes añadir objetos al contexto que necesites en tus resolvers
    return {
        "loop": asyncio.get_running_loop()
    }

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
        reload=True,
        loop="asyncio"
    )