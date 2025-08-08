from abc import ABC, abstractmethod
import math

from config.settings import AVERAGE_SPEED_KMH, EARTH_RADIUS_KM
from models.coordinates import Coordinates


class DistanceCalculator(ABC):
    """Abstract base class for distance calculation strategies"""

    @abstractmethod
    def calculate(self, origin: Coordinates, dest: Coordinates) -> dict[str, float]:
        """Calculate distance and duration between two points"""
        pass


class HaversineCalculator(DistanceCalculator):
    """Calculate straight-line distance using Haversine formula"""

    def calculate(self, origin: Coordinates, dest: Coordinates) -> dict[str, float]:
        """
        Calculate straight-line distance using Haversine formula
        Returns dictionary with distance_km and estimated duration_s
        """
        dlat = math.radians(dest.latitude - origin.latitude)
        dlon = math.radians(dest.longitude - origin.longitude)

        lat1 = math.radians(origin.latitude)
        lat2 = math.radians(dest.latitude)

        a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2

        distance_km = 2 * EARTH_RADIUS_KM * math.asin(math.sqrt(a))
        duration_s = (distance_km / AVERAGE_SPEED_KMH) * 3600

        return {"distance_km": distance_km, "duration_s": duration_s}
