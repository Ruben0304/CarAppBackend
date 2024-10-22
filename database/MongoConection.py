# MongoConnection.py
from motor.motor_asyncio import AsyncIOMotorClient
from typing import Optional
import logging

class DatabaseConnection:
    _instance = None
    client: Optional[AsyncIOMotorClient] = None
    db = None
    is_connected = False

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    async def connect(self):
        if not self.is_connected:
            try:
                self.client = AsyncIOMotorClient('mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/',
                                                 serverSelectionTimeoutMS=5000)
                # Verificar la conexión
                await self.client.admin.command('ping')
                self.db = self.client['CarApp']
                self.is_connected = True
                logging.info("Successfully connected to MongoDB")
            except Exception as e:
                logging.error(f"Failed to connect to MongoDB: {e}")
                self.is_connected = False
                raise Exception(f"Database connection failed: {e}")
        return self.db

    async def get_db(self):
        if not self.is_connected:
            await self.connect()
        return self.db

    async def close(self):
        if self.client:
            self.client.close()
            self.client = None
            self.db = None
            self.is_connected = False
            logging.info("MongoDB connection closed")

# Instancia global
db_connection = DatabaseConnection()