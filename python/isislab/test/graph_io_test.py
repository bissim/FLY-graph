#! /usr/bin/env python3

import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

from fly.graph import Graph

nodes = ["a", "b", "c", "d", "e", "f"]

print("\nUNDIRECTED GRAPH")
graph = Graph()
graph.add_nodes(nodes)
graph.add_edge("a", "b")
graph.add_edge("a", "c")
graph.add_edge("b", "c")
graph.add_edge("b", "e")
graph.add_edge("c", "d")
graph.add_edge("d", "e")
print("Undirected graph: {0}".format(graph))

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

print("\nIMPORT/EXPORT")
data_path = "../../data/"
graph_path = data_path + "graph.py.edgelist"
dgraph_path = data_path + "digraph.py.edgelist"
wgraph_path = data_path + "wgraph.py.edgelist"
wdgraph_path = data_path + "wdgraph.py.edgelist"
igraph = Graph.import_graph(graph_path, ' ')
idgraph = Graph.import_graph(dgraph_path, ' ', is_directed=True)
iwgraph = Graph.import_graph(wgraph_path, ' ', is_weighted=True)
iwdgraph = Graph.import_graph(wdgraph_path, ' ', is_directed=True, is_weighted=True)
print("Undirected graph: {0}".format(igraph))
print("Directed graph: {0}".format(idgraph))
print("Weighted graph: {0}".format(iwgraph))
print("Weight of (a, e) edge from weighted graph: {0}".format(iwgraph.get_edge_weight("a", "e")))
print("Directed weighted graph: {0}".format(iwdgraph))
print("Weight of (a, e) edge from weighted directed graph: {0}".format(iwdgraph.get_edge_weight("a", "e")))
del igraph
del idgraph
del iwgraph
del iwdgraph
Graph.export_graph(graph, graph_path, ' ')
Graph.export_graph(digraph, dgraph_path, ' ')
Graph.export_graph(wgraph, wgraph_path, ' ')
Graph.export_graph(wdgraph, wdgraph_path, ' ')
