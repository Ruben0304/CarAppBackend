# Extraer todos los documentos de MongoDB
from bson import ObjectId

from database.MongoConection import db

textos = []
ids = []


def obtener_documentos(coleccion_name):
    documentos = db[coleccion_name].find().limit(96)
    for doc in documentos:

        name = doc.get("name", "")
        selling_price = doc.get("selling_price", "")
        km_driven = doc.get("km_driven", "")

        # Convertir los valores a string y concatenar
        texto_concatenado = f"{name} {selling_price} {km_driven}"
        ids.append(doc["_id"])

        if texto_concatenado.strip():  # Si el texto concatenado no está vacío
            textos.append(texto_concatenado)
            # # Vectorizar el documento
            # vector = await embed_documents([texto_concatenado])
        return [ids, textos]


async def obtener_documento_carro(coleccion_name, id: ObjectId):
    documento = await db[coleccion_name].find_one({"_id": id})  # Añadido await
    if documento is None:
        raise ValueError(f"No se encontró el documento con id {id}")

    name = documento["name"]
    selling_price = documento["selling_price"]
    km_driven = documento["km_driven"]

    # Convertir los valores a string y concatenar
    texto_concatenado = f"{name} {selling_price} {km_driven}"
    id = documento["_id"]

    return [id, texto_concatenado]
