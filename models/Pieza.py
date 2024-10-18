from typing import Optional

import strawberry

@strawberry.type
class Pieza:
    _id: Optional[str]
    name: Optional[str]