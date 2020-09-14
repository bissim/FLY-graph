package io.github.bissim.fly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.jgrapht.GraphPath;
import org.jgrapht.GraphTests;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.lca.NaiveLCAFinder;
import org.jgrapht.alg.scoring.ClusteringCoefficient;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.GraphMeasurer;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.alg.util.NeighborCache;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.jgrapht.util.SupplierUtil;

import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.GraphExporter;
import org.jgrapht.nio.csv.CSVImporter;
import org.jgrapht.nio.csv.CSVExporter;
import org.jgrapht.nio.csv.CSVFormat;

/**
 * <code>Graph&lt;V, E&gt;</code> is a class used to represent a graph for
 * use into FLY domain-specific language.
 * 
 * It basically wraps the JGraphT representation of graphs according to FLY
 * API for graphs.
 * 
 * @version 1.1.0
 * @author Simone Bisogno
 *&lt;<a href="mailto:s.bisogno10@studenti.unisa.it?cc=s.bisogno90@gmail.com&amp;subject=Java%20FLY%20graph%20library&amp;body=Hello,%0D%0A%0D%0Ayour%20message%20here">s.bisogno10@studenti.unisa.it</a>&gt;
 * 
 * @param <V> Type for nodes
 * @param <E> Type for edges
 *
 */
public class Graph<V, E>
{
	// instance variables
	/**
	 * The JGraphT <code>Graph&lt;V, E&gt;</code> wrapped by this class
	 * @since 1.0.0
	 */
	private org.jgrapht.Graph<V, E> graph;
	/**
	 * The class used for nodes
	 * @since 1.0.0
	 */
	private Class<V> nodeClass;
	/**
	 * The class used for edges
	 * @since 1.0.0
	 */
	private Class<E> edgeClass;
	/**
	 * Denotes whether graph is weighted
	 * @since 1.0.0
	 */
	private boolean isWeighted;
	/**
	 * Denotes whether graph is directed
	 * @since 1.0.0
	 */
	private boolean isDirected;
	/**
	 * Stores information about graph measurements
	 * @since 1.1.0
	 */
	private GraphMeasurer<V, E> graphMeasurer;
	/**
	 * Stores information about graph clustering
	 * @since 1.1.0
	 */
	private ClusteringCoefficient<V, E> clusteringCoefficient;

	/**
	 * The <code>Graph(Class&lt;V&gt;, boolean, boolean)</code> constructs a
	 * graph with node class the same specified as parameter and can be
	 * directed, weighted or both according to boolean parameters.
	 * 
	 * @since 1.0.0
	 * 
	 * @param nodeClass The class of the graph nodes
	 * @param isDirected Denotes whether graph edges will be directed
	 * @param isWeighted Denotes whether graph edges will be weighted
	 */
	public Graph(
			Class<V> nodeClass,
			boolean isDirected,
			boolean isWeighted
	)
	{
		this.isDirected = isDirected;
		this.isWeighted = isWeighted;
		this.nodeClass = nodeClass;
		this.edgeClass = this.setEdgeClass(isWeighted);
		this.graph = this.graphBuilder(
				nodeClass,
				this.edgeClass,
				isDirected,
				isWeighted
		);
	}

	/**
	 * The <code>Graph(Class&lt;V&gt;, boolean, boolean)</code> constructs a
	 * graph with node class the same specified as parameter and can be
	 * directed, weighted or both according to boolean parameters.
	 * 
	 * @since 1.1.0
	 * 
	 * @param nodes The nodes to add to graph
	 * @param nodeClass The class of the graph nodes
	 * @param isDirected Denotes whether graph edges will be directed
	 * @param isWeighted Denotes whether graph edges will be weighted
	 */
	public Graph(
			V[] nodes,
			Class<V> nodeClass,
			boolean isDirected,
			boolean isWeighted
	)
	{
		this(nodeClass, isDirected, isWeighted);
		this.addNodes(nodes);
	}

	/**
	 * The <code>Graph(Class&lt;V&gt;, boolean, boolean)</code> constructs a
	 * graph with node class the same specified as parameter and can be
	 * directed, weighted or both according to boolean parameters.
	 * 
	 * @since 1.1.0
	 * 
	 * @param nodes The nodes to add to graph
	 * @param edges The edges to add to graph
	 * @param nodeClass The class of the graph nodes
	 * @param isDirected Denotes whether graph edges will be directed
	 * @param isWeighted Denotes whether graph edges will be weighted
	 */
	public Graph(
			V[] nodes,
			E[] edges,
			Class<V> nodeClass,
			boolean isDirected,
			boolean isWeighted
	)
	{
		this(nodes, nodeClass, isDirected, isWeighted);
		this.addEdges(edges);
	}

	// public methods

	/**
	 * The <code>clear()</code> method deals with emptying both
	 * graph node set and edge set.
	 * <br>
	 * In version 1.0.0 this method was not daisy-chainable.
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph with emptied node and edge sets
	 */
	public Graph<V, E> clear()
	{
		Set<V> nodes = this.graph
				.vertexSet()
				.stream()
				.collect(Collectors.toSet());
		this.graph.removeAllVertices(nodes);

		return this;
	}

	/*
	 * node methods
	 */

	/**
	 * The <code>addNode(V)</code> method deals with adding specified node
	 * to graph node set.
	 * <br>
	 * In version 1.0.0 this method was not daisy-chainable.
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The node to add to graph
	 * @return The graph with added node
	 */
	public Graph<V, E> addNode(V node)
	{
		this.graph.addVertex(node);

		return this;
	}

	/**
	 * The <code>addNodes(V[])</code> method deals with adding nodes from
	 * specified array to graph node set.
	 * <br>
	 * In version 1.0.0 this method was not daisy-chainable.
	 * 
	 * @since 1.1.0
	 * 
	 * @see #addNode(Object)
	 * 
	 * @param nodes The nodes to add to graph
	 * @return graph with added nodes
	 */
	public Graph<V, E> addNodes(V[] nodes)
	{
		IntStream
				.range(0, nodes.length)
				.forEach(i -> this.addNode(nodes[i]));

		return this;
	}

//	/**
//	 * The <code>addNodes(V...)</code> method deals with adding specified nodes
//	 * to graph node set.
//	 * <br>
//	 * In version 1.0.0 this method was not daisy-chainable.
//	 * 
//	 * @since 1.1.0
//	 * 
//	 * @see #addNode(Object)
//	 * 
//	 * @param nodes The nodes to add to graph
//	 * @return graph with added nodes
//	 */
//	public Graph<V, E> addNodes(V... nodes)
//	{
//		List<V> nodesList = List.of(nodes); // not in Java 8
//		nodesList.stream().forEach(n -> this.addNode(n));
//
//		return this;
//	}

	/**
	 * The <code>nodeDegree(V)</code> method returns the degree of specified
	 * node, as long as it belongs to the graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node of graph which we want to know the degree of
	 * @return The degree of node
	 */
	public int nodeDegree(V node)
	{
		return this.graph.degreeOf(node);
	}

	/**
	 * The <code>nodeInDegree(V)</code> method returns the 'in' degree of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'in' degree of a node one denotes how many edges have the node
	 * as their target.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node of graph which we want to know the 'in' degree of
	 * @return The 'in' degree of node
	 */
	public int nodeInDegree(V node)
	{
		return this.graph.inDegreeOf(node);
	}

	/**
	 * The <code>nodeOutDegree(V)</code> method returns the 'out' degree of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'out' degree of a node one denotes how many edges have the node
	 * as their source.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node of graph which we want to know the 'out' degree of
	 * @return The 'out' degree of node
	 */
	public int nodeOutDegree(V node)
	{
		return this.graph.outDegreeOf(node);
	}

