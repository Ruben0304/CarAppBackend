from motor.motor_asyncio import AsyncIOMotorClient

# Conexión a MongoDB
client = AsyncIOMotorClient('mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/')
db = client['CarApp']