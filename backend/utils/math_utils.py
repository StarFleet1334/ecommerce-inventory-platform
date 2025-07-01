
import math
from typing import Tuple

def convert_to_radians(degrees: float) -> float:
    """Convert degrees to radians"""
    return math.radians(degrees)

def parse_coordinates(coord_str: str) -> Tuple[float, float]:
    """
    Parse coordinate string in format "lat,lon" to tuple
    Raises ValueError if format is invalid
    """
    try:
        lat, lon = map(float, coord_str.split(','))
        return (lat, lon)
    except ValueError:
        raise ValueError("Invalid coordinate format. Expected 'latitude,longitude'")