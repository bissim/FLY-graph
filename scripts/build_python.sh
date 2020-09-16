#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

pwd
cd ./python/
pwd

echo -e "\nBuilding FLY Graph project"
python3 -m pip install --upgrade setuptools wheel
python3 setup.py sdist bdist_wheel
ls -la dist/

echo -e "\nRunning FLY Graph tests"
python3 -m pip install -r fly_graph.egg-info/requires.txt
python3 -m fly.graph.tests

echo -e "\nReport FLY Graph code coverage"
python3 -m pip install coverage
python3 -m coverage run fly/graph/graph.py --omit=examples,tests
python3 -m coverage report -i fly/graph/graph.py --omit=examples,tests
python3 -m coverage xml -i fly/graph/graph.py --omit=examples,tests
ls -la
