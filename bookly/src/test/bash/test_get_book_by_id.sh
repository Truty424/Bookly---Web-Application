#!/bin/bash

BASE_URL="http://localhost:8080"
BOOK_ID="1"

curl -i -X GET "${BASE_URL}/api/book?id=${BOOK_ID}"
