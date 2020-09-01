#! python3
# -*- coding: utf-8 -*-

# Credits: github.com/navdeep-G/setup.py

# Note: To use the 'upload' functionality of this file, you must:
#   $ pipenv install twine --dev
"""
setup.py for human
"""

import io
import os
import sys
from shutil import rmtree

from setuptools import find_namespace_packages, setup, Command

# Package meta-data
NAME = 'fly-graph'
DESCRIPTION = 'A graph library for FLY language, written in Python'
URL = 'https://github.com/bissim/FLY-graph'
EMAIL = 's.bisogno90@gmail.com'
AUTHOR = 'Simone Bisogno'
REQUIRES_PYTHON = '~=3.6'

# Required packages
REQUIRED = [
    'networkx'
]

# Optional packages
EXTRAS = {}

# The rest you shouldn't have to touch too much :)
# ------------------------------------------------
# Except, perhaps the License and Trove Classifiers!
# If you do change the License, remember to change the Trove Classifier for that!

HERE = os.path.abspath(os.path.dirname(__file__))

# Import the README and use it as the long-description
# Note: this will only work if 'README.md' is present in your MANIFEST.in file!
try:
    with io.open(os.path.join(HERE, 'README.md'), encoding='utf-8') as f:
        LONG_DESCRIPTION = '\n' + f.read()
except FileNotFoundError:
    LONG_DESCRIPTION = DESCRIPTION
f.close()
del f

PARENT = os.path.abspath(os.path.dirname(HERE))

# Import the VERSION and use it as the version
with io.open(os.path.join(PARENT, 'VERSION'), encoding='utf-8') as f:
    VERSION = f.readline().rstrip('\n')
f.close()
del f


class UploadCommand(Command):
    """Support setup.py upload."""

    description = 'Build and publish the package.'
    user_options = []

    @staticmethod
    def status(status):
        """Prints things in bold."""
        print('\033[1m{0}\033[0m'.format(status))

    def initialize_options(self):
        """Initialize Options"""
        pass

    def finalize_options(self):
        """Finalize Options"""
        pass

    def run(self):
        """Cleanup, build, upload, and git tag"""
        try:
            self.status('Removing previous builds…')
            rmtree(os.path.join(HERE, 'dist'))
        except OSError:
            pass

        self.status('Building Source and Wheel (universal) distribution…')
        os.system(
            '{0} setup.py sdist bdist_wheel --universal'.format(sys.executable)
        )

        self.status('Uploading the package to PyPI via Twine…')
        os.system('twine upload dist/*')

        self.status('Pushing git tags…')
        os.system('git tag {0}'.format(VERSION))
        os.system('git push --tags')

        sys.exit()


# Where the magic happens:
setup(
    name=NAME,
    version=VERSION,
    description=DESCRIPTION,
    long_description=LONG_DESCRIPTION,
    long_description_content_type="text/markdown",
    author=AUTHOR,
    author_email=EMAIL,
    python_requires=REQUIRES_PYTHON,
    url=URL,
    packages=find_namespace_packages(
        exclude=["tests", "*.tests", "*.tests.*", "tests.*"]
    ),
    install_requires=REQUIRED,
    extras_require=EXTRAS,
    include_package_data=True,
    license='MIT',
    classifiers=[
        # Trove classifiers
        "Programming Language :: Python",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.6",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Development Status :: 6 - Mature",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Other",
        "Topic :: Software Development :: Libraries :: Python Modules",
    ],
    # $ setup.py publish support.
    cmdclass={
        'upload': UploadCommand,
    },
    keywords='FLY graph management',
    project_urls={
        'Source': 'https://github.com/bissim/FLY-graph',
        'Tracker': 'https://github.com/bissim/FLY-graph/issues'
    },
)
