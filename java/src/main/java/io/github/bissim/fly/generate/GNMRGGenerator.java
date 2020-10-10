package io.github.bissim.fly.generate;

import static java.lang.System.err;

import java.io.File;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.GraphExporter;
import org.jgrapht.nio.csv.CSVExporter;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.util.SupplierUtil;
import org.jgrapht.generate.GnmRandomGraphGenerator;

/**
 * The {@code GNMRGGenerator} class ...
 *
 * @version 1.1.1
 * @author Simone Bisogno
 *
 * @param <V> Type for nodes
 * @param <E> Type for edges
 */
@SuppressWarnings("unchecked")
public class GNMRGGenerator<V, E> {
	public static void main(String[] args) {
		if (args.length < 6) {
			err.println(
				"Usage: java GNMRGGenerator <destination_path> " +
				"<node_class_full_name> \"<separator>\" <boolean_directed> " +
				"<boolean_weighted> <num_nodes> <num_edges>"
			);
			System.exit(-1);
		}
		final String PATH = args[0];
		final String NODE_CLASS_NAME = args[1];
		final String SEPARATOR = args[2].replace("\"", "");
		final boolean IS_DIRECTED = Boolean.parseBoolean(args[3]);
		final boolean IS_WEIGHTED = Boolean.parseBoolean(args[4]);
		final int N_NODES = Integer.parseInt(args[5]);
		final int N_EDGES = Integer.parseInt(args[6]);
		try {
			final Class NODE_CLASS = Class.forName(NODE_CLASS_NAME);
			(new GNMRGGenerator<Object, Object>()).run(
				(Class<Object>) NODE_CLASS,
				IS_DIRECTED,
				IS_WEIGHTED,
				N_NODES,
				N_EDGES,
				PATH,
				SEPARATOR
			);
		} catch (Exception e) {
			err.println(
				e.getClass().getSimpleName() +
				": " +
				e.getLocalizedMessage()
			);
		}
	}

	private void run(
		final Class<V> NODE_CLASS,
		final boolean IS_DIRECTED,
		final boolean IS_WEIGHTED,
		final int N_NODES,
		final int N_EDGES,
		final String PATH,
		final String SEPARATOR
	)
		throws Exception
	{
		Graph<V, E> graph = generate(
			NODE_CLASS,
			IS_DIRECTED,
			IS_WEIGHTED,
			N_NODES,
			N_EDGES
		);
		export(graph, PATH, SEPARATOR, IS_WEIGHTED);
	}

	/**
	 *
	 * The {@code generate(String, Class<V>, Class<E>, boolean, boolean, int,
	 * int)} method ...
	 *
	 * @since 1.1.1
	 *
	 * @param path
	 * @param nodeClass
	 * @param edgeClass
	 * @param isDirected
	 * @param isWeighted
	 * @param numNodes
	 * @param numEdges
	 * @throws IllegalArgumentException
	 */
	private Graph<V, E> generate(
		Class<V> nodeClass,
		boolean isDirected,
		boolean isWeighted,
		int numNodes,
		int numEdges
	)
		throws IllegalArgumentException
	{
		Graph<V, E> graph = this.graphBuilder(
			nodeClass,
			this.determineEdgeClass(isWeighted),
			isDirected,
			isWeighted
		);
		(new GnmRandomGraphGenerator<V, E>(numNodes, numEdges))
				.generateGraph(graph, null);

		return graph;
	}

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
	private Graph<V, E> graphBuilder(
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
	 * The {@code determineEdgeClass(boolean)} method ...
	 *
	 * @since 1.1.1
	 *
	 * @param isWeighted Denotes whether graph is weighted
	 * @return {@link DefaultWeightedEdge} class if graph is weighted,
	 *   {@link DefaultEdge} otherwise
	 */
	@SuppressWarnings("unchecked")
	private Class<E> determineEdgeClass(boolean isWeighted)
	{
		return isWeighted?
			(Class<E>) DefaultWeightedEdge.class:
			(Class<E>) DefaultEdge.class;
	}

	/**
	 * The {@code export(Graph<V, E>, String, String, boolean)} method...
	 *
	 *
	 * @param graph The graph to export
	 * @param path The path in which graph has to be exported
	 * @param separator The separator character to use in file
	 * @param isWeighted Denotes whether graph is weighted
	 * @throws Exception An error occurred while exporting graph
	 */
	private void export(
		Graph<V, E> graph,
		String path,
		String separator,
		boolean isWeighted
	)
		throws Exception
	{
		GraphExporter<V, E> exporter =
				new CSVExporter<>(
					(V v) -> v.toString(),
					CSVFormat.EDGE_LIST,
					separator.charAt(0)
				);

		if (isWeighted)
		{
			((CSVExporter<V, E>) exporter)
				.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS, true);
		}

		try
		{
			exporter.exportGraph(
					graph,
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
}
