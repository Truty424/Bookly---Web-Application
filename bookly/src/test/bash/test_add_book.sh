#!/bin/bash

BASE_URL="http://localhost:8080"

curl -i -X POST "${BASE_URL}/api/book" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sample Book",
    "author_id": 1,
    "publisher_id": 2,
    "price": 19.99,
    "language": "English",
    "isbn": "1234567890"
}'
