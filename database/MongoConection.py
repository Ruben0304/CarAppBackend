from motor.motor_asyncio import AsyncIOMotorClient

# Variables globales para el cliente y la base de datos
client = None
db = None

# Función para inicializar la conexión a MongoDB
async def connect_to_mongo():
    global client, db
    client = AsyncIOMotorClient('mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/')
    db = client['CarApp']  # Reemplaza 'CarApp' por el nombre de tu base de datos

# Función para cerrar la conexión a MongoDB
async def close_mongo_connection():
    global client
    if client:
        client.close()
