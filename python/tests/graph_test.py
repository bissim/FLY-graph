#! python3

'''
Dummy test for FLY graph functionalities
'''

# import os, sys
# sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

from fly.graph import Graph

graph = Graph()

nodes = ["a", "b", "c", "d", "e", "f"]

graph.addNodes(nodes)

graph.addEdge("a", "b")
graph.addEdge("a", "c")
graph.addEdge("b", "c")
graph.addEdge("b", "e")
graph.addEdge("c", "d")
graph.addEdge("d", "e")

print("\nUNDIRECTED GRAPH")
print("Undirected graph: {0}".format(graph))
print("Graph nodes: {0}".format(graph.nodeSet()))
print("Number of nodes: {0}".format(graph.numNodes()))
print("Node 'a' is in graph: {0}".format(graph.hasNode("a")))
print("Node 'g' is in graph: {0}".format(graph.hasNode("g")))
print("Graph edges: {0}".format(graph.edgeSet()))
print("Number of edges: {0}".format(graph.numEdges()))
print("Edge ('a', 'c') is in graph: {0}".format(graph.hasEdge("a", "c")))
print("Edge ('a', 'f') is in graph: {0}".format(graph.hasEdge("a", "f")))
print("Degree of node 'a': {0}".format(graph.nodeDegree("a")))
print("Degree of node 'f': {0}".format(graph.nodeDegree("f")))
print("Neighbourhood of 'a': {0}".format(graph.neighbourhood("a")))

# edge manipulation
edge = graph.getEdge("d", "e")
print("Edge: {0}".format(edge))
print ("Change edge target from 'e' to 'a'")
graph.setEdgeTarget(edge, "a")
print("Graph: {0}".format(graph))
edge = graph.getEdge("d", "a")
print("Edge: {0}".format(edge))
print("Change edge source from 'd' to 'c'")
graph.setEdgeSource(edge, "c")
print("Graph: {0}".format(graph))

print("\nWEIGHTED GRAPH")
wgraph = Graph(is_weighted=True)
wgraph.addNodes(nodes)
wgraph.removeNode("f")
wgraph.addEdge("a", "b")
wgraph.addEdge("a", "c")
wgraph.addEdge("b", "c")
wgraph.addEdge("b", "e")
wgraph.addEdge("c", "d")
wgraph.addEdge("d", "e")
wgraph.addEdge("a", "e")
wgraph.addEdge("b", "d")
wgraph.setEdgeWeight("a", "b", 2.0)
print("Weight of edge ('a', 'b'): {0}".format(wgraph.getEdgeWeight("a", "b")))
wgraph.setEdgeWeight("a", "c", 3.0)
wgraph.setEdgeWeight("b", "c", 1.0)
wgraph.setEdgeWeight("b", "e", 4.0)
wgraph.setEdgeWeight("c", "d", 2.0)
wgraph.setEdgeWeight("d", "e", 5.0)
wgraph.setEdgeWeight("a", "e", 2.0)
wgraph.setEdgeWeight("b", "d", 3.0)
print("Weighted graph: {0}".format(wgraph))

print("\nDIRECTED GRAPH")
digraph = Graph(is_directed=True)
digraph.addNodes(nodes)
digraph.removeNode("f")
digraph.addEdge("a", "b")
digraph.addEdge("a", "c")
digraph.addEdge("b", "c")
digraph.addEdge("b", "e")
digraph.addEdge("c", "d")
digraph.addEdge("d", "e")
print("Directed graph: {0}".format(digraph))

print("In degree of node 'b': {0}".format(digraph.nodeInDegree("b")))
print("Out degree of node 'b': {0}".format(digraph.nodeOutDegree("b")))
print("In star of 'b': {0}".format(digraph.nodeInEdges("b")))
print("Out star of 'b': {0}".format(digraph.nodeOutEdges("b")))

print("\nDIRECTED WEIGHTED GRAPH")
wdgraph = Graph(is_directed=True, is_weighted=True)
wdgraph.addNodes(nodes)
wdgraph.removeNode("f")
wdgraph.addEdge("a", "b")
wdgraph.addEdge("a", "c")
wdgraph.addEdge("b", "c")
wdgraph.addEdge("b", "e")
wdgraph.addEdge("c", "d")
wdgraph.addEdge("d", "e")
wdgraph.addEdge("a", "e")
wdgraph.addEdge("b", "d")
wdgraph.setEdgeWeight("a", "b", 2.0)
wdgraph.setEdgeWeight("a", "c", 3.0)
wdgraph.setEdgeWeight("b", "c", 1.0)
wdgraph.setEdgeWeight("b", "e", 4.0)
wdgraph.setEdgeWeight("c", "d", 2.0)
wdgraph.setEdgeWeight("d", "e", 5.0)
wdgraph.setEdgeWeight("a", "e", 2.0)
wdgraph.setEdgeWeight("b", "d", 3.0)
print("Weighted directed graph: {0}".format(wdgraph))

