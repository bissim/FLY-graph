#! python3

'''
Unit tests for Graph class
'''

from unittest import TestLoader
from unittest.runner import TextTestRunner

if __name__ == '__main__':
    print("Running graph tests...")
    testsuite = TestLoader().discover('.', top_level_dir='.')
    TextTestRunner(verbosity=2).run(testsuite)
