package isislab.fly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
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
 * @author Simone Bisogno
 *&lt;<a href="mailto:s.bisogno10@studenti.unisa.it?
 *cc=s.bisogno90@gmail.com&
 *subject=Java%20FLY%20graph%20library&
 *body=Hello,%0D%0A%0D%0A
 *your%20message%20here"
 *>s.bisogno10@studenti.unisa.it</a>&gt;
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
	 */
	private org.jgrapht.Graph<V, E> graph;
	/**
	 * The class used for nodes
	 */
	private Class<V> nodeClass;
	/**
	 * The class used for edges
	 */
	private Class<E> edgeClass;
	/**
	 * Denotes whether graph is weighted
	 */
	private boolean isWeighted;
	/**
	 * Denotes whether graph is directed
	 */
	private boolean isDirected;

	/**
	 * The <code>Graph(Class&lt;V&gt;, boolean, boolean)</code> constructs a
	 * graph with node class the same specified as parameter and can be
	 * directed, weighted or both according to boolean parameters.
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

	// public methods

	/**
	 * The <code>clear()</code> method deals with emptying both
	 * graph node set and edge set.
	 */
	public void clear()
	{
		this.graph.vertexSet().clear();
//		this.graph.edgeSet().clear(); // removing nodes should be enough
	}

	/*
	 * node methods
	 */

	/**
	 * The <code>addNode(V)</code> method deals with adding specified node
	 * to graph node set.
	 * 
	 * @param node The node to add to graph
	 */
	public void addNode(V node)
	{
		this.graph.addVertex(node);
	}

	/**
	 * The <code>addNodes(V[])</code> method deals with adding nodes from
	 * specified array to graph node set.
	 * 
	 * @see #addNode(Object)
	 * 
	 * @param nodes The nodes to add to graph
	 */
	public void addNodes(V[] nodes)
	{
		IntStream
				.range(0, nodes.length)
				.forEach(i -> {
					this.addNode(nodes[i]);
				});
	}

	/*
	 * The <code>addNodes(V...)</code> method deals with adding specified nodes
	 * to graph node set.
	 * 
	 * @see #addNode(Object)
	 * 
	 * @param nodes The nodes to add to graph
	 */
//	public void addNodes(V... nodes)
//	{
//		List<V> nodesList = List.of(nodes); // not in Java 8
//		nodesList.stream().forEach(n -> {
//			this.addNode(n);
//		});
//	}

	/**
	 * The <code>nodeDegree(V)</code> method returns the degree of specified
	 * node, as long as it belongs to the graph.
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
	 * @param node The node of graph which we want to know the 'out' degree of
	 * @return The 'out' degree of node
	 */
	public int nodeOutDegree(V node)
	{
		return this.graph.outDegreeOf(node);
	}

	/**
	 * The <code>neighbourhood(V)</code> method returns the list of edges
	 * in which specified node is source or target.
	 * 
	 * @param node The node which we want to know the neighbourhood of
	 * @return The array of edges having node as source or target
	 */
	public E[] neighbourhood(V node)
	{
		return this.edgeArrayFromSet(this.graph.edgesOf(node));
	}

	/**
	 * The <code>nodeInEdges(V)</code> method returns the 'in' edges of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'in' edges of a node one denotes the edges having the node as
	 * their target.
	 * 
	 * @param node The node which we want to know the 'in edges' of
	 * @return The array of edges having node as target
	 */
	public E[] nodeInEdges(V node)
	{
		return this.edgeArrayFromSet(this.graph.incomingEdgesOf(node));
	}

	/**
	 * The <code>nodeOutEdges(V)</code> method returns the 'out' edges of
	 * specified node, as long as it belongs to the graph.
	 * <br>
	 * By 'out' edges of a node one denotes the edges having the node as
	 * their source.
	 * 
	 * @param node The node which we want to know the 'out edges' of
	 * @return The array of edges having node as source
	 */
	public E[] nodeOutEdges(V node)
	{
		return this.edgeArrayFromSet(this.graph.outgoingEdgesOf(node));
	}

	/**
	 * The <code>nodeSet()</code> method returns all nodes of the graph.
	 * 
	 * @return The array of graph nodes
	 */
	public V[] nodeSet()
	{
		Set<V> nodeSet = this.graph.vertexSet();

		@SuppressWarnings("unchecked")
		V[] nodes = (V[]) Array.newInstance(
				this.nodeClass,
				nodeSet.size()
		);
		Iterator<V> setIterator = nodeSet.iterator();
		IntStream
				.range(0, nodes.length)
				.forEach(i -> {
					nodes[i] = setIterator.next();
				});

		return nodes;
	}

	/**
	 * The <code>numNodes()</code> method returns the number of nodes
	 * in the graph.
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
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 */
	public void addEdge(V firstNode, V secondNode)
	{
		this.graph.addEdge(firstNode, secondNode);
	}

	/*
	 * The <code>addEdges(E[])</code> method ...
	 * 
	 * @param edges The edges to add to graph
	 */
