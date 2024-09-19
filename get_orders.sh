#!/bin/bash

curl -X GET http://localhost:8085/orders | jq .
