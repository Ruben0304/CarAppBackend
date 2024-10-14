from typing import Optional

import strawberry


# Tipo para los detalles de los carros
@strawberry.type
class Carro:
    _id: Optional[str]  # Aquí añadimos el campo id para MongoDB _id
    name: str
    year: int
    selling_price: int
    km_driven: int
    fuel: str
    seller_type: str
    transmission: str
    owner: str

@strawberry.input
class CarroInputUpdate:
    name: Optional[str] = None
    year: Optional[int] = None
    selling_price: Optional[int] = None
    km_driven: Optional[int] = None
    fuel: Optional[str] = None
    seller_type: Optional[str] = None
    transmission: Optional[str] = None
    owner: Optional[str] = None