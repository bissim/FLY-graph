#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

cd ./java/
mvn -T $(nproc) clean package
