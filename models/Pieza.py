from typing import Optional

import strawberry

@strawberry.type
class Pieza:
    _id: Optional[str]
    tipo: Optional[str]
    modelo: Optional[str]
    precio: Optional[int]
    uso: Optional[str]
    cantidad: Optional[int]

@strawberry.input
class PiezaInputUpdate:
    _id: Optional[str] = None
    tipo: Optional[str] = None
    modelo: Optional[str] = None
    precio: Optional[int] = None
    uso: Optional[str] = None
    cantidad: Optional[int] = None