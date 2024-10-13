import strawberry

from database.MongoConection import db
from models.Car import Carro
from models.Conversation import Conversacion
from typing import List


@strawberry.type
class Query:
    @strawberry.field
    async def conversaciones(self) -> List[Conversacion]:
        conversaciones_data = await db.conversations.find().to_list(length=None)
        return [Conversacion(**conv) for conv in conversaciones_data]

    @strawberry.field
    async def carros(self) -> List[Carro]:
        carros_data = await db.carros.find().to_list(length=None)
        return [Carro(**car) for car in carros_data]