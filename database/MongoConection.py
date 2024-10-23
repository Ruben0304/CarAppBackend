from motor.motor_asyncio import AsyncIOMotorClient

class MongoDB:
    _instance = None
    client = None
    db = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls.client = AsyncIOMotorClient("mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/")
            cls.db = cls.client.CarApp
        return cls._instance

    @classmethod
    def close(cls):
        if cls.client:
            cls.client.close()

mongodb = MongoDB().db
