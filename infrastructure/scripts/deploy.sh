#!/bin/bash

# Check if service name is passed
if [ -z "$1" ]; then
  echo "Usage: $0 <service-name>"
  exit 1
fi

SERVICE_NAME="$1"

# Run the commands
docker-compose build "$SERVICE_NAME"
docker-compose up -d "$SERVICE_NAME"
