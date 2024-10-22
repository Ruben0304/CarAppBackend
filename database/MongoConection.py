# MongoConnection.py
from motor.motor_asyncio import AsyncIOMotorClient
from typing import Optional

class DatabaseConnection:
    _instance = None
    client: Optional[AsyncIOMotorClient] = None
    db = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    async def connect(self):
        if not self.client:
            self.client = AsyncIOMotorClient('mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/')
            self.db = self.client['CarApp']
        return self.db

    async def close(self):
        if self.client:
            self.client.close()
            self.client = None
            self.db = None

# Instancia global
db_connection = DatabaseConnection()
