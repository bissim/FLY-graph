#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

pwd
cd ./python/
pwd
python3 -m pip install --upgrade setuptools wheel
python3 setup.py sdist bdist_wheel
ls -la dist/
python3 -m pip install -r fly_graph.egg-info/requires.txt
python3 -m fly.graph.tests
python3 -m pip install coverage
python3 -m coverage report -i fly/graph/*
python3 -m coverage xml -i fly/graph/*
