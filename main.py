# main.py
from fastapi import FastAPI, Depends
from fastapi.middleware.cors import CORSMiddleware
import strawberry
from strawberry.fastapi import GraphQLRouter
from typing import Any
from database.MongoConection import db_connection
# from querys.Mutation import Mutation
from querys.Query import Query
import logging

# Crear el esquema de GraphQL
# schema = strawberry.Schema(query=Query, mutation=Mutation)
schema = strawberry.Schema(query=Query)
# Crear la aplicación FastAPI
# Configurar logging
logging.basicConfig(level=logging.INFO)

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
async def startup_db_client():
    try:
        await db_connection.connect()
        logging.info("Database connection established during startup")
    except Exception as e:
        logging.error(f"Failed to establish database connection during startup: {e}")
        raise e

@app.on_event("shutdown")
async def shutdown_db_client():
    await db_connection.close()

async def get_db():
    db = await db_connection.get_db()
    if db_connection.is_connected is False:
        raise Exception("Database connection not available")
    return db

async def get_context() -> dict[str, Any]:
    try:
        db = await get_db()
        return {"db": db}
    except Exception as e:
        logging.error(f"Error getting context: {e}")
        raise
@app.get("/")
async def root():
    return {"message": "Hello World"}


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