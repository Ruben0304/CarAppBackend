import strawberry

from database.MongoConection import db
from models.Car import Carro, CarroInputUpdate
from models.Conversation import Conversacion, Mensaje
from typing import List, Optional


@strawberry.type
class Query:

    # Consulta para obtener conversaciones, con tres parámetros:
    # 1. `id_mecanico` (opcional): permite filtrar las conversaciones por el mecánico.
    # 2. `cantidad` (opcional): limita la cantidad de conversaciones a devolver.
    # Si no se pasan parámetros, se devuelven todas las conversaciones.
    @strawberry.field
    async def conversaciones(self, id_mecanico: Optional[str] = None, cantidad: Optional[int] = None) -> List[Conversacion]:
        # Recuperamos los datos de la colección conversations en MongoDB
        filtro = {}
        if id_mecanico:
            filtro['id_mecanico'] = id_mecanico

        conversaciones_data = await db.conversations.find(filtro).to_list(length=cantidad)

        # Transformamos los diccionarios de 'conversation' en instancias de Mensaje
        conversaciones_list = []
        for conv in conversaciones_data:
            mensajes = [Mensaje(**msg) for msg in conv['conversation']]
            conversaciones_list.append(Conversacion(
                _id=conv['_id'],
                id_mecanico=conv['id_mecanico'],
                id_usuario=conv['id_usuario'],
                conversation=mensajes
            ))

        return conversaciones_list



    # Consulta para obtener carros, con dos parámetros:
    # 1. `precio_max` (opcional): filtra los carros cuyo precio de venta sea menor o igual a este valor.
    # 2. `cantidad` (opcional): limita la cantidad de carros a devolver.
    # Si no se pasan parámetros, se devuelven todos los carros.
    @strawberry.field
    async def carros(self, precio_max: Optional[int] = None, cantidad: Optional[int] = None,  nombre_marca: Optional[str] = None ) -> List[Carro]:
        # Si se pasa un precio máximo, filtramos por carros cuyo precio de venta sea menor o igual.
        query = {}
        if precio_max:
            query["selling_price"] = {"$lte": precio_max}

        if nombre_marca:
            query["name"] = {"$regex": nombre_marca, "$options": "i"}

        # Realizamos la consulta en MongoDB, aplicando el filtro si es necesario.
        carros_data = await db.carros.find(query).to_list(length=cantidad)

        # Convertimos los documentos de MongoDB a objetos del tipo Carro.
        return [Carro(**car) for car in carros_data]

