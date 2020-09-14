#! python3

'''
Graph manipulation test
'''

import unittest
from unittest import TestCase, main
from fly.graph.graph import Graph

class GraphManipulationTest(TestCase):
    """
    """

    graph = Graph()
    _INITIAL_NODES = ['a', 'b', 'c', 'd', 'e', 'f']
    _TEST_TITLE = "GRAPH MANIPULATION TEST"

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

    def test_clear(self):
        """
        """
        self.assertNotEqual(0, self.graph.numEdges())
        self.graph.clear()
        self.assertEqual(0, self.graph.numEdges())

    def test_addNode(self):
        """
        """
        node = 'g'
        self.assertFalse(self.graph.hasNode(node))
        self.assertEqual(len(self._INITIAL_NODES), self.graph.numNodes())
        self.graph.addNode(node)
        self.assertEqual(len(self._INITIAL_NODES) + 1, self.graph.numNodes())
        self.assertTrue(self.graph.hasNode(node))

    def test_addNodes(self):
        """
        """
        nodes = ['g', 'h', 'i']
        for i in range(0, len(nodes) - 1):
            self.assertFalse(self.graph.hasNode(nodes[i]))
        self.assertEqual(len(self._INITIAL_NODES), self.graph.numNodes())
        self.graph.addNodes(nodes)
        self.assertEqual(len(self._INITIAL_NODES) + len(nodes), self.graph.numNodes())
        for i in range(0, len(nodes) - 1):
            self.assertTrue(self.graph.hasNode(nodes[i]))

    def test_nodeDegree(self):
        """
        """
        self.assertEqual(2, self.graph.nodeDegree("a"))
        self.assertEqual(3, self.graph.nodeDegree("b"))
        self.assertEqual(3, self.graph.nodeDegree("c"))
        self.assertEqual(1, self.graph.nodeDegree("d"))
        self.assertEqual(1, self.graph.nodeDegree("e"))
        self.assertEqual(0, self.graph.nodeDegree("f"))

    def test_nodeInDegree(self):
        """
        """
        self.assertEqual(2, self.graph.nodeDegree("a"))
        self.assertEqual(3, self.graph.nodeDegree("b"))
        self.assertEqual(3, self.graph.nodeDegree("c"))
        self.assertEqual(1, self.graph.nodeDegree("d"))
        self.assertEqual(1, self.graph.nodeDegree("e"))
        self.assertEqual(0, self.graph.nodeDegree("f"))

    def test_nodeOutDegree(self):
        """
        """
        self.assertEqual(2, self.graph.nodeDegree("a"))
        self.assertEqual(3, self.graph.nodeDegree("b"))
        self.assertEqual(3, self.graph.nodeDegree("c"))
        self.assertEqual(1, self.graph.nodeDegree("d"))
        self.assertEqual(1, self.graph.nodeDegree("e"))
        self.assertEqual(0, self.graph.nodeDegree("f"))

    def test_neighbourhood(self):
        """
        """
        pass

    def test_nodeInEdges(self):
        """
        """
        pass

    def test_nodeOutEdges(self):
        """
        """
        pass

    def test_nodeSet(self):
        """
        """
        pass

    def test_numNodes(self):
        """
        """
        pass

    def test_removeNode(self):
        """
        """
        pass

    def test_hasNode(self):
        """
        """
        pass

    def test_addEdge(self):
        """
        """
        pass

    def test_getEdge(self):
        """
        """
        pass

    def test_edgeSet(self):
        """
        """
        pass

    def test_numEdges(self):
        """
        """
        pass

    def test_getEdgeSource(self):
        """
        """
        pass

    def test_setEdgeSource(self):
        """
        """
        pass

    def test_getEdgeTarget(self):
        """
        """
        pass

    def test_setEdgeTarget(self):
        """
        """
        pass

    def test_getEdgeWeight(self):
        """
        """
        pass

    def test_setEdgeWeight(self):
        """
        """
        pass

    def test_removeEdge(self):
        """
        """
        pass

    def test_hasEdge(self):
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
