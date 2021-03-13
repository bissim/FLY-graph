#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

pwd
cd ./python/
pwd

echo -e "\nBuilding FLY Graph project"
python3 -m pip install --upgrade pip setuptools build wheel tox
python3 -m build
ls -la dist/

echo -e "\nRunning FLY Graph tests and code coverage"
python3 -m tox -e cover
ls -la
