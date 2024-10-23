from contextlib import asynccontextmanager

from motor.motor_asyncio import AsyncIOMotorClient
from pymongo import MongoClient

client = MongoClient("mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/")
database =  client.CarApp
carrosDB = database.get_collection("carros")
piezasDB = database.get_collection("piezas")
conversationsDB = database.get_collection("conversations")

