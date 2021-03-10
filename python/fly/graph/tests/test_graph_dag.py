#! python3

'''
Graph DAG and topological sorting test
'''

import unittest
from unittest import main
from fly.graph.graph import Graph
from fly.graph.tests.test_graph import GraphTestCase

class GraphDAGTest(GraphTestCase):
    """
    """

    #graph = Graph() # TODO it has to be directed
    _TEST_TITLE = "GRAPH DAG AND TOPOLOGICAL SORTING TEST"

    def test_isDAG(self):
        """
        """
        pass

if __name__ == '__main__':
    main(verbosity=2)
