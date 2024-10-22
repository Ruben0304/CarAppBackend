import strawberry
from embeddings.EmbeddingGenerator import embed_queries
from embeddings.QdrantManager import qdrant_client
from models.Car import Carro, CarroInputUpdate
from models.Conversation import Conversacion, Mensaje
from typing import List, Optional

from models.Pieza import Pieza


@strawberry.type
class Query:

 @strawberry.field
 async def piezas(self, info, tipo: Optional[str] = None, cantidad: Optional[int] = None) -> List[Pieza]:
    # Obtener la base de datos del contexto
    db = info.context["db"]

    query = {}
    if tipo:
        query["tipo"] = {"$regex": tipo, "$options": "i"}

    piezas_data = await db.piezas.find(query).to_list(length=cantidad)
    return [Pieza(**p) for p in piezas_data]


# Consulta para obtener conversaciones, con tres parámetros:
# 1. `id_mecanico` (opcional): permite filtrar las conversaciones por el mecánico.
# 2. `cantidad` (opcional): limita la cantidad de conversaciones a devolver.
# Si no se pasan parámetros, se devuelven todas las conversaciones.
@strawberry.field
async def conversaciones(
        self,
        info,
        id_mecanico: Optional[str] = None,
        cantidad: Optional[int] = None
) -> List[Conversacion]:
    db = info.context["db"]

    filtro = {}
    if id_mecanico:
        filtro['id_mecanico'] = id_mecanico

    conversaciones_data = await db.conversations.find(filtro).to_list(length=cantidad)

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


@strawberry.field
async def carros(
        self,
        info,
        cantidad: Optional[int] = None
) -> List[Carro]:
    db = info.context["db"]

    query = {}
    carros_data = await db.carros.find(query).to_list(length=cantidad)
    return [Carro(**car) for car in carros_data]


@strawberry.field
async def search_carros(
        self,
        info,
        query: str
) -> List[Carro]:
    db = info.context["db"]

    # Vectorizar el texto de la consulta
    vector_query = await embed_queries([query])
    vector = vector_query.float_[0]

    # Búsqueda en Qdrant
    search_result = qdrant_client.search(
        collection_name="mi_coleccion",
        query_vector=vector,
        limit=10
    )

    # Obtener IDs y buscar en MongoDB
    ids = [result.payload["original_id"] for result in search_result]
    carros_data = await db.carros.find({"_id": {"$in": ids}}).to_list(length=10)
    return [Carro(**car) for car in carros_data]


@strawberry.field
async def search_piezas(
        self,
        info,
        query: str
) -> List[Pieza]:
    db = info.context["db"]

    # Vectorizar el texto de la consulta
    vector_query = await embed_queries([query])
    vector = vector_query.float_[0]

    # Búsqueda en Qdrant
    search_result = qdrant_client.search(
        collection_name="mi_coleccion",
        query_vector=vector,
        limit=10
    )

    # Obtener IDs y buscar en MongoDB
    ids = [result.payload["original_id"] for result in search_result]
    piezas_data = await db.piezas.find({"_id": {"$in": ids}}).to_list(length=10)
    return [Pieza(**p) for p in piezas_data]
