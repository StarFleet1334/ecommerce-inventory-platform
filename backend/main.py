import argparse
import sys
from typing import Tuple

from models.coordinates import Coordinates
from services import RouteService
from utils.math_utils import parse_coordinates

def create_argument_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description='Calculate route distance and ETA')
    parser.add_argument('--origin', type=str, help='Origin coordinates (lat,lon)')
    parser.add_argument('--dest', type=str, help='Destination coordinates (lat,lon)')
    return parser

def get_example_coordinates() -> Tuple[Coordinates, Coordinates]:
    """Return example coordinates for NYC and LA"""
    nyc = Coordinates(40.7128, -74.0060)  # New York City
    la = Coordinates(34.0522, -118.2437)  # Los Angeles
    return nyc, la

def main():
    parser = create_argument_parser()
    args = parser.parse_args()

    try:
        if args.origin and args.dest:
            origin = Coordinates(*parse_coordinates(args.origin))
            dest = Coordinates(*parse_coordinates(args.dest))
        else:
            print("No coordinates provided, using example NYC to LA route")
            origin, dest = get_example_coordinates()

        route_service = RouteService()
        info = route_service.get_distance_and_eta(origin, dest)

        print(f"Origin     : {origin}")
        print(f"Destination: {dest}")
        print(f"Distance   : {info['distance_km']:.1f} km")
        print(f"ETA        : {info['eta']}")

    except ValueError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"Unexpected error: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()