	/**
	 * The {@code neighbourhood} method ...
	 * <br>
	 * In version 1.0.0, it erroneously gave the list of edges the given node
	 * was head or tail of.
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The node which we want to know the neighbourhood of
	 * @return ...
	 */
	public Graph<V, E> neighbourhood(V node)
	{
		return new Graph<V, E>(
					this.nodeClass,
					this.isDirected,
					this.isWeighted
				)
				.addNode(node)
				.addNodes(this.neighbourNodes(node))
				.addEdges(this.neighbourhoodEdges(node));
	}

	/**
	 * The {@code nodeEdges(V)} method returns the adjacent edges of
	 * specified node, as long as it belongs to the graph.
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The node which we want to know the edges of
	 * @return The array of edges having node as source or target
	 */
	@SuppressWarnings("unchecked")
	public E[] nodeEdges(V node)
	{
		return (E[]) this.setToArray(
				this.graph.edgesOf(node),
				this.edgeClass
		);
	}

	/**
	 * The <code>nodeInEdges(V)</code> method returns the 'in' edges of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'in' edges of a node one denotes the edges having the node as
	 * their target.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node which we want to know the 'in edges' of
	 * @return The array of edges having node as target
	 */
	@SuppressWarnings("unchecked")
	public E[] nodeInEdges(V node)
	{
		return (E[]) this.setToArray(
				this.graph.incomingEdgesOf(node),
				this.edgeClass
		);
	}

	/**
	 * The <code>nodeOutEdges(V)</code> method returns the 'out' edges of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'out' edges of a node one denotes the edges having the node as
	 * their source.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node which we want to know the 'out edges' of
	 * @return The array of edges having node as source
	 */
	@SuppressWarnings("unchecked")
	public E[] nodeOutEdges(V node)
	{
		return (E[]) this.setToArray(
				this.graph.outgoingEdgesOf(node),
				this.edgeClass
		);
	}

	/**
	 * The <code>nodeSet()</code> method returns all nodes of the graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The array of graph nodes
	 */
	@SuppressWarnings("unchecked")
	public V[] nodeSet()
	{
		Set<V> nodeSet = this.graph.vertexSet();

		return (V[]) this.setToArray(nodeSet, this.nodeClass);
	}

	/**
	 * The <code>numNodes()</code> method returns the number of nodes
	 * in the graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The number of graph nodes
	 */
	public int numNodes()
	{
		return this.graph.vertexSet().size();
	}

	/**
	 * The <code>removeNode(V)</code> method deals with removing specified
	 * node from graph, if it belongs to it.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node to remove
	 */
	public void removeNode(V node)
	{
		this.graph.removeVertex(node);
	}

	/**
	 * The <code>hasNode(V)</code> method checks whether specified node
	 * belongs to graph or not.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node to check for
	 * @return <code>true</code> if the node belongs to graph,
	 * <code>false</code> otherwise
	 */
	public boolean hasNode(V node)
	{
		return this.graph.containsVertex(node);
	}

	/*
	 * edge methods
	 */

	/**
	 * The <code>addEdge(V, V)</code> method adds an edge to graph which
	 * specified nodes are the source and target of.
	 * <br>
	 * In version 1.0.0 this method was not daisy-chainable.
	 * 
	 * @since 1.1.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @return graph with added edge
	 */
	public Graph<V, E> addEdge(V firstNode, V secondNode)
	{
		this.graph.addEdge(firstNode, secondNode);

		return this;
	}

	/**
	 * The <code>getEdge(V, V)</code> method returns the edge which
	 * specified nodes are the source and target of.
	 * 
	 * @since 1.0.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @return The edge of the graph where <code>firstNode</code> is source
	 * and <code>secondNode</code> is target
	 */
	public E getEdge(V firstNode, V secondNode)
	{
		return this.graph.getEdge(firstNode, secondNode);
	}

	/**
	 * The <code>edgeSet()</code> method returns all edges of the graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The array of graph edges
	 */
	@SuppressWarnings("unchecked")
	public E[] edgeSet()
	{
		Set<E> edgeSet = this.graph.edgeSet();

		return (E[]) this.setToArray(edgeSet, this.edgeClass);
	}

	/**
	 * The <code>numEdges()</code> method returns the number of edges
	 * in the graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The number of graph edges
	 */
	public int numEdges()
	{
		return this.graph.edgeSet().size();
	}

	// TODO comment
	/**
	 * The {@code getEdgeSource(E)} method ...
	 * 
	 * @since 1.0.0
	 * 
	 * @param e The edge which source has to be extracted of
	 * @return The edge source
	 */
	public V getEdgeSource(E e)
	{
		return this.graph.getEdgeSource(e);
	}

	// TODO comment
	/**
	 * The {@code setEdgeSource(E, V)} method ...
	 * 
	 * @since 1.0.0
	 * 
	 * @param e The edge which source has to be set of
	 * @param s The edge source to set
	 */
	public void setEdgeSource(E e, V s)
	{
		V t = this.graph.getEdgeTarget(e);
		this.graph.removeEdge(e);
		this.graph.addEdge(s, t);
	}

	// TODO comment
	/**
	 * The {@code getEdgeTarget(E)} method ...
	 * 
	 * @since 1.0.0
	 * 
	 * @param e The edge which target has to be extracted of
	 * @return The edge target
	 */
	public V getEdgeTarget(E e)
	{
		return this.graph.getEdgeTarget(e);
	}

	// TODO comment
	/**
	 * The {@code setEdgeTarget(E, V)} method ...
	 * 
	 * @since 1.0.0
	 * 
	 * @param e The edge which target has to be set of
	 * @param t The edge target to set
	 */
	public void setEdgeTarget(E e, V t)
	{
		V s = this.graph.getEdgeSource(e);
		this.graph.removeEdge(e);
		this.graph.addEdge(s, t);
	}

	/**
	 * The <code>getEdgeWeight(V, V)</code> method returns the weight of
	 * edge specified by the two given nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @return The weight of the edge
	 */
	public double getEdgeWeight(V firstNode, V secondNode)
	{
		E edge = this.graph.getEdge(firstNode, secondNode);
		return this.graph.getEdgeWeight(edge);
	}

	/**
	 * The <code>setEdgeWeight(V, V, float)</code> method sets the weight
	 * of edge specified by the two given nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @param weight The weight of the edge
	 */
	public void setEdgeWeight(V firstNode, V secondNode, double weight)
	{
		E edge = this.graph.getEdge(firstNode, secondNode);
		this.graph.setEdgeWeight(edge, weight);
	}

	/**
	 * The <code>removeEdge(V, V)</code> method removes the edge
	 * between specified nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 */
	public void removeEdge(V firstNode, V secondNode)
	{
		this.graph.removeEdge(firstNode, secondNode);
	}

	/**
	 * The <code>hasEdge(V, V)</code> method checks whether graph has
	 * an edge between specified nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @return <code>true</code> if the edge belongs to graph,
	 * <code>false</code> otherwise
	 */
	public boolean hasEdge(V firstNode, V secondNode)
	{
		return this.graph.containsEdge(firstNode, secondNode);
	}

	/*
	 * Graph measurement
	 */

	// TODO comment
	/**
	 * The {@code shortestPath(V, V)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param source The source of shortest path to find
	 * @param target The target of shortest path to find
	 * @return The shortest path from source to target
	 */
	@SuppressWarnings("unchecked")
	public E[] shortestPath(V source, V target)
	{
		GraphPath<V, E> shortestPath =
				new DijkstraShortestPath<>(this.graph)
				.getPath(source, target);
		if (shortestPath == null)
		{
			return (E[]) null;
		}
		else {
			return (E[]) shortestPath
				.getEdgeList()
				.toArray();
		}
	}