//	public void addEdges(E[] edges)
//	{
//		List<E> edgesList = List.of(edges);
//		edgesList.stream().forEach(e -> {
//			this.addEdge(firstNode, secondNode);
//		});
//	}

	/**
	 * The <code>getEdge(V, V)</code> method returns the edge which
	 * specified nodes are the source and target of.
	 * 
	 * @param firstNode One of the edge nodes
	 * @param secondNode The other edge node
	 * @return the edge of the graph where <code>firstNode</code> is source
	 * and <code>secondNode</code> is target
	 */
	public E getEdge(V firstNode, V secondNode)
	{
		return this.graph.getEdge(firstNode, secondNode);
	}

	/**
	 * The <code>edgeSet()</code> method returns all edges of the graph.
	 * 
	 * @return The array of graph edges
	 */
	public E[] edgeSet()
	{
		Set<E> edgeSet = this.graph.edgeSet();

		@SuppressWarnings("unchecked")
		E[] edges = (E[]) Array.newInstance(
				this.edgeClass,
				edgeSet.size()
		);
		Iterator<E> setIterator = edgeSet.iterator();
		IntStream
				.range(0, edges.length)
				.forEach(i -> {
					edges[i] = setIterator.next();
				});

		return edges;
	}

	/**
	 * The <code>numEdges()</code> method returns the number of edges
	 * in the graph.
	 * 
	 * @return The number of graph edges
	 */
	public int numEdges()
	{
		return this.graph.edgeSet().size();
	}

	// TODO comment
	public V getEdgeSource(E e)
	{
		return this.graph.getEdgeSource(e);
	}

	// TODO comment
	public void setEdgeSource(E e, V s)
	{
		V t = this.graph.getEdgeTarget(e);
		this.graph.removeEdge(e);
		this.graph.addEdge(s, t);
	}

	// TODO comment
	public V getEdgeTarget(E e)
	{
		return this.graph.getEdgeTarget(e);
	}

	// TODO comment
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
	 * I/O related functions
	 */

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
	@SuppressWarnings("unchecked")
	public static <V, E> Graph<V, E> importGraph(
			String path,
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

//		@SuppressWarnings("unchecked")
//		VertexProvider<V> vertexProvider =
//				(id, attributes) -> (V) id;
//		EdgeProvider<V, E> edgeProvider =
//				(from, to, label, attributes) ->
//					flyGraph.graph.getEdgeSupplier().get();

		CSVImporter<V, E> importer = new CSVImporter<>(
					CSVFormat.EDGE_LIST,
					separator.charAt(0)
			);
		// easy fix for real graph import
		// as per https://stackoverflow.com/questions/61089620/
		// effective since JGraphT 1.4.1
		// remove imported graph conversion after enabling this line
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
					new FileReader(path)
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

		// convert imported indexed graph to effective labeled one
		// as per https://stackoverflow.com/questions/60461351/
//		Map<V, Map<String, Attribute>> attrs = new HashMap<>();
//		importer.addVertexAttributeConsumer((p, a) -> {
//		    Map<String, Attribute> map = attrs.computeIfAbsent(
//		    		p.getFirst(),
//		    		k -> new HashMap<>()
//		    );
//		    map.put(p.getSecond(), a);
//		});
//		Graph<V, E> effectiveGraph = new Graph<V, E>(
//					nodeClass,
//					isDirected,
//					isWeighted
//			);
//		for(V v : Arrays.asList(flyGraph.nodeSet()))
//		    effectiveGraph.addNode((V) attrs.get(v).get("ID").getValue());
//		for(E e : Arrays.asList(flyGraph.edgeSet())){
//		    V source = flyGraph.getEdgeSource(e);
//		    V target = flyGraph.getEdgeTarget(e);
//		    V sourceID = (V) attrs.get(source).get("ID").getValue();
//		    V targetID = (V) attrs.get(target).get("ID").getValue();
//		    effectiveGraph.addEdge(sourceID, targetID);
//		}

		return flyGraph;
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
	 * @param node The node which we want to know the connected component of
	 * @return the connected component given node belongs to
	 */
	public V[] nodeConnectedComponent(V node)
	{
		ConnectivityInspector<V, E> inspector =
				new ConnectivityInspector<>(this.graph);

		Set<V> nodeConnectedSet = inspector.connectedSetOf(node);
		@SuppressWarnings("unchecked")
		V[] nodeComponent = (V[]) Array.newInstance(
				this.nodeClass,
				nodeConnectedSet.size()
		);
		Iterator<V> setIterator = nodeConnectedSet.iterator();
		IntStream
				.range(0, nodeConnectedSet.size())
				.forEach(i -> {
					nodeComponent[i] = setIterator.next();
				});

		return nodeComponent;
	}

	/**
	 * The <code>stronglyConnectedComponents()</code> method returns strongly
	 * connected components of graph as group of nodes.
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
						.forEach(j -> {
							nodes[j] = componentIterator.next();
						});
				strongComponents[i] = nodes;
			});

		return strongComponents;
	}

	/**
	 * The <code>stronglyConnectedSubgraphs()</code> method returns strongly
	 * connected subgraphs of graph.
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
	 * @return array of topological sorted nodes
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

//	/*
//	 * Minimum spanning tree
//	 */

	/**
	 * The <code>getMST()</code> method returns the minimum spanning tree
	 * of graph.
	 * 
	 * @return minimum spanning tree of graph
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
	 * Object methods
	 */

	/**
	 * The <code>toString()</code> method returns a string
	 * representation of <code>Graph&lt;V, E&gt;</code> object.
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

	/**
	 * The <code>addEdges()</code> helper method adds edges objects to graph;
	 * it first convert every edge into its couple of nodes and then invokes
	 * the {@link #addEdge(Object, Object)} method for every couple of nodes.
	 * 
	 * @see #addEdge(Object, Object)
	 * 
	 * @param edges Array of edges to add to graph
	 */
	private void addEdges(E[] edges)
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
	}

	/**
	 * The <code>graphBuilder()</code> helper method constructs an instance
	 * of <code>org.jgrapht.Graph&lt;V, E&gt;</code> by specifying the
	 * node class, the edge class, whether graph is directed, whether graph
	 * is weighted.
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
	 * @param edgeSet A set of edges
	 * @return The array of edges
	 */
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
				.forEach(i -> {
					edgeArray[i] = setIterator.next();
				});

		return edgeArray;
	}

	/**
	 * The <code>extractBfsEdges(V)</code> helper method deals with converting
	 * the sequence of nodes from BFS visit into pairs of nodes; such a
	 * sequence will be converted in a proper sequence of edges by
	 * <code>{@link #bfsEdges(Object)}</code> public method.
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
	 * This class models a pair of generic objects.
	 * <br />
	 * For purposes within <code>{@link Graph}</code> class,
	 * instances of <code>Pair</code> will be immutable; in
	 * particular, class itself will be immutable.
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
		 * First element of the pair.
		 */
		private V first;
		/**
		 * Second element of the pair.
		 */
		private V second;

		/*
		 * constructor
		 */

		/**
		 * Build a pair of <code>V</code> objects.
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
		 * @return pair first element
		 */
		public V getFirst()
		{
			return this.first;
		}

		/**
		 * Return second element of pair.
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
	}

}
