from motor.motor_asyncio import AsyncIOMotorClient

client = AsyncIOMotorClient("mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/")
database =  client.CarApp
carrosDB = database.get_collection("carros")
conversationsDB = database.get_collection("conversations")
