#!/bin/bash

BASE_URL="http://localhost:8080"

curl -i -X GET "${BASE_URL}/api/authors"
