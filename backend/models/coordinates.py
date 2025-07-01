from dataclasses import dataclass
from typing import Tuple

@dataclass
class Coordinates:
    latitude: float
    longitude: float

    def to_tuple(self) -> Tuple[float, float]:
        """Convert coordinates to a tuple format (latitude, longitude)"""
        return (self.latitude, self.longitude)

    @classmethod
    def from_tuple(cls, coords: Tuple[float, float]) -> 'Coordinates':
        """Create Coordinates instance from a tuple (latitude, longitude)"""
        return cls(latitude=coords[0], longitude=coords[1])

    def __str__(self) -> str:
        return f"({self.latitude}, {self.longitude})"