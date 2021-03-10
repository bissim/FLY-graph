#! python3

'''
Graph test class
'''

import unittest
from unittest import TestCase
from fly.graph.graph import Graph

class GraphTestCase(TestCase):
    """
    """

    _TEST_TITLE = "GRAPH TEST"
    _INITIAL_NODES = ['a', 'b', 'c', 'd', 'e', 'f']
    graph = Graph()

    @classmethod
    def setUpClass(cls):
        """
        """
        print(f"\n{cls._TEST_TITLE}")

    def setUp(self):
        """
        """
        self.graph.addNodes(self._INITIAL_NODES)
        self.graph.addEdge("a", "b")
        self.graph.addEdge("a", "c")
        self.graph.addEdge("b", "c")
        self.graph.addEdge("b", "e")
        self.graph.addEdge("c", "d")

    def tearDown(self):
        """
        """
        num_nodes = self.graph.numNodes()
        if num_nodes != 0:
            self.graph.clear()

    @classmethod
    def tearDownClass(cls):
        """
        """
        pass
