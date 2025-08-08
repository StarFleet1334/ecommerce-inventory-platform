from datetime import timedelta
import logging

from models.coordinates import Coordinates

from services.distance_calculator import DistanceCalculator, HaversineCalculator

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class RouteService:
    """Service for calculating routes between two points"""

    def __init__(self):
        self.calculator: DistanceCalculator = HaversineCalculator()

    def get_distance_and_eta(
        self, origin: Coordinates, dest: Coordinates
    ) -> dict[str, float | timedelta]:
        """Calculate distance and estimated time of arrival between two points"""
        result = self.calculator.calculate(origin, dest)
        result["eta"] = timedelta(seconds=round(result["duration_s"]))
        return result
