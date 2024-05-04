#!/bin/bash

# 1. Build the Docker Image
docker build -t araxah/webapp02:0.0.3 -f ./docker/Dockerfile.dockerfile .

# 2. Send the image to Docker Hub
docker push araxah/webapp02:0.0.3

# 3. Launch the docker compose
docker-compose -f docker/docker-compose.yml up --build
