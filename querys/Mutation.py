# import strawberry
#
# from database.MongoConection import db
# from embeddings.QdrantManager import obtenerEmbedingsDeCarroInsertadoEInsertar, \
#     obtenerEmbedingsDePiezaInsertadoEInsertar
# from models.Car import Carro, CarroInputUpdate
# from models.Conversation import Conversacion, Mensaje
# from typing import List, Optional
# from bson import ObjectId
#
# from models.Pieza import Pieza, PiezaInputUpdate
#
#
# @strawberry.type
# class Mutation:
#
#     #Insercion de un nuevo carro
#     @strawberry.mutation
#     async def insertar_carro(self, input: CarroInputUpdate) -> Carro:
#         nuevo_carro = {
#             "name": input.name,
#             "year": input.year,
#             "selling_price": input.selling_price,
#             "km_driven": input.km_driven,
#             "fuel": input.fuel,
#             "seller_type": input.seller_type,
#             "transmission": input.transmission,
#             "owner": input.owner
#         }
#         resultado = await db.carros.insert_one(nuevo_carro)
#         nuevo_id = str(resultado.inserted_id)
#         del nuevo_carro["_id"]
#         object_id = ObjectId(nuevo_id)
#         await obtenerEmbedingsDeCarroInsertadoEInsertar(object_id)
#
#         return Carro(_id=nuevo_id, **nuevo_carro)
#
#     @strawberry.mutation
#     async def actualizar_carro(self, id: str, input: CarroInputUpdate) -> Optional[Carro]:
#
#         update_data = {k: v for k, v in input.__dict__.items() if v is not None}
#
#         object_id = ObjectId(id)
#
#         resultado = await db.carros.update_one({"_id": object_id}, {"$set": update_data})
#
#         if resultado.matched_count == 0:
#             return None
#
#         carro_actualizado = await db.carros.find_one({"_id": object_id})
#
#         return Carro(**carro_actualizado)
#
#     #Insercion de una nueva pieza
#     @strawberry.mutation
#     async def insertar_pieza(self, input: PiezaInputUpdate) -> Pieza:
#         nueva_pieza = {
#             "tipo": input.tipo,
#             "modelo": input.modelo,
#             "precio": input.precio,
#             "uso": input.uso,
#             "cantidad": input.cantidad
#         }
#         resultado = await db.piezas.insert_one(nueva_pieza)
#
#         nuevo_id = str(resultado.inserted_id)
#
#         del nueva_pieza["_id"]
#
#         object_id = ObjectId(nuevo_id)
#        # await obtenerEmbedingsDePiezaInsertadoEInsertar(object_id)
#
#         return Pieza(_id=nuevo_id, **nueva_pieza)