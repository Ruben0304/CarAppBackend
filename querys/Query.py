import strawberry
from database.MongoConection import  mongodb
from database.ObtenerDatosParaQdrant import db
from embeddings.EmbeddingGenerator import embed_queries
from embeddings.QdrantManager import qdrant_client
from models.Car import Carro, CarroInputUpdate
from models.Conversation import Conversacion, Mensaje
from typing import List, Optional

from models.Pieza import Pieza


@strawberry.type
class Query:

    @strawberry.field
    async def conversaciones(self, id_mecanico: Optional[str] = None, cantidad: Optional[int] = None) -> List[Conversacion]:
     # Recuperamos los datos de la colección conversations en MongoDB
     filtro = {}
     if id_mecanico:
        filtro['id_mecanico'] = id_mecanico
     conversaciones_data = await mongodb.conversations.find(filtro).to_list(length=cantidad)
     mongodb.close()
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
    async def carros(self, cantidad: Optional[int] = None ) -> List[Carro]:
        # Si se pasa un precio máximo, filtramos por carros cuyo precio de venta sea menor o igual.
        query = {}
        # Realizamos la consulta en MongoDB, aplicando el filtro si es necesario.
        carros_data = await db.carros.find(query).to_list(length=cantidad)

        # Convertimos los documentos de MongoDB a objetos del tipo Carro.
        return [Carro(**car) for car in carros_data]

    @strawberry.field
    async def piezas(self, tipo: Optional[str] = None, cantidad: Optional[int] = None) -> List[Pieza]:
        query = {}
        if tipo:
            query["tipo"] = {"$regex": tipo, "$options": "i"}

        # Realizamos la consulta en MongoDB, aplicando el filtro si es necesario.
        piezas_data = await db.piezas.find(query).to_list(length=cantidad)

        return [Pieza(**p) for p in piezas_data]

    #POR PROBAR AUN
    @strawberry.field
    async def search_carros(self, query: str) -> List[Carro]:
        # Paso 1: Vectorizar el texto de la consulta usando la función de la API de embedding
        vector_query = await embed_queries([query])  # La función espera una lista de textos, incluso si es solo uno

        # Extraer el vector desde el campo float_ (es una lista de listas, por lo que tomamos el primer vector)
        vector = vector_query.float_[0]

        # Paso 2: Enviar el vector a Qdrant para buscar los 10 puntos más cercanos
        search_result = qdrant_client.search(
            collection_name="mi_coleccion",  # Nombre de tu colección en Qdrant
            query_vector=vector,
            limit=10  # Limitar la búsqueda a los 10 puntos más cercanos
        )

        # Paso 3: Obtener los IDs de los puntos resultantes de Qdrant
        ids = [result.payload["original_id"] for result in search_result]  # Extraemos solo los IDs de los puntos

        # Paso 4: Usar los IDs obtenidos para consultar la base de datos MongoDB
        carros_data = await db.carros.find({"_id": {"$in": ids}}).to_list(length=10)

        # Paso 5: Convertir los documentos de MongoDB en objetos del tipo Carro y devolver la lista
        return [Carro(**car) for car in carros_data]

    @strawberry.field
    async def search_piezas(self, query: str) -> List[Pieza]:

        vector_query = await embed_queries([query])  # La función espera una lista de textos, incluso si es solo uno

        vector = vector_query.float_[0]


        search_result = qdrant_client.search(
            collection_name="mi_coleccion",  # Cambiar a la coleccion de piezas
            query_vector=vector,
            limit=10  # Limitar la búsqueda a los 10 puntos más cercanos
        )

        # Paso 3: Obtener los IDs de los puntos resultantes de Qdrant
        ids = [result.payload["original_id"] for result in search_result]  # Extraemos solo los IDs de los puntos

        # Paso 4: Usar los IDs obtenidos para consultar la base de datos MongoDB
        piezas_data = await db.piezas.find({"_id": {"$in": ids}}).to_list(length=10)

        # Paso 5: Convertir los documentos de MongoDB en objetos del tipo Carro y devolver la lista
        return [Pieza(**p) for p in piezas_data]


