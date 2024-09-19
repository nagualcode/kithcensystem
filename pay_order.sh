#!/bin/bash
if [[ -z "$1" ]]; then
echo "Usage: $0 OrderID"
exit 1
fi

curl -X PUT "http://localhost:8082/payments/$1?status=paid"
