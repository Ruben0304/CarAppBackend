from motor.motor_asyncio import AsyncIOMotorClient

class MongoDB:
   @staticmethod
   async def get_database(self):
       client = AsyncIOMotorClient("mongodb+srv://ruben:zixelowe1@personal.yycznyk.mongodb.net/")
       return client.CarApp