	// TODO comment
	/**
	 * The {@code getDiameter()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph diameter
	 */
	public double getDiameter()
	{
		return this.graphMeasurer().getDiameter();
	}

	// TODO comment
	/**
	 * The {@code getRadius()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph radius
	 */
	public double getRadius()
	{
		return this.graphMeasurer().getRadius();
	}

	/**
	 * The {@code getCenter()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph center
	 */
	@SuppressWarnings("unchecked")
	public V[] getCenter()
	{
		Set<V> center = this.graphMeasurer().getGraphCenter();

		return (V[]) this.setToArray(center, this.nodeClass);
	}

	/**
	 * The {@code getPeriphery()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph periphery
	 */
	@SuppressWarnings("unchecked")
	public V[] getPeriphery()
	{
		Set<V> periphery = this.graphMeasurer().getGraphPeriphery();

		return (V[]) this.setToArray(periphery, this.nodeClass);
	}

	/**
	 * The {@code getNodeEccentricity(V)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The node whose eccentricity has to be found
	 * @return The node eccentricity
	 */
	public double getNodeEccentricity(V node)
	{
		return this.graphMeasurer().getVertexEccentricityMap().get(node);
	}

	/*
	 * Graph metrics
	 */

	// TODO comment
	// /**
	//  * The ...
	//  * 
	//  * @since 1.1.0
	//  * 
	//  * @return The graph total number of triangles
	//  */
	// public long getNumberOfTriangles()
	// {
	// 	return GraphMetrics.getNumberOfTriangles(this.graph);
	// }

	// TODO comment
	// /**
	//  * An $O(|E|^{3/2})$ algorithm for counting the number of non-trivial triangles in an undirected
	//  * graph. A non-trivial triangle is formed by three distinct vertices all connected to each
	//  * other.
	//  *
	//  * <p>
	//  * For more details of this algorithm see Ullman, Jeffrey: "Mining of Massive Datasets",
	//  * Cambridge University Press, Chapter 10
	//  *
	//  * @since 1.1.0
	//  * @see org.jgrapht.GraphMetrics#getNumberOfTriangles(org.jgrapht.Graph)
	//  * 
	//  * @param graph the input graph
	//  * @param <V> the graph vertex type
	//  * @param <E> the graph edge type
	//  * @return The number of triangles in the graph which node is part of
	//  * @throws NullPointerException if {@code graph} is {@code null}
	//  * @throws IllegalArgumentException if {@code graph} is not undirected
	//  */
	// public long getNumberOfTriangles(V node) // TODO test
	// {
	// 	GraphTests.requireUndirected(this.graph);

	// 	final int sqrtV = (int) Math.sqrt(this.graph.vertexSet().size());

	// 	/**
	// 	 * The set of node neighbour nodes
	// 	 */
	// 	Set<V> vertexSet = Graphs.neighborSetOf(this.graph, node);
	// 	List<V> vertexList = new ArrayList<>(vertexSet); // just consider node neighbourhood
	// 	Graph<V, E> neighbourhood = this.neighbourhood(node);

	// 	/*
	// 	 * The book suggest the following comparator: "Compare vertices based on their degree. If
	// 	 * equal compare them of their actual value, since they are all integers".
	// 	 */

	// 	// Fix vertex order for unique comparison of vertices
	// 	Map<V, Integer> vertexOrder =
	// 		CollectionUtil.newHashMapWithExpectedSize(vertexSet.size()); // neighbourhood
	// 	int k = 0;
	// 	for (V v : vertexSet)
	// 	{
	// 		vertexOrder.put(v, k++);
	// 	}

	// 	Comparator<V> comparator = Comparator
	// 		.comparingInt(neighbourhood::nodeDegree)
	// 		.thenComparingInt(System::identityHashCode)
	// 		.thenComparingInt(vertexOrder::get);

	// 	vertexList.sort(comparator);

	// 	// vertex v is a heavy-hitter iff degree(v) >= sqrtV
	// 	List<V> heavyHitterVertices = vertexList
	// 		.stream()
	// 		.filter(x -> neighbourhood.graph.degreeOf(x) >= sqrtV)
	// 		.collect(Collectors.toCollection(ArrayList::new));

	// 	// count the number of triangles formed from only heavy-hitter vertices
	// 	long numberTriangles = this.naiveCountTriangles(heavyHitterVertices);

	// 	for (E edge : this.neighbourhoodEdges(node))
	// 	{
	// 		V v1 = neighbourhood.getEdgeSource(edge);
	// 		V v2 = neighbourhood.getEdgeTarget(edge);

	// 		if (v1 == v2)
	// 		{
	// 			continue;
	// 		}

	// 		if (
	// 			neighbourhood.graph.degreeOf(v1) < sqrtV ||
	// 			neighbourhood.graph.degreeOf(v2) < sqrtV
	// 		)
	// 		{
	// 			// ensure that v1 <= v2 (swap them otherwise)
	// 			if (comparator.compare(v1, v2) > 0)
	// 			{
	// 				V tmp = v1;
	// 				v1 = v2;
	// 				v2 = tmp;
	// 			}

	// 			for (E e : neighbourhood.graph.edgesOf(v1))
	// 			{
	// 				V u = Graphs.getOppositeVertex(neighbourhood.graph, e, v1);

	// 				// check if the triangle is non-trivial: u, v1, v2 are distinct vertices
	// 				if (u == v1 || u == v2)
	// 				{
	// 					continue;
	// 				}

	// 				/*
	// 				 * Check if v2 <= u and if (u, v2) is a valid edge. If both of them are true,
	// 				 * then we have a new triangle (v1, v2, u) and all three vertices in the
	// 				 * triangle are ordered (v1 <= v2 <= u) so we count it only once.
	// 				 */
	// 				if (
	// 					comparator.compare(v2, u) <= 0 &&
	// 					neighbourhood.graph.containsEdge(u, v2))
	// 				{
	// 					numberTriangles++;
	// 				}
	// 			}
	// 		}
	// 	}

	// 	return numberTriangles;
	// }

	// TODO comment
	// /**
	//  * The {@code getNumberOfTriplets()} method ...
	//  * 
	//  * @since 1.1.0
	//  * @see org.jgrapht.alg.scoring.ClusteringCoefficient#computeGlobalClusteringCoefficient()
	//  * 
	//  * @return The graph number of triplets
	//  */
	// public int getNumberOfTriplets()
	// {
	// 	return this.triplets(null);
	// }

	// TODO comment
	// /**
	//  * The {@code getNumberOfTriplets(V)} method ...
	//  * 
	//  * @since 1.1.0
	//  * @see org.jgrapht.alg.scoring.ClusteringCoefficient#computeGlobalClusteringCoefficient()
	//  * 
	//  * @return The graph number of triplets which node is part of
	//  */
	// public int getNumberOfTriplets(V node)
	// {
	// 	return this.triplets(node);
	// }

	// TODO comment
	/**
	 * The {@code getAverageClusteringCoefficient()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph average clustering coefficient
	 */
	public double getAverageClusteringCoefficient()
	{
		return this.clusteringCoefficient()
				.getAverageClusteringCoefficient();
	}

	// TODO comment
	/**
	 * The {@code getGlobalClusteringCoefficient()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph global clustering coefficient
	 */
	public double getGlobalClusteringCoefficient()
	{
		return this.clusteringCoefficient()
				.getGlobalClusteringCoefficient();
	}

