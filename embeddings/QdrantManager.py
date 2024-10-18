from qdrant_client import QdrantClient
import uuid
from qdrant_client import QdrantClient
from qdrant_client.http import models
from bson import ObjectId
from embeddings.EmbeddingGenerator import embed_documents

import embeddings.EmbeddingGenerator
from database.ObtenerDatosParaQdrant import obtener_documentos, obtener_documento_carro, obtener_documento_pieza

qdrant_client = QdrantClient(
    url="https://8b479d4d-d506-4ec1-9e8d-0fa8bbafa172.europe-west3-0.gcp.cloud.qdrant.io:6333",
    api_key="DpAlOl0MQvDYcNwep9r9fLSQ2UYEjjE3zpJ1mLwCIuxMrujzN2PmPw",
)


# insertar vectores
def insert_documents(ids, vectors, coleccion_name):
    print(ids)
    print(vectors)
    # Verificar que tenemos datos para insertar

    # Crear la lista de PointStruct para la inserción
    points_to_insert = [
        models.PointStruct(
            id=str(uuid.uuid4()),  # Generamos un UUID como ID para Qdrant
            vector=vector,
            payload={"original_id": str(id)}  # Guardamos el ObjectId original como string en el payload
        ) for id, vector in zip(ids, vectors)
    ]

    # Verificar que tenemos puntos para insertar
    if not points_to_insert:
        raise ValueError("No hay puntos para insertar.")

    # Insertar los vectores
    qdrant_client.upsert(
        collection_name=coleccion_name,
        points=points_to_insert
    )

    print(f"Se insertaron {len(points_to_insert)} vectores correctamente.")


def insert_document(id, vectors, coleccion_name):
    print(id)
    print(vectors)
    # Verificar que tenemos datos para insertar

    # Crear la lista de PointStruct para la inserción
    points_to_insert = [
        models.PointStruct(
            id=str(uuid.uuid4()),  # Generamos un UUID como ID para Qdrant
            vector=vectors,
            payload={"original_id": str(id)}  # Guardamos el ObjectId original como string en el payload
        )
    ]

    # Verificar que tenemos puntos para insertar
    if not points_to_insert:
        raise ValueError("No hay puntos para insertar.")

    # Insertar los vectores
    qdrant_client.upsert(
        collection_name=coleccion_name,
        points=points_to_insert
    )

    print(f"Se insertaron {len(points_to_insert)} vectores correctamente.")





async def  obtenerEmbedingsDeDColeccionEInsertar():
     cars = obtener_documentos("carros")
     ids = cars[0]
     textos = cars[1]
     vectors = await embed_documents(textos).float_
     insert_documents(ids, vectors, "carros")

async def obtenerEmbedingsDeCarroInsertadoEInsertar(id: ObjectId):
    cars = await obtener_documento_carro("carros", id)  # Añadido await
    ids = cars[0]
    textos = cars[1]

    print(ids)
    print(textos)

    vectors = await embed_documents([textos])

    print(vectors)

    insert_document(ids, vectors.float_[0], "mi_coleccion")

async def obtenerEmbedingsDePiezaInsertadoEInsertar(id: ObjectId):
    pieza = await obtener_documento_pieza("piezas", id)  # Añadido await

    ids = pieza[0]
    textos = pieza[1]

    print(ids)
    print(textos)

    vectors = await embed_documents([textos])

    print(vectors)

    insert_document(ids, vectors.float_[0], "mi_coleccion") #CAMBIAR NOMBRE A COLECCION DE PIEZAS