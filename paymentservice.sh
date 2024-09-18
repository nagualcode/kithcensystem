#!/bin/bash

cd paymentservice || exit 1
mvn clean install || exit 1
cd .. || exit 1
docker compose up -d --build paymentservice


