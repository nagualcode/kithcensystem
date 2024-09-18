#!/bin/bash

cd orderservice || exit 1
mvn clean install || exit 1
cd .. || exit 1
docker compose up -d --build orderservice


