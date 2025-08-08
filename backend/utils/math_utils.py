import math


def convert_to_radians(degrees: float) -> float:
    """Convert degrees to radians"""
    return math.radians(degrees)


def parse_coordinates(coord_str: str) -> tuple[float, float]:
    """
    Parse coordinate string in format "lat,lon" to tuple
    Raises ValueError if a format is invalid
    """
    try:
        lat, lon = map(float, coord_str.split(","))
        return lat, lon
    except ValueError as e:
        raise ValueError("Invalid coordinate format. Expected 'latitude,longitude'") from e
