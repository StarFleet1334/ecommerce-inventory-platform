#!/bin/bash

# Bash script to download and start Zipkin
# Usage: ./start-zipkin.sh

echo "Starting Zipkin setup..."

if [ -f "zipkin.jar" ]; then
    echo "zipkin.jar found, skipping download..."
else
    echo "Downloading Zipkin..."

    if command -v curl &> /dev/null; then
        curl -sSL https://zipkin.io/quickstart.sh | bash -s
    elif command -v wget &> /dev/null; then
        wget -qO- https://zipkin.io/quickstart.sh | bash -s
    else
        echo "Error: Neither curl nor wget is available!"
        exit 1
    fi

    echo "Download completed!"
fi

if ! command -v java &> /dev/null; then
    echo "Java not found! Please install Java 8 or higher."
    exit 1
fi

echo "Java found: $(java -version 2>&1 | head -n 1)"

echo "Starting Zipkin server..."
echo "Zipkin will be available at: http://localhost:9411"
echo "Press Ctrl+C to stop Zipkin"
echo ""

java -jar zipkin.jar