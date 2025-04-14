#!/bin/bash

BASE_URL="http://localhost:8080"

curl -i -X POST "${BASE_URL}/api/authors" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe"
}'
