import strawberry

from database.MongoConection import db
from models.Car import Carro, CarroInputUpdate
from models.Conversation import Conversacion, Mensaje
from typing import List, Optional
from bson import ObjectId

@strawberry.type
class Mutation:

    #Insercion de un nuevo carro
    @strawberry.mutation
    async def insertar_carro(self, input: CarroInputUpdate) -> Carro:
        nuevo_carro = {
            "name": input.name,
            "year": input.year,
            "selling_price": input.selling_price,
            "km_driven": input.km_driven,
            "fuel": input.fuel,
            "seller_type": input.seller_type,
            "transmission": input.transmission,
            "owner": input.owner
        }
        resultado = await db.carros.insert_one(nuevo_carro)

        nuevo_id = str(resultado.inserted_id)

        del nuevo_carro["_id"]
        return Carro(_id=nuevo_id, **nuevo_carro)

    @strawberry.mutation
    async def actualizar_carro(self, id: str, input: CarroInputUpdate) -> Optional[Carro]:

        update_data = {k: v for k, v in input.__dict__.items() if v is not None}

        object_id = ObjectId(id)

        resultado = await db.carros.update_one({"_id": object_id}, {"$set": update_data})

        if resultado.matched_count == 0:
            return None

        carro_actualizado = await db.carros.find_one({"_id": object_id})

        return Carro(**carro_actualizado)