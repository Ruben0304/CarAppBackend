import strawberry
from typing import List, Optional


# Tipo para los mensajes dentro de una conversación
@strawberry.type
class Mensaje:
    autor: str
    mensaje: str


# Tipo para una conversación entre un usuario y un mecánico
@strawberry.type
class Conversacion:
    _id: Optional[str]  # Aquí añadimos el campo id para MongoDB _id
    id_mecanico: str
    id_usuario: str
    conversation: List[Mensaje]
