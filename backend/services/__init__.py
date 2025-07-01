
from .distance_calculator import (
    DistanceCalculator,
    HaversineCalculator
)
from .route_service import RouteService

__all__ = [
    'DistanceCalculator',
    'HaversineCalculator',
    'RouteService',
    'create_route_service'
]

def create_route_service(use_backup_only: bool = False) -> RouteService:
    """
    Factory function to create a RouteService instance

    Args:
        use_backup_only: If True, creates a RouteService that only uses
                        HaversineCalculator (useful for testing or when
                        OSRM service is known to be unavailable)

    Returns:
        RouteService: Configured instance of RouteService
    """
    service = RouteService()

    if use_backup_only:
        service.primary_calculator = HaversineCalculator()
        service.backup_calculator = HaversineCalculator()

    return service