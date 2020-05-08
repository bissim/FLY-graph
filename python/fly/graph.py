#! /usr/bin/env python3

# package fly.graph

from typing import TypeVar, Generic
#from logging import Logger

import networkx as nx

# The type used to represent nodes (or 'vertices') into a graph.
V = TypeVar('V')

# The type used to represent edges into a graph.
E = TypeVar('E')

class Graph():
    """
    A class used to represent a graph for use into FLY language.

    It basically wraps the NetworkX representation of graphs according
    to FLY API for graphs.

    Attributes
    ----------
    graph : object
        the Graph object from NetworkX library

    is_directed : bool
        Denotes whether graph is directed

    is_weighted : bool
        Denotes whether graph is weighted

    Methods
    -------
    clear()
        Empties graph nodes and edges sets.

    add_node(node)
        Adds a specified node to graph.

    add_nodes(nodes)
        Adds a specified list of nodes to graph.

    node_degree(node)
        Gets the number of edges the specified node is part of.

    node_in_degree(node)
        Gets the number of edges the specified node is target of.

    node_out_degree(node)
        Gets the number of edges the specified node is source of.

    neighbourhood(node)
        Gets the edges the specified node is part of.

    node_in_edges(node)
        Gets the edges the specified node is target of.

    node_out_edges(node)
        Gets the edges the specified node is source of.

    node_set()
        Gets the nodes of graph.

    num_nodes()
        Gets the number of nodes in graph.

    remove_node(node)
        Removes a specified node from graph.

    has_node(node)
        Checks whether graph has a specified node.

    add_edge(first_node, second_node)
        Adds an edge between two specified nodes to graph.

    get_edge(first_node, second_node)
        Gets the edge of graph between two specified nodes.

    edge_set()
        Gets the edges of graph.

    num_edges()
        Gets the number of edges of graph.

    get_edge_weight(first_node, second_node)
        Gets weight of the edge of graph between two specified nodes.

    set_edge_weight(first_node, second_node, weight)
        Sets weight of the edge of graph between two specified nodes.

    remove_edge(first_node, second_node)
        Removes the edge of graph between two specified nodes.

    has_edge(first_node, second_node)
        Checks whether graph has node between two specified nodes or not.

    import_graph(path, separator, is_directed, is_weighted)
        Imports a graph from file.

    export_graph(fly_graph, path, separator, is_directed, is_weighted)
        Exports a graph to file.

    bfs_edges(root_node)
        Gets edges of BFS rooted in specified node.

    bfs_nodes(root_node)
        Gets nodes of BFS rooted in specified node.

    bfs_tree(root_node)
        Gets the BFS rooted in specified node.

    dfs_edges(root_node)
        Gets edges of DFS rooted in specified node.

    dfs_nodes(root_node)
        Gets nodes of DFS rooted in specified node.

    dfs_tree(root_node)
        Gets the DFS rooted in specified node.

    is_connected()
        Checks whether graph is connected or not.

    is_strongly_connected()
        Checks whether graph is strongly connected or not.

    connected_components()
        Gets connected components of graph.

    connected_subgraphs()
        Gets connected subgraphs of graph.

    number_connected_components()
        Gets the number of connected components of graph.

    node_connected_components(node)
        Gets connected component for specified node.

    strongly_connected_components()
        Gets strongly connected components of graph.

    strongly_connected_subgraphs()
        Gets strongly connected subgraphs of graph.

    is_dag()
        Checks whether graph is DAG or not.

    topological_sort()
        Gets a topological sort for graph.

    get_mst()
        Gets minimum spanning tree from graph by running Prim's algorithm on it.
    """

    # TODO consider documenting methods into class docblock

    # initializer
    def __init__(self, is_directed=False, is_weighted=False) -> None:
        """
        Builds an instance of FLY Graph.

        It consists of an instance of NetworkX graph, a boolean value that
        indicates whether graph is directed or not (by default, it is set to
        False) and another boolean value that indicates whether graph is
        weighted or not (by default, it is set to False). 

        Parameters
        ----------
        is_directed : bool
            Denotes whether graph is directed or not (default: False)

        is_weighted : bool
            Denotes whether graph is weighted or not (default: False)
        """
        self.graph = nx.DiGraph() if is_directed else nx.Graph()
        self.is_directed = is_directed
        self.is_weighted = is_weighted

    def __repr__(self):
        to_string = "{" + str(self.graph.nodes) + ", " + str(self.graph.edges) + "}"
        if self.is_directed:
            to_string += ", directed"
        if self.is_weighted:
            to_string += ", weighted"
        return to_string

    # TODO properly write str

