package io.github.bissim.fly.util;

import java.util.function.Supplier;

import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

/**
 * {@code CustomGraphBuilder&lt;V, E&gt;} is a class used to generate JGraphT
 * graphs to be used mainly by {@code Graph<V, E>} class.
 * 
 * @version 1.1.2
 * @author Simone Bisogno
 *&lt;<a href="mailto:s.bisogno10@studenti.unisa.it?cc=s.bisogno90@gmail.com&amp;subject=Java%20FLY%20graph%20library&amp;body=Hello,%0D%0A%0D%0Ayour%20message%20here">s.bisogno10@studenti.unisa.it</a>&gt;
 * 
 * @param <V> Type for nodes
 * @param <E> Type for edges
 */
public class CustomGraphBuilder<V, E> {
    /**
	 * The <code>graphBuilder()</code> method constructs an instance
	 * of <code>org.jgrapht.Graph&lt;V, E&gt;</code> by specifying the
	 * node class, the edge class, whether graph is directed, whether graph
	 * is weighted.
	 * 
	 * @since 1.1.2
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
	public org.jgrapht.Graph<V, E> build(
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
}
