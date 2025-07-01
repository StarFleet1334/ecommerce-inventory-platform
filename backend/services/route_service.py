from datetime import timedelta
from typing import Dict, Union
import logging

from models.coordinates import Coordinates
from services.distance_calculator import (
    HaversineCalculator,
    DistanceCalculator
)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class RouteService:
    """Service for calculating routes between two points"""

    def __init__(self):
        self.calculator: DistanceCalculator = HaversineCalculator()

    def get_distance_and_eta(
            self,
            origin: Coordinates,
            dest: Coordinates
    ) -> Dict[str, Union[float, timedelta]]:
        """Calculate distance and estimated time of arrival between two points"""
        result = self.calculator.calculate(origin, dest)
        result["eta"] = timedelta(seconds=round(result["duration_s"]))
        return result