#! python3

'''
Graph metrics test
'''

import unittest
from unittest import TestCase, main
from fly.graph.graph import Graph

class GraphMetricsTest(TestCase):
    """
    """

    graph = Graph()
    _INITIAL_NODES = ['a', 'b', 'c', 'd', 'e', 'f']
    _TEST_TITLE = "GRAPH METRICS TEST"

    @classmethod
    def setUpClass(cls):
        """
        """
        print(f"\n{cls._TEST_TITLE}")
        pass

    def setUp(self):
        """
        """
        self.graph.addNodes(self._INITIAL_NODES)
        self.graph.addEdge("a", "b")
        self.graph.addEdge("a", "c")
        self.graph.addEdge("b", "c")
        self.graph.addEdge("b", "e")
        self.graph.addEdge("c", "d")

    def test_getAverageClusteringCoefficient(self):
        """
        """
        pass

    def tearDown(self):
        """
        """
        num_nodes = self.graph.numNodes()
        if num_nodes is not 0:
            self.graph.clear()

    @classmethod
    def tearDownClass(cls):
        """
        """
        pass

if __name__ == '__main__':
    main(verbosity=2)