	// TODO comment
	/**
	 * The {@code getNodeClusteringCoefficient(V)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The graph node which clustering coefficient has to be found
	 * @return The graph node clustering coefficient
	 */
	public double getNodeClusteringCoefficient(V node)
	{
		return this.clusteringCoefficient()
				.getVertexScore(node);
	}

	/*
	 * I/O related functions
	 */

	/**
	 * The <code>importGraph(File, String, Class&lt;V&gt;, boolean,
	 * boolean)</code> method imports a graph from a specified file;
	 * class object for nodes has to be specified, along with two boolean
	 * values denoting whether graph is directed or weighted.
	 * <br>
	 * Source file has to be in CSV format: specifically, it has to be an
	 * <em>edge list</em> file listing the edges of graph (and, optionally,
	 * their weight) in such a fashion:<br><br>
	 * <code>a b 2.3</code><br>
	 * <code>b c 1.8</code><br><br>
	 * In this example, two weighted edges have been defined, separated by
	 * space character. Weight has to be specified or not according to
	 * relative boolean value passed.
	 * 
	 * @since 1.0.0
	 * 
	 * @param <V> The class for nodes.
	 * @param <E> The class for edges.
	 * @param file Edgelist file in file system
	 * @param separator Separator character for CSV
	 * @param nodeClass <code>Class&lt;V&gt;</code> object for nodes
	 * @param isWeighted <code>true</code> if graph is weighted,
	 * <code>false</code> otherwise
	 * @param isDirected <code>true</code> if graph is directed,
	 * <code>false</code> otherwise
	 * @return the <code>Graph&lt;V, E&gt;</code> object read from
	 * <code>path</code> file
	 * @throws FileNotFoundException There is no file at specified path
	 * @throws Exception Graph cannot be imported from file
	 */
	@SuppressWarnings("unchecked")
	public static <V, E> Graph<V, E> importGraph(
			File file,
			String separator,
			Class<V> nodeClass,
			boolean isWeighted,
			boolean isDirected
	)
			throws FileNotFoundException, Exception
	{
		Graph<V, E> flyGraph = new Graph<V, E>(
					nodeClass,
					isDirected,
					isWeighted
			);

		CSVImporter<V, E> importer = new CSVImporter<>(
					CSVFormat.EDGE_LIST,
					separator.charAt(0)
			);
		// easy fix for real graph import
		// as per https://stackoverflow.com/questions/61089620/
		// effective since JGraphT 1.4.1
		importer.setVertexFactory(id -> (V) id);

		// tell importer that graph to import is weighted
		if (isWeighted)
		{
			importer.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, true);
		}

		try
		{
			importer.importGraph(
					flyGraph.graph,
					new FileReader(file)
			);
		}
		catch (ImportException e)
		{
			// we need to hide ImportException because
			// caller mustn't be aware of the use of
			// JGraphT library, that's why we're relaunching
			// it as a messaged RuntimeException
			throw new Exception(
					e.getClass().getSimpleName() +
					": " +
					e.getLocalizedMessage()
			);
		}

