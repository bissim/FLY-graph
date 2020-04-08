#! /usr/bin/env python3

import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

from fly.graph import Graph

graph = Graph()

nodes = ["a", "b", "c", "d", "e", "f"]

graph.add_nodes(nodes)

graph.add_edge("a", "b")
graph.add_edge("a", "c")
graph.add_edge("b", "c")
graph.add_edge("b", "e")
graph.add_edge("c", "d")
graph.add_edge("d", "e")

print("\nUNDIRECTED GRAPH")
print("Undirected graph: {0}".format(graph))
print("Graph nodes: {0}".format(graph.node_set()))
print("Number of nodes: {0}".format(graph.num_nodes()))
print("Node 'a' is in graph: {0}".format(graph.has_node("a")))
print("Node 'g' is in graph: {0}".format(graph.has_node("g")))
print("Graph edges: {0}".format(graph.edge_set()))
print("Number of edges: {0}".format(graph.num_edges()))
print("Edge ('a', 'c') is in graph: {0}".format(graph.has_edge("a", "c")))
print("Edge ('a', 'f') is in graph: {0}".format(graph.has_edge("a", "f")))
print("Degree of node 'a': {0}".format(graph.node_degree("a")))
print("Degree of node 'f': {0}".format(graph.node_degree("f")))
print("Neighbourhood of 'a': {0}".format(graph.neighbourhood("a")))

print("\nWEIGHTED GRAPH")
wgraph = Graph(is_weighted=True)
wgraph.add_nodes(nodes)
wgraph.remove_node("f")
wgraph.add_edge("a", "b")
wgraph.add_edge("a", "c")
wgraph.add_edge("b", "c")
wgraph.add_edge("b", "e")
wgraph.add_edge("c", "d")
wgraph.add_edge("d", "e")
wgraph.add_edge("a", "e")
wgraph.add_edge("b", "d")
wgraph.set_edge_weight("a", "b", 2.0)
print("Weight of edge ('a', 'b'): {0}".format(wgraph.get_edge_weight("a", "b")))
wgraph.set_edge_weight("a", "c", 3.0)
wgraph.set_edge_weight("b", "c", 1.0)
wgraph.set_edge_weight("b", "e", 4.0)
wgraph.set_edge_weight("c", "d", 2.0)
wgraph.set_edge_weight("d", "e", 5.0)
wgraph.set_edge_weight("a", "e", 2.0)
wgraph.set_edge_weight("b", "d", 3.0)
print("Weighted graph: {0}".format(wgraph))

print("\nDIRECTED GRAPH")
digraph = Graph(is_directed=True)
digraph.add_nodes(nodes)
digraph.remove_node("f")
digraph.add_edge("a", "b")
digraph.add_edge("a", "c")
digraph.add_edge("b", "c")
digraph.add_edge("b", "e")
digraph.add_edge("c", "d")
digraph.add_edge("d", "e")
print("Directed graph: {0}".format(digraph))

print("In degree of node 'b': {0}".format(digraph.node_in_degree("b")))
print("Out degree of node 'b': {0}".format(digraph.node_out_degree("b")))
print("In star of 'b': {0}".format(digraph.node_in_edges("b")))
print("Out star of 'b': {0}".format(digraph.node_out_edges("b")))

print("\nDIRECTED WEIGHTED GRAPH")
wdgraph = Graph(is_directed=True, is_weighted=True)
wdgraph.add_nodes(nodes)
wdgraph.remove_node("f")
wdgraph.add_edge("a", "b")
wdgraph.add_edge("a", "c")
wdgraph.add_edge("b", "c")
wdgraph.add_edge("b", "e")
wdgraph.add_edge("c", "d")
wdgraph.add_edge("d", "e")
wdgraph.add_edge("a", "e")
wdgraph.add_edge("b", "d")
wdgraph.set_edge_weight("a", "b", 2.0)
wdgraph.set_edge_weight("a", "c", 3.0)
wdgraph.set_edge_weight("b", "c", 1.0)
wdgraph.set_edge_weight("b", "e", 4.0)
wdgraph.set_edge_weight("c", "d", 2.0)
wdgraph.set_edge_weight("d", "e", 5.0)
wdgraph.set_edge_weight("a", "e", 2.0)
wdgraph.set_edge_weight("b", "d", 3.0)
print("Weighted directed graph: {0}".format(wdgraph))

