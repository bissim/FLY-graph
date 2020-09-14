package io.github.bissim.fly;

import static java.lang.System.out;

import java.util.stream.IntStream;

public class GraphDummyTest
{

	public static void main(String[] args)
	{
		Graph<String, Object> graph = new Graph<>(String.class, false, false);
		String[] nodes = {"a", "b", "c", "d", "e", "f"};

		/*
		 * graph management
		 */
		// add nodes
		graph.addNodes(nodes);

		// add some edges
		graph.addEdge("a", "b");
		graph.addEdge("a", "c");
		graph.addEdge("b", "c");
		graph.addEdge("b", "e");
		graph.addEdge("c", "d");
		graph.addEdge("d", "e");

		// print graph
		out.println("Undirected graph: " + graph);

		// edge manipulation
		Object edge = graph.getEdge("d", "e");
		out.println("Edge " + edge);
		out.println("Change edge target from 'e' to 'a'");
		graph.setEdgeTarget(edge, "a");
		out.println("Graph: " + graph);
		edge = graph.getEdge("d", "a");
		out.println("Edge: " + edge);
		out.println("Change edge source from 'd' to 'c'");
		graph.setEdgeSource(edge, "c");
		out.println("Graph: " + graph);
		

		// other management methods
		String[] graphNodes = graph.nodeSet();
		out.print("Graph nodes: [");
		IntStream
				.range(0, graphNodes.length)
				.forEach(i -> out.print(graphNodes[i] + ","));
		out.print("]\n");
		out.println("Number of nodes: " + graph.numNodes());
		out.println("Node 'a' is in graph: " + graph.hasNode("a"));
		out.println("Node 'g' is in graph: " + graph.hasNode("g"));
		Object[] graphEdges = graph.edgeSet();
		out.print("Graph edges: [");
		IntStream
				.range(0, graphEdges.length)
				.forEach(i -> out.print(graphEdges[i] + ","));
		out.print("]\n");
		out.println("Number of edges: " + graph.numEdges());
		out.println("Edge (\"a\", \"c\") is in graph: " + graph.hasEdge("a", "c"));
		out.println("Edge (\"a\", \"f\") is in graph: " + graph.hasEdge("a", "f"));
		out.println("Degree of node 'a': " + graph.nodeDegree("a"));
		out.println("Degree of node 'f': " + graph.nodeDegree("f"));
		out.println("Neighbourhood of node 'a': " + graph.neighbourhood("a"));

		/*
		 * create weighted graph
		 */
		Graph<String, Object> wgraph = new Graph<>(String.class, false, true);
		wgraph.addNodes(nodes);
		wgraph.removeNode("f");
		wgraph.addEdge("a", "b");
		wgraph.setEdgeWeight("a", "b", 2.0);
		out.println("Weight of edge (\"a\", \"b\"): " + wgraph.getEdgeWeight("a", "b"));
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

		out.println("In degree of 'b': " + digraph.nodeInDegree("b"));
		out.println("Out degree of 'b': " + digraph.nodeOutDegree("b"));
		out.print("In star of 'b': [");
		Object[] inStar = digraph.nodeInEdges("b");
		IntStream
				.range(0, inStar.length)
				.forEach(i -> out.print(inStar[i] + ","));
		out.print("]\n");
		out.print("Out star of 'b': [");
		Object[] outStar = digraph.nodeOutEdges("b");
		IntStream
				.range(0, outStar.length)
				.forEach(i -> out.print(outStar[i] + ","));
		out.print("]\n");

		/*
		 * create directed weighted graph
		 */
		Graph<String, Object> wdgraph = new Graph<>(String.class, true, true);
		wdgraph.addNodes(nodes);
		wdgraph.removeNode("f");
		wdgraph.addEdge("a", "b");
		wdgraph.setEdgeWeight("a", "b", 2.0);
		out.println("Weight of edge (\"a\", \"b\"): " + wdgraph.getEdgeWeight("a", "b"));
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
		 * graph metrics
		 */
		out.println("\nGRAPH METRICS");
		out.println("Graph: " + graph);
		Object[] shortestPathA2C = graph.shortestPath("a", "c");
		out.print("Shortest path from 'a' to 'c': ");
		IntStream
				.range(0, shortestPathA2C.length)
				.forEach(i -> out.print(shortestPathA2C[i] + ","));
		out.println();
		Object[] shortestPathA2D = graph.shortestPath("a", "d");
		out.print("Shortest path from 'a' to 'd': ");
		IntStream
				.range(0, shortestPathA2D.length)
				.forEach(i -> out.print(shortestPathA2D[i] + ","));
		out.println();
		Object[] shortestPathA2F = graph.shortestPath("a", "f");
		out.print("Shortest path from 'a' to 'f': ");
		if (shortestPathA2F != null)
		{
			IntStream
				.range(0, shortestPathA2F.length)
				.forEach(i -> out.print(shortestPathA2F[i] + ","));
		}
		else
		{
			out.print("Infinity");
		}
		out.println();
		graph.removeNode("f");
		out.println("Graph diameter: " + graph.getDiameter());
		out.println("Graph radius: " + graph.getRadius());
		graph.addNode("f");
		double clusterScoreA = graph.getNodeClusteringCoefficient("a");
		out.println("Clustering coefficient of node 'a': " + clusterScoreA);
		double clusterScoreB = graph.getNodeClusteringCoefficient("b");
		out.println("Clustering coefficient of node 'a': " + clusterScoreB);
		double clusterScoreAvg = graph.getAverageClusteringCoefficient();
		out.println("Average clustering coefficient for graph: " + clusterScoreAvg);
		double clusterScoreGlobal = graph.getGlobalClusteringCoefficient();
		out.println("Global clustering coefficient for graph: " + clusterScoreGlobal);
		// long trianglesA = graph.getNumberOfTriangles("a");
		// out.println("Number of triangles for node 'a': " + trianglesA);
		// long triangles = graph.getNumberOfTriangles();
		// out.println("Total number of triangles in graph: " + triangles);
		// long triadsA = graph.getNumberOfTriplets("a");
		// out.println("Number of triplets for node 'a': " + triadsA);
		// long triads = graph.getNumberOfTriplets();
		// out.println("Total number of triplets in graph: " + triads);

		/*
		 * find BFS nodes
		 */
		out.println("\nBREADTH FIRST SEARCH");
		String rootNode = "a";
		String[] bfsNodes = graph.bfsNodes(rootNode);
		out.print("BFS nodes order: ");
		IntStream
				.range(0, bfsNodes.length)
				.forEach(i -> out.print(bfsNodes[i] + ","));
		out.println();

		/*
		 * find BFS edges
		 */
		Object[] bfsEdges = graph.bfsEdges(rootNode);
		out.print("BFS edges: ");
		IntStream
				.range(0, bfsEdges.length)
				.forEach(i -> out.print(bfsEdges[i] + ","));
		out.println();

		/*
		 * find BFS tree
		 */
		Graph<String, Object> bfsTree = graph.bfsTree(rootNode);
		out.println("BFS tree: " + bfsTree);

		/*
		 * find DFS nodes
		 */
		out.println("\nDEPTH FIRST SEARCH");
		String[] moreNodes = {"g", "h", "i", "j"};
		graph.addNodes(moreNodes);
		graph.addEdge("c", "f");
		graph.addEdge("c", "h");
		graph.addEdge("f", "g");
		graph.addEdge("f", "i");
		graph.addEdge("i", "j");
		out.println("Graph: " + graph);
		String[] dfsNodes = graph.dfsNodes(rootNode);
		out.print("DFS nodes order: ");
		IntStream
				.range(0, dfsNodes.length)
				.forEach(i -> out.print(dfsNodes[i] + ","));
		out.println();

		/*
		 * find DFS edges
		 */
		Object[] dfsEdges = graph.dfsEdges(rootNode);
		out.print("DFS edges: ");
		IntStream
				.range(0, dfsEdges.length)
				.forEach(i -> out.print(dfsEdges[i] + ","));
		out.println();

		/*
		 * find DFS tree
		 */
		Graph<String, Object> dfsTree = graph.dfsTree(rootNode);
		out.println("DFS tree: " + dfsTree);
		graph.removeNode("g");
		graph.removeNode("h");
		graph.removeNode("i");
		graph.removeNode("j");
		graph.removeEdge("c", "f");

		/*
		 * Connectivity
		 */
		out.println("\nCONNECTIVITY");
		graph.addNode("g");
		graph.addEdge("f", "g");
		out.println("Graph: " + graph);
		out.println("Graph is connected: " + graph.isConnected());
		out.println("Number of connected components: " + graph.numberConnectedComponents());
		out.print("Connected components: [");
		Object[] connectedComponents = graph.connectedComponents();
		IntStream
				.range(0, connectedComponents.length) // iterate over components
				.forEach(i -> {
					out.print("[");
					// extract a component and iterate over it
					String[] component = (String[]) connectedComponents[i];
					IntStream
							.range(0, component.length)
							.forEach(j -> out.print(component[j] + ","));
					out.print("],");
				});
		out.print("]\n");
		out.print("Conncted component of 'a': [");
		String[] connectedComponent = graph.nodeConnectedComponent("a");
		IntStream
			.range(0, connectedComponent.length)
			.forEach(i -> out.print(connectedComponent[i] + ","));
		out.print("]\n");
		out.print("Conncted component of 'f': [");
		String[] connectedComponent2 = graph.nodeConnectedComponent("f");
		IntStream
			.range(0, connectedComponent2.length)
			.forEach(i -> out.print(connectedComponent2[i] + ","));
		out.print("]\n");
		Graph<String, Object>[] connectedSubgraphs = graph.connectedSubgraphs();
		IntStream
				.range(0, connectedSubgraphs.length)
				.forEach(i -> out.println("Subgraph " + i + ": " + connectedSubgraphs[i]));
		out.println("Removing nodes 'f' and 'g'...");
		graph.removeNode("f");
		graph.removeNode("g");
		out.println("Undirected graph: " + graph);
		out.println("Graph is connected: " + graph.isConnected());
		out.println("Number of connected components: " + graph.numberConnectedComponents());

		digraph.addEdge("d", "a");
		digraph.addNode("f");
		digraph.addEdge("e", "f");
		digraph.addEdge("f", "e");
		out.println("\nDirected graph: " + digraph);
		out.println("Digraph is strongly connected: " + digraph.isStronglyConnected());
		out.print("Connected components: [");
		Object[] stronglyConnectedComponents = digraph.stronglyConnectedComponents();
		IntStream
				.range(0, stronglyConnectedComponents.length) // iterate over components
				.forEach(i -> {
					out.print("[");
					// extract a component and iterate over it
					String[] component = (String[]) stronglyConnectedComponents[i];
					IntStream
							.range(0, component.length)
							.forEach(j -> out.print(component[j] + ","));
					out.print("],");
				});
		out.print("]\n");
		Graph<String, Object>[] stronglyConnectedSubgraphs =
				digraph.stronglyConnectedSubgraphs();
		IntStream
				.range(0, stronglyConnectedSubgraphs.length)
				.forEach(i -> {
					out.println("Strong subgraph " + i + ": " + stronglyConnectedSubgraphs[i]);
				});
		out.println("Removing (a, c) and adding (c, a) and (e, c)...");
		digraph.removeEdge("a", "c");
		digraph.addEdge("c", "a");
		digraph.addEdge("e", "c");
		out.println("Directed graph: " + digraph);
		out.println("Digraph is strongly connected: " + digraph.isStronglyConnected());

		/*
		 * DAG and topological sorting
		 */
		out.println("\nDAG AND TOPOLOGICAL SORTING");
		digraph.removeNode("f");
		digraph.removeEdge("d", "a");
		out.println("Digraph: " + digraph);
		out.println("Digraph is DAG: " + digraph.isDAG());
		out.println("Removing edges (c, a) and (e, c)...");
		digraph.removeEdge("c", "a");
		digraph.removeEdge("e", "c");
		out.println("Digraph: " + digraph);
		out.println("Digraph is DAG: " + digraph.isDAG());
		String[] topSort = digraph.topologicalSort();
		out.print("Topological sorting: [");
		IntStream
				.range(0, topSort.length)
				.forEach(i -> out.print(topSort[i] + ","));
		out.print("]\n");

		/*
		 * Minimum spanning tree
		 */
		out.println("\nMINIMUM SPANNING TREE");
		Graph<String, Object> mst = wgraph.getMST();
		out.println("MST of weighted graph: " + mst);

		/*
		 * Lowest common ancestor
		 */
		out.println("\nLOWEST COMMON ANCESTOR");
		String lca = digraph.getLCA("d", "e");
		out.println("Lowest common ancestor of nodes 'd' and 'e': " + lca);
	}

}