		return flyGraph;
	}

	/**
	 * The <code>importGraph(String, String, Class&lt;V&gt;, boolean,
	 * boolean)</code> method imports a graph from a file at specified path;
	 * class object for nodes has to be specified, along with two boolean
	 * values denoting whether graph is directed or weighted.
	 * <br>
	 * Source file has to be in CSV format: specifically, it has to be an
	 * <em>edge list</em> file listing the edges of graph (and, optionally,
	 * their weight) in such a fashion:<br><br>
	 * <code>a b 2.3</code><br>
	 * <code>b c 1.8</code><br><br>
	 * In this example, two weighted edges have been defined, separated by
	 * space character. Weight has to be specified or not according to
	 * relative boolean value passed.
	 * 
	 * @since 1.0.0
	 * 
	 * @param <V> The class for nodes.
	 * @param <E> The class for edges.
	 * @param path Position of edgelist file in file system
	 * @param separator Separator character for CSV
	 * @param nodeClass <code>Class&lt;V&gt;</code> object for nodes
	 * @param isWeighted <code>true</code> if graph is weighted,
	 * <code>false</code> otherwise
	 * @param isDirected <code>true</code> if graph is directed,
	 * <code>false</code> otherwise
	 * @return the <code>Graph&lt;V, E&gt;</code> object read from
	 * <code>path</code> file
	 * @throws FileNotFoundException There is no file at specified path
	 * @throws Exception Graph cannot be imported from file
	 */
	public static <V, E> Graph<V, E> importGraph(
			String path,
			String separator,
			Class<V> nodeClass,
			boolean isWeighted,
			boolean isDirected
	)
			throws FileNotFoundException, Exception
	{
		File file = new File(path);
		return Graph.importGraph(
				file,
				separator,
				nodeClass,
				isWeighted,
				isDirected
		);
	}

	/**
	 * The <code>exportGraph(Graph&lt;V, E&gt;, String, String, boolean,
	 * boolean)</code> method exports the graph to a file at a specified path.
	 * Destination file will be in CSV format: specifically, it will be an
	 * <em>edge list</em> file listing the edges of graph (and, optionally,
	 * their weight) in such a fashion:<br><br>
	 * <code>a b 2.3</code><br>
	 * <code>b c 1.8</code><br><br>
	 * 
	 * @since 1.0.0
	 * 
	 * @param <V> The class for nodes.
	 * @param <E> The class for edges.
	 * @param flyGraph <code>Graph&gt;V, E&lt;</code> object to write into file
	 * @param path Destination for edgelist file in file system
	 * @param separator Separator character for CSV
	 * @throws Exception Graph cannot be exported to file
	 */
	public static <V, E> void exportGraph(
			Graph<V, E> flyGraph,
			String path,
			String separator
	)
			throws Exception
	{
//		ComponentNameProvider<V> vertexIdProvider =
//				new ComponentNameProvider<V>()
//				{
//					public String getName(V id)
//					{
//						return id.toString();
//					}
//				};

		GraphExporter<V, E> exporter =
				new CSVExporter<>(
						(V v) -> v.toString(),
						CSVFormat.EDGE_LIST,
						separator.charAt(0)
				);

		if (flyGraph.isWeighted)
		{
			((CSVExporter<V, E>) exporter)
					.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, true);
		}

		try
		{
			exporter.exportGraph(
					flyGraph.graph,
					new File(path)
			);
		}
		catch (ExportException e)
		{
			// like in importGraph method, we have to
			// hide usage of JGraphT exception classes
			throw new Exception(
					e.getClass().getSimpleName() +
					": " +
					e.getLocalizedMessage()
			);
		}
	}

	/*
	 * Graph traversal
	 */

	/**
	 * The <code>bfsEdges(V)</code> method returns edges of the BFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of BFS tree
	 * @return Array of edges of BFS tree
	 */
	public E[] bfsEdges(V rootNode)
	{
		Pair[] nodePairs = this.extractBfsEdges(rootNode);

		@SuppressWarnings("unchecked")
		E[] bfsEdges = (E[]) Array.newInstance(
				this.edgeClass,
				nodePairs.length
		);

		IntStream
				.range(0, nodePairs.length)
				.forEach(i -> {
					bfsEdges[i] = this.getEdge(
							nodePairs[i].getFirst(),
							nodePairs[i].getSecond()
					);
				});

		return bfsEdges;
	}

	/**
	 * The <code>bfsNodes(V)</code> method returns nodes of the BFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of BFS tree
	 * @return Array of nodes of BFS tree
	 */
	public V[] bfsNodes(V rootNode)
	{
		BreadthFirstIterator<V, E> iterator = new BreadthFirstIterator<>(
						this.graph,
						rootNode
				);

		// count iterator elements
		int numNodes = 0;
		while (iterator.hasNext())
		{
			iterator.next(); // drop nodes
			numNodes++;
		}

		// create array of V elements to return
		// these elements are BFSed ones
		@SuppressWarnings("unchecked")
		V[] bfsNodes = (V[]) Array.newInstance(
				this.nodeClass,
				numNodes // number of elements of the iterator
		);
		BreadthFirstIterator<V, E> iterator2 = new BreadthFirstIterator<>(
						this.graph,
						rootNode
				);

		IntStream
				.range(0, numNodes)
				.forEach(i -> {
					bfsNodes[i] = iterator2.next();
				});

		return bfsNodes;
	}

	/**
	 * The <code>bfsTree(V)</code> method returns the BFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of BFS tree
	 * @return BFS tree
	 */
	public Graph<V, E> bfsTree(V rootNode)
	{
		Graph<V, E> bfsTree = new Graph<>(
				this.nodeClass,
				this.isDirected,
				this.isWeighted
		);

		// add BFS nodes to graph
		bfsTree.addNodes(this.bfsNodes(rootNode));

		// add BFS edges to graph
		Pair[] nodePairs = this.extractBfsEdges(rootNode);
		IntStream
				.range(0, nodePairs.length)
				.forEach(i -> {
					bfsTree.addEdge(
							nodePairs[i].getFirst(),
							nodePairs[i].getSecond()
					);
				});

		return bfsTree;
	}

	/**
	 * The <code>dfsEdges(V)</code> method returns edges of the DFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of DFS tree
	 * @return Set of edges of DFS tree
	 */
	public E[] dfsEdges(V rootNode)
	{
		Pair[] nodePairs = this.extractDfsEdges(rootNode);

		@SuppressWarnings("unchecked")
		E[] dfsEdges = (E[]) Array.newInstance(
				this.edgeClass,
				nodePairs.length
		);

		IntStream
				.range(0, nodePairs.length)
				.forEach(i -> {
					dfsEdges[i] = this.getEdge(
							nodePairs[i].getFirst(),
							nodePairs[i].getSecond()
					);
				});

		return dfsEdges;
	}

	/**
	 * The <code>dfsNodes(V)</code> method returns nodes of the DFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of DFS tree
	 * @return Set of nodes of DFS tree
	 */
	public V[] dfsNodes(V rootNode)
	{
		DepthFirstIterator<V, E> iterator = new DepthFirstIterator<>(
				this.graph,
				rootNode
		);

		// count iterator elements
		int numNodes = (int) StreamSupport
				.stream(
						Spliterators.spliteratorUnknownSize(
								iterator,
								Spliterator.ORDERED
						),
						false
				)
				.count();

		// create array of V elements to return
		// these elements are DFSed ones
		@SuppressWarnings("unchecked")
		V[] dfsNodes = (V[]) Array.newInstance(
				this.nodeClass,
				numNodes // number of elements of the iterator
		);
		DepthFirstIterator<V, E> iterator2 = new DepthFirstIterator<>(
						this.graph,
						rootNode
				);

		IntStream
				.range(0, numNodes)
				.forEach(i -> {
					dfsNodes[i] = iterator2.next();
				});

		return dfsNodes;
	}

	/**
	 * The <code>dfsTree(V)</code> method returns the DFS tree
	 * rooted in specified node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param rootNode Desired root node of DFS tree
	 * @return DFS tree
	 */
	public Graph<V, E> dfsTree(V rootNode)
	{
		Graph<V, E> dfsTree = new Graph<V, E>(
				this.nodeClass,
				this.isDirected,
				this.isWeighted
		);

		// add DFS nodes to graph
		dfsTree.addNodes(this.dfsNodes(rootNode));

		// add DFS edges to graph
		Pair[] nodePairs = this.extractDfsEdges(rootNode);
		IntStream
				.range(0, nodePairs.length)
				.forEach(i -> {
					dfsTree.addEdge(
							nodePairs[i].getFirst(),
							nodePairs[i].getSecond()
					);
				});

		return dfsTree;
	}

	/*
	 * Connectivity
	 */

	/**
	 * The <code>isConnected()</code> method indicates whether graph is
	 * connected.
	 * 
	 * @since 1.0.0
	 * 
	 * @return <code>true</code> if the graph is connected,
	 * <code>false</code> otherwise
	 */
	public boolean isConnected()
	{
		ConnectivityInspector<V, E> inspector =
				new ConnectivityInspector<>(this.graph);

		return inspector.isConnected();
	}

	/**
	 * The <code>isStronglyConnected()</code> method indicates whether graph
	 * is strongly connected.
	 * 
	 * @since 1.0.0
	 * 
	 * @return <code>true</code> if the graph is strongly connected,
	 * <code>false</code> otherwise
	 */
	public boolean isStronglyConnected()
	{
		KosarajuStrongConnectivityInspector<V, E> inspector =
				new KosarajuStrongConnectivityInspector<>(this.graph);

		return inspector.isStronglyConnected();
	}

	/**
	 * The <code>connectedComponents()</code> method returns connected
	 * components of graph as groups of nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @return array of arrays of connected nodes
	 */
	public Object[] connectedComponents()
	{
		ConnectivityInspector<V, E> inspector =
				new ConnectivityInspector<>(this.graph);
		List<Set<V>> connectedSets = inspector.connectedSets();
		int nComps = connectedSets.size();

		Object[] components = (Object[]) Array.newInstance(
				Object.class,
				nComps
		);

		IntStream
				.range(0, nComps) // iterate over connected components
				.forEach(i -> {
					Set<V> component = connectedSets.get(i);
					@SuppressWarnings("unchecked")
					V[] nodes = (V[]) Array.newInstance(
							this.nodeClass,
							component.size()
					);
					Iterator<V> componentIterator = component.iterator();
					IntStream
							// iterate over component nodes
							.range(0, component.size())
							.forEach(j -> {
								nodes[j] = componentIterator.next();
							});
					components[i] = nodes;
				});

		return components;
	}

	/**
	 * The <code>connectedSubgraphs()</code> method returns connected
	 * subgraphs of graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return array of connected subgraphs
	 */
	@SuppressWarnings("unchecked")
	public Graph<V, E>[] connectedSubgraphs()
	{
		int numConnComps = this.numberConnectedComponents();
		Graph<V, E>[] subgraphs = new Graph[numConnComps];
		Object[] components = this.connectedComponents();

		// add subgraph nodes and edges
		IntStream
				.range(0, numConnComps)
				.forEach(i -> {
					subgraphs[i] = new Graph<>(
							this.nodeClass,
							this.isDirected,
							this.isWeighted
					);
					subgraphs[i].addNodes((V[]) components[i]);
					subgraphs[i].addEdges(this.edgeSet());
				});

		return subgraphs;
	}

	/**
	 * The <code>numberConnectedComponents()</code> method returns number of
	 * connected components of graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return number of connected components of the graph
	 */
	public int numberConnectedComponents()
	{
		ConnectivityInspector<V, E> inspector =
				new ConnectivityInspector<>(this.graph);

		return inspector.connectedSets().size();
	}

	/**
	 * The <code>nodeConnectedComponent(V)</code> method returns connected
	 * component of graph for given node.
	 * 
	 * @since 1.0.0
	 * 
	 * @param node The node which we want to know the connected component of
	 * @return the connected component given node belongs to
	 */
	@SuppressWarnings("unchecked")
	public V[] nodeConnectedComponent(V node)
	{
		ConnectivityInspector<V, E> inspector =
				new ConnectivityInspector<>(this.graph);

		Set<V> nodeConnectedSet = inspector.connectedSetOf(node);
		return (V[]) this.setToArray(nodeConnectedSet, this.nodeClass);
	}

	/**
	 * The <code>stronglyConnectedComponents()</code> method returns strongly
	 * connected components of graph as group of nodes.
	 * 
	 * @since 1.0.0
	 * 
	 * @return array of arrays of strongly connected components
	 */
	public Object[] stronglyConnectedComponents()
	{
		KosarajuStrongConnectivityInspector<V, E> inspector =
				new KosarajuStrongConnectivityInspector<>(this.graph);
		List<Set<V>> strConnSets = inspector.stronglyConnectedSets();
		int nComps = strConnSets.size();

		Object[] strongComponents = (Object[]) Array.newInstance(
				Object.class,
				nComps
		);


		IntStream
			.range(0, nComps) // iterate over connected components
			.forEach(i -> {
				Set<V> strongComponent = strConnSets.get(i);
				@SuppressWarnings("unchecked")
				V[] nodes = (V[]) Array.newInstance(
						this.nodeClass,
						strongComponent.size()
				);
				Iterator<V> componentIterator = strongComponent.iterator();
				IntStream // iterate over component nodes
						.range(0, strongComponent.size())
						.forEach(j -> nodes[j] = componentIterator.next());
				strongComponents[i] = nodes;
			});

		return strongComponents;
	}

	/**
	 * The <code>stronglyConnectedSubgraphs()</code> method returns strongly
	 * connected subgraphs of graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return array of strongly connected subgraphs
	 */
	public Graph<V, E>[] stronglyConnectedSubgraphs()
	{
		KosarajuStrongConnectivityInspector<V, E> inspector =
				new KosarajuStrongConnectivityInspector<>(this.graph);
		List<Set<V>> strConnSets = inspector.stronglyConnectedSets();
		int nComps = strConnSets.size();
		@SuppressWarnings("unchecked")
		Graph<V, E>[] subgraphs = new Graph[nComps];
		List<org.jgrapht.Graph<V, E>> strongGraphs =
				inspector.getStronglyConnectedComponents();

		// add subgraph nodes and edges
		IntStream
				.range(0, nComps)
				.forEach(i -> {
					subgraphs[i] = new Graph<>(
							this.nodeClass,
							this.isDirected,
							this.isWeighted
					);
					subgraphs[i].graph = strongGraphs.get(i);
				});

		return subgraphs;
	}

	/*
	 * DAG and topological sorting
	 */

	/**
	 * The <code>isDAG()</code> method checks whether graph is a directed
	 * acyclic graph (DAG).
	 * 
	 * @since 1.0.0
	 * 
	 * @return <code>true</code> if graph is DAG,
	 * <code>false</code> otherwise
	 */
	public boolean isDAG()
	{
		CycleDetector<V, E> cycleDetector = new CycleDetector<>(this.graph);

		return !cycleDetector.detectCycles();
	}

	/**
	 * The <code>topologicalSort()</code> method returns the list of nodes
	 * ordered according to topological sort.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The array of topological sorted nodes
	 */
	public V[] topologicalSort()
	{
		TopologicalOrderIterator<V, E> iterator =
				new TopologicalOrderIterator<>(this.graph);

		@SuppressWarnings("unchecked")
		V[] nodes = (V[]) Array.newInstance(
				this.nodeClass,
				this.numNodes() // topological order includes all graph ndes
		);

		for (int i = 0; iterator.hasNext(); i++)
		{
			nodes[i] = iterator.next();
		}

		return nodes;
	}

	/*
	 * Minimum spanning tree
	 */

	/**
	 * The <code>getMST()</code> method returns the minimum spanning tree
	 * of graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @return The graph minimum spanning tree
	 */
	public Graph<V, E> getMST()
	{
		PrimMinimumSpanningTree<V, E> spanningTreeAlgorithm =
				new PrimMinimumSpanningTree<>(this.graph);

		Iterator<E> edgesIterator = spanningTreeAlgorithm
				.getSpanningTree()
				.iterator();

		Graph<V, E> mst = new Graph<>(
				this.nodeClass,
				this.isDirected,
				this.isWeighted
		);

		mst.addNodes(this.nodeSet());
		edgesIterator.forEachRemaining(e -> {
			mst.addEdge(
					this.graph.getEdgeSource(e),
					this.graph.getEdgeTarget(e)
			);
		});

		return mst;
	}

	/*
	 * Lowest common ancestor
	 */

	// TODO comment
	/**
	 * The {@code getLCA(V, V)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param node1 The graph first node which LCA has to be found of
	 * @param node2 The graph second node which LCA has to be found of
	 * @return The LCA of nodes
	 */
	public V getLCA(V node1, V node2)
	{
		GraphTests.requireDirected(this.graph);
		return new NaiveLCAFinder<V, E>(this.graph).getLCA(node1, node2);
	}

	/*
	 * Object methods
	 */

	/**
	 * The <code>toString()</code> method returns a string
	 * representation of <code>Graph&lt;V, E&gt;</code> object.
	 * 
	 * @since 1.0.0
	 * 
	 * @return Representation of <code>Graph&lt;V, E&gt;</code> object
	 */
	@Override
	public String toString()
	{
		String stringGraph = this.graph.toString();

		if (this.isDirected)
		{
			stringGraph += ", directed";
		}

		if (this.isWeighted)
		{
			stringGraph += ", weighted";
		}

		return stringGraph;
	}

	/**
	 * The <code>equals(Object)</code> method indicates whether some other
	 * 'object' is equal to this graph.
	 * 
	 * @since 1.0.0
	 * 
	 * @param obj Another <code>Graph&lt;V, E&gt;</code> object
	 * @return <code>true</code> if <code>obj</code> is equal to graph,
	 * <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Graph<V, E> other = (Graph<V, E>) obj;
		if (edgeClass == null) {
			if (other.edgeClass != null)
				return false;
		} else if (!edgeClass.equals(other.edgeClass))
			return false;
		if (graph == null) {
			if (other.graph != null)
				return false;
		} else if (!graph.equals(other.graph))
			return false;
		if (isDirected != other.isDirected)
			return false;
		if (isWeighted != other.isWeighted)
			return false;
		if (nodeClass == null) {
			if (other.nodeClass != null)
				return false;
		} else if (!nodeClass.equals(other.nodeClass))
			return false;
		return true;
	}

	/**
	 * The <code>hashCode()</code> method  calculates the
	 * hash code for a <code>Graph&lt;V, E&gt;</code> object.
	 * 
	 * @since 1.0.0
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (
			(edgeClass == null) ? 0 : edgeClass.hashCode()
		);
		result = prime * result + ((graph == null) ? 0 : graph.hashCode());
		result = prime * result + (isDirected ? 1231 : 1237);
		result = prime * result + (isWeighted ? 1231 : 1237);
		result = prime * result + (
			(nodeClass == null) ? 0 : nodeClass.hashCode()
		);
		return result;
	}

	/*
	 * helper methods
	 */

	@SuppressWarnings("unchecked")
	private E[] neighbourhoodEdges(V node)
	{
		Set<E> neighbourhoodEdgesSet = new HashSet<>();

		// let's start by adding edges incidents to given node
		neighbourhoodEdgesSet.addAll(this.graph.edgesOf(node));

		// then retrieve neighbour nodes and pick
		// the edges between them and given node
		// and among them
		Set<V> neighbourNodesSet = Graphs.neighborSetOf(this.graph, node);
		this.graph.edgeSet().forEach(e -> {
			V source = this.getEdgeSource(e);
			V target = this.getEdgeTarget(e);
			if (
				neighbourNodesSet.contains(source) &&
				neighbourNodesSet.contains(target)
			)
			{
				neighbourhoodEdgesSet.add(e);
			}
		});

		// now turn the set into array and return it
		return (E[]) this.setToArray(neighbourhoodEdgesSet, this.edgeClass);
	}

	/**
	 * The <code>neighbourNodes(V)</code> method returns the list of nodes
	 * which are adjacent to given node.
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The node which we want to know the neighbourhood of
	 * @return The array of edges having node as source or target
	 */
	@SuppressWarnings("unchecked")
	private V[] neighbourNodes(V node)
	{
		Set<V> nodesSet = Graphs.neighborSetOf(this.graph, node);

		// convert set of nodes into array of nodes
		return (V[]) this.setToArray(nodesSet, this.nodeClass);
	}

	/**
	 * The {@code naiveCountTriangles(List<V>)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param vertexSubset The node set which number of triangles has
	 * to be found of
	 * @return The number of triangles for graph nodes subset
	 */
	@SuppressWarnings("unused")
	private long naiveCountTriangles(List<V> vertexSubset)
	{
		long total = 0;

		for (int i = 0; i < vertexSubset.size(); i++)
		{
			for (int j = i + 1; j < vertexSubset.size(); j++)
			{
				for (int k = j + 1; k < vertexSubset.size(); k++)
				{
					V u = vertexSubset.get(i);
					V v = vertexSubset.get(j);
					V w = vertexSubset.get(k);

					if (
						this.graph.containsEdge(u, v) &&
						this.graph.containsEdge(v, w) &&
						this.graph.containsEdge(w, u)
					)
					{
						total++;
					}
				}
			}
		}

		return total;
	}

	/**
	 * The {@code triplets(V)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param node The graph node which triplets number has to be found of
	 * @return The graph node number of triplets
	 */
	@SuppressWarnings("unused")
	private int triplets(V node)
	{
		org.jgrapht.Graph<V, E> graph;
		if (node == null)
		{
			graph = this.graph;
		}
		else
		{
			graph = this.neighbourhood(node).graph;
		}
		NeighborCache<V, E> neighborCache = new NeighborCache<>(graph);
		// array needed to overcome final variable issue
		// in 'forEach'
		int[] numberTriplets = {0};

		graph.vertexSet().forEach(v -> {
			if (graph.getType().isUndirected()) {
				int nodeDegree = graph.degreeOf(v);
				numberTriplets[0] += 1.0 * nodeDegree * (nodeDegree - 1) / 2;
			} else {
				numberTriplets[0] += 1.0 * neighborCache.predecessorsOf(v).size()
					* neighborCache.successorsOf(v).size();
			}
		});

		return numberTriplets[0];
	}

	/**
	 * The {@code graphMeasurer()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return The graph measurer
	 */
	private GraphMeasurer<V, E> graphMeasurer()
	{
		if (this.graphMeasurer == null)
		{
			this.graphMeasurer = new GraphMeasurer<>(this.graph);
		}

		return this.graphMeasurer;
	}

	// TODO comment
	/**
	 * The {@code clusteringCoefficient()} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @return Tge graph clustering coefficient object
	 */
	private ClusteringCoefficient<V, E> clusteringCoefficient()
	{
		if (this.clusteringCoefficient == null)
		{
			this.clusteringCoefficient = new ClusteringCoefficient<>(this.graph);
		}

		return this.clusteringCoefficient;
	}

	/**
	 * The <code>addEdges()</code> helper method adds edges objects to graph;
	 * it first convert every edge into its couple of nodes and then invokes
	 * the {@link #addEdge(Object, Object)} method for every couple of nodes.
	 * <b>
	 * In version 1.0.0 this method was not daisy-chainable.
	 * 
	 * @since 1.1.0
	 * 
	 * @see #addEdge(Object, Object)
	 * 
	 * @param edges Array of edges to add to graph
	 * @return graph with added edges
	 */
	private Graph<V, E> addEdges(E[] edges)
	{
		IntStream
				.range(0, edges.length)
				.forEach(i -> {
					try {
						this.addEdge(
							this.graph.getEdgeSource(edges[i]),
							this.graph.getEdgeTarget(edges[i])
						);
					}
					catch (IllegalArgumentException iae)
					{
						System.err.print(
								iae.getClass().getSimpleName() +
								": " +
								iae.getLocalizedMessage() +
								".\n"
						);
					}
				});

		return this;
	}

