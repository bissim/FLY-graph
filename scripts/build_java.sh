#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

cd ./java/

echo -e "\nRunning FLY Graph tests and code coverage"
mvn -T $(nproc) clean package
