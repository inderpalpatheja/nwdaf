

#!/bin/bash

default='1'
value=${1:-$default}

echo "value: [$value]"

export NWDAF_VER=$value

./mvnw clean
./mvnw compile
./mvnw package