#    def __str__(self):
#        pass

    def clear(self) -> None:
        """
        Empties graph nodes and edges sets.
        """
        self.graph.clear()

    #
    # Nodes
    #

    def add_node(self, node: V) -> None:
        """
        Adds a node to graph.

        Parameters
        ----------
        node : V
            The node to add to graph

        """
        self.graph.add_node(node)

    def add_nodes(self, nodes: list) -> None:
        """
        Adds a list of nodes to graph.

        Parameters
        ----------
        nodes : list
            The list of nodes to add to graph
        """
        self.graph.add_nodes_from(nodes)

#    def add_nodes(self, *nodes: V) -> None:
#        """
#        """
#        for node in nodes:
#            self.add_node(node)

    def node_degree(self, node: V) -> int:
        """
        Gets the number of edges the node is part of.

        Parameters
        ----------
        node : V
            The node to check for degree

        Returns
        -------
        int
            Number of edges the node is part of
        """
        return self.graph.degree(node)

    def node_in_degree(self, node: V) -> int:
        """
        Gets the number of edges the specified node is target of.

        Parameters
        ----------
        node : V
            The node to check for 'in' degree

        Returns
        -------
        int
            Number of edges the node is target of
        """
        return self.graph.in_degree(node)

    def node_out_degree(self, node: V) -> int:
        """
        Gets the number of edges the specified node is source of.

        Parameters
        ----------
        node : V
            The node to check for 'out' degree

        Returns
        -------
        int
            Number of edges the node is souce of
        """
        return self.graph.out_degree(node)

    def neighbourhood(self, node: V) -> list:
        """
        Gets the edges the specified node is part of.

        Parameters
        ----------
        node : V
            The node which we want the neighbourhood of

        Returns
        -------
        list
            Neighbourhood of specified node

        """
        return list(self.graph.adj[node])

    def node_in_edges(self, node: V) -> list:
        """
        Gets the edges the specified node is target of.

        Parameters
        ----------
        node : V
            The node which we want the incoming edges of

        Returns
        -------
        list
            Incoming edges of specified node
            
        """
        return list(self.graph.in_edges(nbunch=node))

    def node_out_edges(self, node: V) -> list:
        """
        Gets the edges the specified node is source of.

        Parameters
        ----------
        node : V
            The node which we want the outgoing edges of

        Returns
        -------
        list
            Outgoing edges of specified node
        """
        return list(self.graph.out_edges(nbunch=node))

    def node_set(self) -> list:
        """
        Gets the nodes of graph.

        Returns
        -------
        list
            Graph nodes set
        """
        return list(self.graph.nodes)

    def num_nodes(self) -> int:
        """
        Gets the number of nodes in graph.

        Returns
        -------
        int
            Number of graph nodes
        """
        return self.graph.order()

    def remove_node(self, node: V) -> None:
        """
        Removes a specified node from graph.

        Parameters
        ----------
        node : V
            The node we want to remove from graph

        """
        self.graph.remove_node(node)

    def has_node(self, node: V) -> bool:
        """
        Checks whether graph has a specified node.

        Parameters
        ----------
        node : V
            The node we want to check to be in graph

        Returns
        -------
        bool
            'True' if node is in graph, 'False' otherwise
        """
        return self.graph.has_node(node)

    #
    # Edges
    #

    def add_edge(self, first_node: V, second_node: V) -> None:
        """
        Adds an edge between two specified nodes to graph.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge
        """
        self.graph.add_edge(first_node, second_node)

    def get_edge(self, first_node: V, second_node: V) -> E:
        """
        Gets the edge of graph between two specified nodes.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge

        Returns
        -------
        E
            Graph edge which specified nodes are source and target of
        """
        return self.graph.edges[first_node, second_node]

    def edge_set(self) -> list:
        """
        Gets the edges of graph.

        Returns
        -------
        list
            Graph edge set
        """
        return list(self.graph.edges)

    def num_edges(self) -> int:
        """
        Gets the number of edges of graph.

        Returns
        -------
        int
            The number of graph edges
        
        """
        return self.graph.size()

    def get_edge_weight(self, first_node: V, second_node: V) -> float:
        """
        Gets weight of the edge of graph between two specified nodes.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge

        Returns
        -------
        float
            Weight of edge which specified nodes are edge and target of
        """
        return self.graph[first_node][second_node]['weight'] #if self.is_weighted else 1.0

    def set_edge_weight(self, first_node: V, second_node: V, weight: float) -> None:
        """
        Sets weight of the edge of graph between two specified nodes.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge

        weight : float
            Weight of edge
        """
        self.graph[first_node][second_node]['weight'] = weight #if self.is_weighted else 1.0

    def remove_edge(self, first_node: V, second_node: V) -> None:
        """
        Removes the edge of graph between two specified nodes.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge
        """
        self.graph.remove_edge(first_node, second_node)

    def has_edge(self, first_node: V, second_node: V) -> bool:
        """
        Checks whether graph has node between two specified nodes or not.

        Parameters
        ----------
        first_node : V
            Source node of edge

        second_node : V
            Target node of edge

        Returns
        -------
        bool
            'True' if an edge exists between specified nodes, 'False' otherwise
        """
        return self.graph.has_edge(first_node, second_node)

    #
    # I/O
    #

    @staticmethod
    def import_graph(path: str, separator: str, is_directed=False, is_weighted=False) -> object:
        """
        Imports a graph from a CSV file.

        Graph has to be represented as 'edge list' in order to be properly read
        from file.

        Parameters
        ----------
        path : str
            Path of source file

        separator : str
            Separator character of CSV file

        is_directed : bool
            Denotes whether graph is directed or not (default: False)

        is_weighted : bool
            Denotes whether graph is weighted or not (default: False)

        Returns
        -------
        object
            FLY graph read from file
        """
        fly_graph = Graph(is_directed=is_directed, is_weighted=is_weighted)
        fly_graph.graph = nx.read_weighted_edgelist(path, delimiter=separator) #if fly_graph.is_weighted else nx.read_edgelist(path, delimiter=separator, data=False)
        return fly_graph

    @staticmethod
    def export_graph(fly_graph: object, path: str, separator: str) -> None:
        """
        Exports a graph to file.

        Parameters
        ----------
        fly_graph : object
            FLY graph to save in file

        path : str
            Path of destination file

        separator : str
            Separator character of CSV file
        """
        nx.write_weighted_edgelist(fly_graph.graph, path, delimiter=separator) #if is_weighted else nx.write_edgelist(fly_graph.graph, path, delimiter=separator)

    #
    # Graph traversal
    #

    def bfs_edges(self, root_node: V) -> list:
        """
        Gets edges of BFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of BFS tree

        Returns
        -------
        list
            Edges of BFS tree extracted from graph
        """
        return list(nx.bfs_edges(self.graph, root_node))

    def bfs_nodes(self, root_node: V) -> list:
        """
        Gets nodes of BFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of BFS tree

        Returns
        -------
        list
            Nodes o BFS tree extracted from graph
        """
        return [root_node] + [v for u, v in nx.bfs_edges(self.graph, root_node)]

    def bfs_tree(self, root_node: V) -> object:
        """
        Gets the BFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of BFS tree

        Returns
        -------
        object
            BFS tree extracted from graph
        """
        tree = Graph()
        tree.graph.add_edges_from(self.bfs_edges(root_node))
        tree.graph.add_nodes_from(self.bfs_nodes(root_node))
        return tree

    def dfs_edges(self, root_node: V) -> list:
        """
        Gets edges of DFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of DFS tree

        Returns
        -------
        list
            Edges of DFS tree extracted from graph
        """
        return list(nx.dfs_edges(self.graph, source=root_node))

    def dfs_nodes(self, root_node: V) -> list:
        """
        Gets nodes of DFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of DFS tree

        Returns
        -------
        list
            Nodes of DFS tree extracted from graph
        """
        return [root_node] + [v for u, v in nx.dfs_edges(self.graph, source=root_node)]

    def dfs_tree(self, root_node: V) -> object:
        """
        Gets the DFS rooted in specified node.

        Parameters
        ----------
        root_node : V
            Root node of DFS tree

        Returns
        -------
        object
            DFS tree exrcted from graph
        """
        tree = Graph()
        tree.graph.add_edges_from(self.dfs_edges(root_node))
        tree.graph.add_nodes_from(self.dfs_nodes(root_node))
        return tree

    #
    # Connectivity
    #

    def is_connected(self) -> bool:
        """
        Checks whether graph is connected or not.

        Returns
        -------
        bool
            'True' if graph is connected, 'False' otherwise
        """
        return nx.is_connected(self.graph)

    def is_strongly_connected(self) -> bool:
        """
        Checks whether graph is strongly connected or not.

        Returns
        -------
        bool
            'True' if graph i strongly connected, 'False' otherwise
        """
        return nx.is_strongly_connected(self.graph)

    def connected_components(self) -> list:
        """
        Gets connected components of graph.

        Returns
        -------
        list
            Graph connected components
        """
        return list(set for set in nx.connected_components(self.graph))

    def connected_subgraphs(self) -> list:
        """
        Gets connected subgraphs of graph.

        Returns
        -------
        list
            Graph connected subgraphs
        """
        subgraphs = list()
        for set in nx.connected_components(self.graph):
            subgraph = Graph(is_directed = self.is_directed, is_weighted = self.is_weighted)
            subgraph.add_nodes(set)
            for edge in self.graph.edges:
                if edge[0] in set and edge[1] in set:
                    subgraph.add_edge(edge[0], edge[1])
            subgraphs.append(subgraph)
        return subgraphs
