#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

cd ./python/

echo -e "\nRunning FLY Graph tests and code coverage"
python3 -m tox -e cover
