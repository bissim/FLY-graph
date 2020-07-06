#! /usr/bin/env python3

import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="fly-graph",
    version="0.0.1-dev+20200706",
    author="Simone Bisogno",
    author_email="s.bisogno90@gmail.com",
    description="A graph library for FLY language, written in Python",
    license='MIT',
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/bissim/FLY-graph",
    packages=setuptools.find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Development Status :: 4 - Beta",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Other",
        "Topic :: Software Development :: Libraries :: Python Modules",
    ],
    keywords='FLY graph management',
    install_requires=['networkx'],
    python_requires='~=3.6, !=3.7',
    project_urls={
        'Source': 'https://github.com/bissim/FLY-graph',
        'Tracker': 'https://github.com/bissim/FLY-graph/issues'
    },
)