#print("\nIMPORT/EXPORT")
#data_path = "./../../../../../data/"
#igraph = Graph.importGraph(data_path + "unweighted.edgelist", ' ')
#idgraph = Graph.importGraph(data_path + "unweighted.edgelist", ' ', is_directed=True)
#iwgraph = Graph.importGraph(data_path + "weighted.edgelist", ' ', is_weighted=True)
#iwdgraph = Graph.importGraph(data_path + "weighted.edgelist", ' ', is_directed=True, is_weighted=True)
#print("Undirected graph: {0}".format(igraph))
#print("Directed graph: {0}".format(idgraph))
#print("Weighted graph: {0}".format(iwgraph))
#print("Directed weighted graph: {0}".format(iwdgraph))
#del igraph
#del idgraph
#del iwgraph
#del iwdgraph
#Graph.exportGraph(graph, data_path + "graph.py.edgelist", ' ')
#Graph.exportGraph(digraph, data_path + "digraph.py.edgelist", ' ', is_directed=True)
#Graph.exportGraph(wgraph, data_path + "wgraph.py.edgelist", ' ', is_weighted=True)
#Graph.exportGraph(wdgraph, data_path + "wdgraph.py.edgelist", ' ', is_directed=True, is_weighted=True)

print("\nBFS & DFS")
root_node = "a"
print("BFS nodes order: {0}".format(graph.bfsNodes(root_node)))
print("BFS edges: {0}".format(graph.bfsEdges(root_node)))
print("BFS tree: {0}".format(graph.bfsTree(root_node)))

print("DFS nodes order: {0}".format(graph.dfsNodes(root_node)))
print("DFS edges: {0}".format(graph.dfsEdges(root_node)))
print("DFS tree: {0}".format(graph.dfsTree(root_node)))

print("\nCONNECTIVITY")
graph.addNode("g")
graph.addEdge("f", "g")
print("Graph: {0}".format(graph))
print("Graph is connected: {0}".format(graph.isConnected()))
print("Number of connected components: {0}".format(graph.numberConnectedComponents()))
print("Connected components: [{0}]".format(graph.connectedComponents()))
print("Connected component of 'a': {0}".format(graph.nodeConnectedComponent("a")))
print("Connected component of 'f': {0}".format(graph.nodeConnectedComponent("f")))
subgraphs = graph.connectedSubgraphs()
i = 0
for subgraph in subgraphs:
    print("Subgraph {0}: {1}".format(i, subgraph))
    i += 1
del subgraphs
#print("Connected subgraphs: {0}".format(graph.connected_subgraphs()))
print("Removing 'f' and 'g'...")
graph.removeNode("f")
graph.removeNode("g")
print("Graph: {0}".format(graph))
print("Graph is connected: {0}".format(graph.isConnected()))
print("Number of connected components: {0}".format(graph.numberConnectedComponents()))
print("---")
digraph.addEdge("d", "a")
digraph.addNode("f")
digraph.addEdge("e", "f")
digraph.addEdge("f", "e")
print("Directed graph: {0}".format(digraph))
print("Digraph is strongly connected: {0}".format(digraph.isStronglyConnected()))
print("Connected components: [{0}]".format(digraph.stronglyConnectedComponents()))
subgraphs = digraph.stronglyConnectedSubgraphs()
i = 0
for subgraph in subgraphs:
    print("Strong subgraph {0}: {1}".format(i, subgraph))
    i += 1
del subgraphs
print("Removing (a, c) and adding (c, a) and (e, c)...")
digraph.removeEdge("a", "c")
digraph.addEdge("c", "a")
digraph.addEdge("e", "c")
print("Directed graph: {0}".format(digraph))
print("Digraph is strongly connected: {0}".format(digraph.isStronglyConnected()))


print("\nDAG & TOPOLOGICAL SORT")
digraph.removeNode("f")
digraph.removeEdge("d", "a")
print("Digraph: {0}".format(digraph))
print("Digraph is DAG: {0}".format(digraph.isDAG()))
print("Removing edges (c, a) and (e, c)...")
digraph.removeEdge("c", "a")
digraph.removeEdge("e", "c")
print("Digraph: {0}".format(digraph))
print("Digraph is DAG: {0}".format(digraph.isDAG()))
print("Topological sorting: {0}".format(digraph.topologicalSort()))

print("\nMINIMUM SPANNING TREE")
print("MST of weigted graph: {0}".format(wgraph.getMST()))
