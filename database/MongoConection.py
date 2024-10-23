from motor.motor_asyncio import AsyncIOMotorClient

mongodb_client = None
mongodb = None

async def init_db():
    global mongodb_client
    global mongodb
    mongodb_client = AsyncIOMotorClient("mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/")
    mongodb = mongodb_client["CarApp"]

async def close_db():
    global mongodb_client
    mongodb_client.close()
