# MongoConnection.py
from motor.motor_asyncio import AsyncIOMotorClient
from contextlib import asynccontextmanager

class DatabaseConnection:
    def __init__(self):
        self.client = None
        self.db = None

    async def connect(self):
        self.client = AsyncIOMotorClient('mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/')
        self.db = self.client['CarApp']
        return self.db

    async def close(self):
        if self.client:
            self.client.close()

# Crear una instancia global
db_connection = DatabaseConnection()

# Gestor de contexto para usar en FastAPI
@asynccontextmanager
async def get_database():
    try:
        await db_connection.connect()
        yield db_connection.db
    finally:
        await db_connection.close()