//	/**
//	 * The <code>addEdges(E...)</code> method ...
//	 * 
//	 * @since 1.0.0
//	 * 
//	 * @param edges The edges to add to graph
//	 */
//	private void addEdges(E... edges)
//	{
//		List<E> edgesList = List.of(edges); // not in Java 8
//		edgesList.stream().forEach(e -> this.addEdge(firstNode, secondNode));
//	}

	/**
	 * The <code>graphBuilder()</code> helper method constructs an instance
	 * of <code>org.jgrapht.Graph&lt;V, E&gt;</code> by specifying the
	 * node class, the edge class, whether graph is directed, whether graph
	 * is weighted.
	 * 
	 * @since 1.0.0
	 * 
	 * @see org.jgrapht.Graph
	 * 
	 * @param nodeClass Class of nodes
	 * @param edgeClass Class of edges
	 * @param isDirected Denotes whether graph edges will be directed
	 * @param isWeighted Denotes whether graph edges will be weighted
	 * @return a JGraphT <code>org.jgrapht.Graph&lt;V, E&gt;</code> object
	 */
	@SuppressWarnings("unchecked")
	private org.jgrapht.Graph<V, E> graphBuilder(
			Class<V> nodeClass,
			Class<E> edgeClass,
			boolean isDirected,
			boolean isWeighted
	)
	{
		GraphTypeBuilder<V, E> builder = null;

		if (isDirected)
		{
			builder = GraphTypeBuilder.<V, E>directed();
		}
		else
		{
			builder = GraphTypeBuilder.<V, E>undirected();
		}

		// apply other graph attributed
		builder
				.weighted(isWeighted)
				.edgeClass(edgeClass)
				.vertexClass(nodeClass)
				.edgeSupplier(SupplierUtil.createSupplier(edgeClass));

		// determine the node supplier to use
		if (nodeClass == String.class)
		{
			builder.vertexSupplier(
					(Supplier<V>) SupplierUtil.createStringSupplier()
			);
		}
		else if (nodeClass == Integer.class)
		{
			builder.vertexSupplier(
					(Supplier<V>) SupplierUtil.createIntegerSupplier()
			);
		}
		else if (nodeClass == Long.class)
		{
			builder.vertexSupplier(
					(Supplier<V>) SupplierUtil.createLongSupplier()
			);
		}
		else
		{
			builder.vertexSupplier(SupplierUtil.createSupplier(nodeClass));
		}

		return builder.buildGraph();
	}

	/**
	 * The <code>setEdgeClass</code> helper method deals with choosing the
	 * edge class for graph whether it is weighted or not: in the first case,
	 * it returns the class object of {@link DefaultWeightedEdge}; in the
	 * second case, it returns the class object of {@link DefaultEdge}.
	 * 
	 * @since 1.0.0
	 * 
	 * @see DefaultEdge
	 * @see DefaultWeightedEdge
	 * 
	 * @param isWeighted Denotes whether graph edges will be weighted
	 * @return a class between <code>DefaultEdge</code> and
	 * <code>DefaultWeightedEdge</code>
	 */
	@SuppressWarnings("unchecked")
	private Class<E> setEdgeClass(boolean isWeighted)
	{
		if (isWeighted)
			return (Class<E>) DefaultWeightedEdge.class;
		else
			return (Class<E>) DefaultEdge.class;
	}

	/**
	 * The <code>edgeArrayFromSet</code> helper method deals with creating an
	 * array of edges from a specified set.
	 * 
	 * @deprecated
	 * @since 1.0.0
	 * 
	 * @param edgeSet A set of edges
	 * @return The array of edges
	 */
	@SuppressWarnings("unused")
	private E[] edgeArrayFromSet(Set<E> edgeSet)
	{
		@SuppressWarnings("unchecked")
		E[] edgeArray = (E[]) Array.newInstance(
				this.edgeClass,
				edgeSet.size()
		);
		Iterator<E> setIterator = edgeSet.iterator();
		IntStream
				.range(0, edgeArray.length)
				.forEach(i -> edgeArray[i] = setIterator.next());

		return edgeArray;
	}

	/**
	 * The <code>extractBfsEdges(V)</code> helper method deals with converting
	 * the sequence of nodes from BFS visit into pairs of nodes; such a
	 * sequence will be converted in a proper sequence of edges by
	 * <code>{@link #bfsEdges(Object)}</code> public method.
	 * 
	 * @since 1.0.0
	 * 
	 * @see #bfsEdges(Object)
	 * 
	 * @param rootNode Root node of BFS tree
	 * @return the array of <code>Pair</code> of nodes representing edges
	 */
	private Pair[] extractBfsEdges(V rootNode)
	{
		V[] bfsNodes = this.bfsNodes(rootNode);

		BreadthFirstIterator<V, E> iterator = new BreadthFirstIterator<>(
						this.graph,
						rootNode
				);

		// visit graph
		while (iterator.hasNext()) iterator.next();

		@SuppressWarnings("unchecked")
		Pair[] bfsEdges = (Pair[]) Array.newInstance(
				Pair.class,
				bfsNodes.length - 1 // # of tree edges
		);

		int[] nodeLevels = new int[bfsNodes.length];
		IntStream
				.range(0, bfsNodes.length)
				.forEach(i -> {
					nodeLevels[i] = iterator.getDepth(bfsNodes[i]);
				});

		// unfortunate algorithm to find edges
		// within adjacency matrix
		int edgeCounter = 0;
		int jStartPoint = 0;
		for (int i = 0; i < nodeLevels.length - 1; i++)
		{
			if (edgeCounter == bfsEdges.length) break;

			// we only visit upper triangle of adjacency
			// matrix if graph is undirected because of
			// diagonal simmetry
			if (!this.isDirected) jStartPoint = i + 1;

			for (int j = jStartPoint; j < nodeLevels.length; j++)
			{
				if (edgeCounter == bfsEdges.length) break;

				if (
						nodeLevels[j] - nodeLevels[i] == 1 &&
						this.hasEdge(bfsNodes[i], bfsNodes[j])
				)
				{
					bfsEdges[edgeCounter++] = new Pair(
							bfsNodes[i],
							bfsNodes[j]
					);
				}
			}
		}

		return bfsEdges;
	}

	/**
	 * The <code>extractDfsEdges(V)</code> helper method deals with converting
	 * the sequence of nodes from DFS visit into pairs of nodes; such a
	 * sequence will be converted in a proper sequence of edges by
	 * <code>{@link #dfsEdges(Object)}</code> public method.
	 * 
	 * @since 1.0.0
	 * 
	 * @see #dfsEdges(Object)
	 * 
	 * @param rootNode Root node of DFS tree
	 * @return the array of <code>Pair</code> of nodes representing edges
	 */
	private Pair[] extractDfsEdges(V rootNode)
	{
		V[] dfsNodes = this.dfsNodes(rootNode);

		DepthFirstIterator<V, E> iterator = new DepthFirstIterator<>(
						this.graph,
						rootNode
				);

		@SuppressWarnings("unchecked")
		Pair[] dfsEdges = (Pair[]) Array.newInstance(
				Pair.class,
				dfsNodes.length - 1 // # of tree edges
		);

		// try to extract edges from iterator
		V firstNode = null, secondNode = null;
		int edgesCounter = 0;
		int backwardIndex = 0;
		while (
				iterator.hasNext() &&
				edgesCounter < dfsEdges.length
		)
		{
			// nodes have to be picked in couples
			// where the first element is the second
			// one from previous couple
			// if this is the first iteration, extract
			// first element from iterator
			if (firstNode == null)
			{
				firstNode = iterator.next();
			}
			else
			{
				firstNode = secondNode;
			}

			secondNode = iterator.next();

			if (this.hasEdge(firstNode, secondNode))
			{
				dfsEdges[edgesCounter++] = new Pair(firstNode, secondNode);
			}
			else
			{
				// the node in firstNode list that is
				// parent of secondNode has to be found
				backwardIndex = edgesCounter - 1;
				while (backwardIndex >= 0)
				{
					if (
							this.hasEdge(
									dfsEdges[backwardIndex].getFirst(),
									secondNode
							)
					)
					{
						dfsEdges[edgesCounter++] = new Pair(
								dfsEdges[backwardIndex].getFirst(),
								secondNode
						);
						break;
					}
					backwardIndex--;
				}
			}
		}

		return dfsEdges;
	}

	/**
	 * The {@code setToArray(Set)} method ...
	 * 
	 * @since 1.1.0
	 * 
	 * @param set The set to convert to array
	 * @param setClass The class of elements of set to convert
	 * @return The array of elements from set
	 */
	private Object[] setToArray(Set<?> set, Class<?> setClass)
	{
		Object[] array = (Object[]) Array.newInstance(
				setClass,
				set.size()
		);
		Iterator<?> setIterator = set.iterator();
		IntStream
				.range(0, array.length)
				.forEach(i -> array[i] = setIterator.next());

		return array;
	}

	/**
	 * This class models a pair of generic objects.
	 * <br />
	 * For purposes within <code>{@link Graph}</code> class,
	 * instances of <code>Pair</code> will be immutable; in
	 * particular, class itself will be immutable.
	 * 
	 * @since 1.0.0
	 * 
	 * @author Simone Bisogno &lt;s.bisogno10@studenti.unisa.it&gt;
	 *
	 */
	private final class Pair
	{
		/*
		 *  instance variables
		 */

		/**
		 * @since 1.0.0
		 * First element of the pair.
		 */
		private V first;
		/**
		 * @since 1.0.0
		 * Second element of the pair.
		 */
		private V second;

		/*
		 * constructor
		 */

		/**
		 * Build a pair of <code>V</code> objects.
		 * 
		 * @since 1.0.0
		 * 
		 * @param first pair first element
		 * @param second pair second element
		 */
		public Pair(V first, V second)
		{
			this.first = first;
			this.second = second;
		}

		/**
		 * Return fist element of pair.
		 * 
		 * @since 1.0.0
		 * 
		 * @return pair first element
		 */
		public V getFirst()
		{
			return this.first;
		}

		/**
		 * Return second element of pair.
		 * 
		 * @since 1.0.0
		 * 
		 * @return pair second element
		 */
		public V getSecond()
		{
			return this.second;
		}

		/*
		 * Object methods
		 */

		@Override
		public String toString()
		{
			return "(" + this.first + ", " + this.second + ")";
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			Pair other = (Pair) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (first == null) {
				if (other.first != null)
					return false;
			} else if (!first.equals(other.first))
				return false;
			if (second == null) {
				if (other.second != null)
					return false;
			} else if (!second.equals(other.second))
				return false;
			return true;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((first == null) ? 0 : first.hashCode());
			result = prime * result + (
				(second == null) ? 0 : second.hashCode()
			);
			return result;
		}

		private Graph<V, E> getEnclosingInstance()
		{
			return Graph.this;
		}
	} // end of Pair class

}
