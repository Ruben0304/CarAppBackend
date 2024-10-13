import strawberry
from typing import List


# Tipo para los mensajes dentro de una conversación
@strawberry.type
class Mensaje:
    autor: str
    mensaje: str


# Tipo para una conversación entre un usuario y un mecánico
@strawberry.type
class Conversacion:
    id_mecanico: str
    id_usuario: str
    conversation: List[Mensaje]
