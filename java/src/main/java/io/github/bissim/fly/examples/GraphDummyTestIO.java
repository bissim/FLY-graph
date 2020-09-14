package io.github.bissim.fly.examples;

import static java.lang.System.err;
import static java.lang.System.out;

import io.github.bissim.fly.Graph;

public class GraphDummyTestIO
{

	public static void main(String[] args)
	{
		String[] nodes = {"a", "b", "c", "d", "e", "f"};

		/*
		 * create unweighted undirected graph
		 */
		Graph<String, Object> graph = new Graph<>(String.class, false, false);
		graph.addNodes(nodes);
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "e");
		graph.addEdge("c", "d");
		graph.addEdge("d", "e");
		out.println("Undirected graph: " + graph);

		/*
		 * create weighted graph
		 */
		Graph<String, Object> wgraph = new Graph<>(String.class, false, true);
		wgraph.addNodes(nodes);
		wgraph.removeNode("f");
		wgraph.addEdge("a", "b");
		wgraph.setEdgeWeight("a", "b", 2.0);
		wgraph.addEdge("a", "c");
		wgraph.setEdgeWeight("a", "c", 3.0);
		wgraph.addEdge("b", "c");
		wgraph.setEdgeWeight("b", "c", 1.0);
		wgraph.addEdge("b", "e");
		wgraph.setEdgeWeight("b", "e", 4.0);
		wgraph.addEdge("c", "d");
		wgraph.setEdgeWeight("c", "d", 2.0);
		wgraph.addEdge("d", "e");
		wgraph.setEdgeWeight("d", "e", 5.0);
		wgraph.addEdge("a", "e");
		wgraph.setEdgeWeight("a", "e", 2.0);
		wgraph.addEdge("b", "d");
		wgraph.setEdgeWeight("b", "d", 3.0);
		out.println("Weighted graph: " + wgraph);

		/*
		 * create directed graph
		 */
		Graph<String, Object> digraph = new Graph<>(String.class, true, false);
		digraph.addNodes(nodes);
		digraph.removeNode("f");
		digraph.addEdge("a", "b");
		digraph.addEdge("a", "c");
		digraph.addEdge("b", "c");
		digraph.addEdge("b", "e");
		digraph.addEdge("c", "d");
		digraph.addEdge("d", "e");
		out.println("Directed graph: " + digraph);

		/*
		 * create directed weighted graph
		 */
		Graph<String, Object> wdgraph = new Graph<>(String.class, true, true);
		wdgraph.addNodes(nodes);
		wdgraph.removeNode("f");
		wdgraph.addEdge("a", "b");
		wdgraph.setEdgeWeight("a", "b", 2.0);
		wdgraph.addEdge("a", "c");
		wdgraph.setEdgeWeight("a", "c", 3.0);
		wdgraph.addEdge("b", "c");
		wdgraph.setEdgeWeight("b", "c", 1.0);
		wdgraph.addEdge("b", "e");
		wdgraph.setEdgeWeight("b", "e", 4.0);
		wdgraph.addEdge("c", "d");
		wdgraph.setEdgeWeight("c", "d", 2.0);
		wdgraph.addEdge("d", "e");
		wdgraph.setEdgeWeight("d", "e", 5.0);
		wdgraph.addEdge("a", "e");
		wdgraph.setEdgeWeight("a", "e", 2.0);
		wdgraph.addEdge("b", "d");
		wdgraph.setEdgeWeight("b", "d", 3.0);
		out.println("Weighted directed graph: " + wdgraph);

		/*
		 * Import/Export graph
		 */
		out.println("\nGRAPH I/O");
		Graph<String, Object> importedGraph = null;
		Graph<String, Object> importedDGraph = null;
		Graph<String, Object> importedWGraph = null;
		Graph<String, Object> importedWDGraph = null;
		String dataPath = "../data/";
		String graphPath = dataPath + "graph.py.edgelist";
		String dgraphPath = dataPath + "digraph.py.edgelist";
		String wgraphPath = dataPath + "wgraph.py.edgelist";
		String wdgraphPath = dataPath + "wdgraph.py.edgelist";
		final String SEPARATOR = " ";
		final Class<String> NODE_CLASS = String.class;
		try
		{
			out.println("Importing " + graphPath + "...");
			importedGraph = Graph.importGraph(
					graphPath,
					SEPARATOR, // separator character
					NODE_CLASS, // node class
					false, // is weighted?
					false // is directed?
			);

			out.println("Importing " + dgraphPath + "...");
			importedDGraph = Graph.importGraph(
					dgraphPath,
					SEPARATOR,
					NODE_CLASS,
					false,
					true
			);

			out.println("Importing " + wgraphPath + "...");
			importedWGraph = Graph.importGraph(
					wgraphPath,
					SEPARATOR,
					NODE_CLASS,
					true,
					false
			);

			out.println("Importing " + wdgraphPath + "...");
			importedWDGraph = Graph.importGraph(
					wdgraphPath,
					SEPARATOR,
					NODE_CLASS,
					true,
					true
			);
		}
		catch (Exception e)
		{
			err.println(
					e.getClass().getSimpleName() +
					": " +
					e.getLocalizedMessage()
			);
		}
		out.println("Imported undirected graph: " + importedGraph);
		out.println("Imported directed graph: " + importedDGraph);
		out.println("Imported weighted graph: " + importedWGraph);
		out.println(
				"Weight of (a, e) edge from weighted graph: " +
				importedWGraph.getEdgeWeight("a", "e")
		);
		out.println("Imported weighted directed graph: " + importedWDGraph);
		out.println(
				"Weight of (a, e) edge from weighted directed graph: " +
				importedWDGraph.getEdgeWeight("a", "e")
		);
		try
		{
			out.println("Exporting " + graphPath + "...");
			Graph.exportGraph(graph, graphPath, " ");
			out.println("Exporting " + dgraphPath + "...");
			Graph.exportGraph(digraph, dgraphPath, " ");
			out.println("Exporting " + wgraphPath + "...");
			Graph.exportGraph(wgraph, wgraphPath, " ");
			out.println("Exporting " + wdgraphPath + "...");
			Graph.exportGraph(wdgraph, wdgraphPath, " ");
		}
		catch (Exception e)
		{
			err.println(
					e.getClass().getSimpleName() +
					": " +
					e.getLocalizedMessage()
			);
		}
		out.println("Graphs have been successfully exported!");
	}

}
