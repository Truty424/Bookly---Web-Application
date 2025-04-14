#!/bin/bash

BASE_URL="http://localhost:8080"
USER_ID=5

curl -i -X GET "${BASE_URL}/api/order/user/${USER_ID}"