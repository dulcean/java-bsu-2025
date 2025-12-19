#!/bin/bash

set -e

echo "Initializing backend..."
./init_backend.sh

echo "Initializing frontend..."
./init_frontend.sh

echo "Building backend..."
cd backend
mvn clean package -DskipTests
cd ..

if docker compose version >/dev/null 2>&1; then
    echo "Starting docker compose (plugin)..."
    docker compose up --build
elif docker-compose version >/dev/null 2>&1; then
    echo "Starting docker-compose (legacy)..."
    docker-compose up --build
else
    echo "Docker Compose not found"
    exit 1
fi

