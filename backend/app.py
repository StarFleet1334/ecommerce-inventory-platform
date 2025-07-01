from flask import Flask, request, jsonify
from models.coordinates import Coordinates
from services.route_service import RouteService

app = Flask(__name__)
route_service = RouteService()

@app.route('/calculate', methods=['GET'])
def calculate_route():
    print(f"Received request with parameters: {request.args}")
    try:
        origin_lat = float(request.args.get('originLat'))
        origin_lon = float(request.args.get('originLon'))
        dest_lat = float(request.args.get('destLat'))
        dest_lon = float(request.args.get('destLon'))

        origin = Coordinates(latitude=origin_lat, longitude=origin_lon)
        dest = Coordinates(latitude=dest_lat, longitude=dest_lon)

        result = route_service.get_distance_and_eta(origin, dest)

        response = {
            "distanceKm": result['distance_km'],
            "durationSeconds": result['duration_s']
        }

        return jsonify(response)

    except ValueError as e:
        return jsonify({"error": "Invalid coordinates provided"}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)