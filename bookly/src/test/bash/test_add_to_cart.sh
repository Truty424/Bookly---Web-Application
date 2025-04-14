#!/bin/bash

BASE_URL="http://localhost:8080"
CART_ID=123
BOOK_ID=1

curl -i -X POST "${BASE_URL}/api/cart/add?cart_Id=${CART_ID}&book_Id=${BOOK_ID}"