#print("\nIMPORT/EXPORT")
#data_path = "./../../../../../data/"
#igraph = Graph.import_graph(data_path + "unweighted.edgelist", ' ')
#idgraph = Graph.import_graph(data_path + "unweighted.edgelist", ' ', is_directed=True)
#iwgraph = Graph.import_graph(data_path + "weighted.edgelist", ' ', is_weighted=True)
#iwdgraph = Graph.import_graph(data_path + "weighted.edgelist", ' ', is_directed=True, is_weighted=True)
#print("Undirected graph: {0}".format(igraph))
#print("Directed graph: {0}".format(idgraph))
#print("Weighted graph: {0}".format(iwgraph))
#print("Directed weighted graph: {0}".format(iwdgraph))
#del igraph
#del idgraph
#del iwgraph
#del iwdgraph
#Graph.export_graph(graph, data_path + "graph.py.edgelist", ' ')
#Graph.export_graph(digraph, data_path + "digraph.py.edgelist", ' ', is_directed=True)
#Graph.export_graph(wgraph, data_path + "wgraph.py.edgelist", ' ', is_weighted=True)
#Graph.export_graph(wdgraph, data_path + "wdgraph.py.edgelist", ' ', is_directed=True, is_weighted=True)

print("\nBFS & DFS")
root_node = "a"
print("BFS nodes order: {0}".format(graph.bfs_nodes(root_node)))
print("BFS edges: {0}".format(graph.bfs_edges(root_node)))
print("BFS tree: {0}".format(graph.bfs_tree(root_node)))

print("DFS nodes order: {0}".format(graph.dfs_nodes(root_node)))
print("DFS edges: {0}".format(graph.dfs_edges(root_node)))
print("DFS tree: {0}".format(graph.dfs_tree(root_node)))

print("\nCONNECTIVITY")
graph.add_node("g")
graph.add_edge("f", "g")
print("Graph: {0}".format(graph))
print("Graph is connected: {0}".format(graph.is_connected()))
print("Number of connected components: {0}".format(graph.number_connected_components()))
print("Connected components: [{0}]".format(graph.connected_components()))
print("Connected component of 'a': {0}".format(graph.node_connected_component("a")))
print("Connected component of 'f': {0}".format(graph.node_connected_component("f")))
subgraphs = graph.connected_subgraphs()
i = 0
for subgraph in subgraphs:
    print("Subgraph {0}: {1}".format(i, subgraph))
    i += 1
del subgraphs
#print("Connected subgraphs: {0}".format(graph.connected_subgraphs()))
print("Removing 'f' and 'g'...")
graph.remove_node("f")
graph.remove_node("g")
print("Graph: {0}".format(graph))
print("Graph is connected: {0}".format(graph.is_connected()))
print("Number of connected components: {0}".format(graph.number_connected_components()))
print("---")
digraph.add_edge("d", "a")
digraph.add_node("f")
digraph.add_edge("e", "f")
digraph.add_edge("f", "e")
print("Directed graph: {0}".format(digraph))
print("Digraph is strongly connected: {0}".format(digraph.is_strongly_connected()))
print("Connected components: [{0}]".format(digraph.strongly_connected_components()))
subgraphs = digraph.strongly_connected_subgraphs()
i = 0
for subgraph in subgraphs:
    print("Strong subgraph {0}: {1}".format(i, subgraph))
    i += 1
del subgraphs
print("Removing (a, c) and adding (c, a) and (e, c)...")
digraph.remove_edge("a", "c")
digraph.add_edge("c", "a")
digraph.add_edge("e", "c")
print("Directed graph: {0}".format(digraph))
print("Digraph is strongly connected: {0}".format(digraph.is_strongly_connected()))


print("\nDAG & TOPOLOGICAL SORT")
digraph.remove_node("f")
digraph.remove_edge("d", "a")
print("Digraph: {0}".format(digraph))
print("Digraph is DAG: {0}".format(digraph.is_dag()))
print("Removing edges (c, a) and (e, c)...")
digraph.remove_edge("c", "a")
digraph.remove_edge("e", "c")
print("Digraph: {0}".format(digraph))
print("Digraph is DAG: {0}".format(digraph.is_dag()))
print("Topological sorting: {0}".format(digraph.topological_sort()))

print("\nMINIMUM SPANNING TREE")
print("MST of weigted graph: {0}".format(wgraph.get_mst()))
