#!/bin/bash

BASE_URL="http://localhost:8080"

curl -i -X POST "${BASE_URL}/api/order" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 5,
    "cartId": 123,
    "paymentMethod": "CreditCard",
    "shippingAddress": "123 Sample St"
}'
