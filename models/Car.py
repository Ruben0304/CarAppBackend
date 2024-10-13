import strawberry


# Tipo para los detalles de los carros
@strawberry.type
class Carro:
    name: str
    year: int
    selling_price: int
    km_driven: int
    fuel: str
    seller_type: str
    transmission: str
    owner: str