#        return list(self.graph.subgraph(set).copy() for set in nx.connected_components(self.graph))

    def number_connected_components(self) -> int:
        """
        Gets the number of connected components of graph.

        Returns
        -------
        int
            Number of Graph connected components
        """
        return nx.number_connected_components(self.graph)

    def node_connected_component(self, node: V) -> list:
        """
        Gets connected component for specified node.

        Parameters
        ----------
        node : V
            The node which we want to know the connected component of

        Returns
        -------
        list
            Connected component of specified node
        """
        return nx.node_connected_component(self.graph, node)

    def strongly_connected_components(self) -> list:
        """
        Gets strongly connected components of graph.

        Returns
        -------
        list
            Strongly connected component of specified node
        """
        return list(set for set in nx.kosaraju_strongly_connected_components(self.graph))

    def strongly_connected_subgraphs(self) -> list:
        """
        Gets strongly connected subgraphs of graph.

        Returns
        -------
        list
            Graph strngly connected components
        """
        subgraphs = list()
        for set in nx.kosaraju_strongly_connected_components(self.graph):
            subgraph = Graph(is_directed = self.is_directed, is_weighted = self.is_weighted)
            subgraph.add_nodes(set)
            for edge in self.graph.edges:
                if edge[0] in set and edge[1] in set:
                    subgraph.add_edge(edge[0], edge[1])
            subgraphs.append(subgraph)
        return subgraphs
#        return list(self.__class__().graph = (self.graph.subgraph(set).copy for set in nx.kosaraju_strongly_connected_components(self.graph)))

    #
    # DAG and topological sorting
    #

    def is_dag(self) -> bool:
        """
        Checks whether graph is a directed acyclic graph (DAG) or not.

        Returns
        -------
        bool
            'True' if graph is DAG, 'False' otherwise
        """
        return nx.is_directed_acyclic_graph(self.graph)

    def topological_sort(self) -> list:
        """
        Gets a topological sort for graph.

        Returns
        -------
        list
            Graph topological-sorted nodes
        """
        return list(nx.topological_sort(self.graph))

    #
    # Minimum spanning tree
    #

    def get_mst(self) -> object:
        """
        Gets minimum spanning tree from graph by running Prim's algorithm on it.

        Returns
        -------
        object
            Graph minimum spanning tree
        """
        mst = self.__class__()
        mst.graph = nx.minimum_spanning_tree(self.graph, algorithm='prim')
        return mst
#
# Run tests as standalone module
#
if __name__ == "__main__":
    print("Run FLY Graph tests")

    import os, sys
    sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))
    from ..test import graph_test, graph_io_test

    graph_test
    graph_io_test
