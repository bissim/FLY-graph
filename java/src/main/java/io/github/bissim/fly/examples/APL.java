package io.github.bissim.fly.examples;

import static java.lang.System.err;
import static java.lang.System.out;

import io.github.bissim.fly.Graph;

public class APL
{
    public static void main(String[] args)
    {
        Graph<String, Object> graph = (Graph<String, Object>) null;
        String[] nodes = { "a", "b", "c", "d", "e" };
        int iterations = Integer.parseInt(args[4]);

        final long START_TIME = System.currentTimeMillis();
        for (int count = 0; count < iterations; count++)
        {
            if (args.length > 4)
            {
                final String PATH = args[0];
                final String SEPARATOR = args[1].replace("\"", "");
                final boolean IS_DIRECTED = Boolean.parseBoolean(args[2]);
                final boolean IS_WEIGHTED = Boolean.parseBoolean(args[3]);
//                iterations = Integer.parseInt(args[4]);
                try
                {
                    graph = Graph.importGraph(PATH, SEPARATOR, String.class, IS_WEIGHTED, IS_DIRECTED);
                }
                catch (Exception e)
                {
                    err.println(
                        e.getClass().getSimpleName() +
                        ": " +
                        e.getLocalizedMessage()
                    );
                }
                nodes = graph.nodeSet();
            }
            else
            {
                out.println("You may want to specify the following parameters:");
                out.println(
                    "java APL <path_to_edgelist> \"<separator>\" " +
                    "<boolean_is_directed> <boolean_is_weighted>"
                );
                graph =
                        new Graph<>(String.class, false, false)
                        .addNodes(nodes)
                        .addEdge("a", "b")
                        .addEdge("a", "c")
                        .addEdge("b", "c")
                        .addEdge("b", "e")
                        .addEdge("c", "d");
                iterations = 1;
            }

            // in order to find graph APL, we have
            // to build a matrix of distances
            double[][] distancesLengths = new double[nodes.length][nodes.length];
            String source, target;
            for (int row = 0; row < nodes.length; row++)
            {
                // we only calculate upper triangle except diagonal
                for (int col = row + 1; col < nodes.length; col++)
                {
                    source = nodes[row];
                    target = nodes[col];
                    distancesLengths[row][col] =
                        graph
                            .shortestPath(source, target)
                            .length;
                }
            }

            // Average Path Length (APL for its friends) is
            // the average length of distances among nodes
            double lengthsSum = 0.0;
            for (int row = 0; row < nodes.length; row++)
            {
                for (int col = row + 1; col < nodes.length; col++)
                {
                    lengthsSum += distancesLengths[row][col];
                }
            }

            // sum of all distances lengths is 16, so APL
            // should be 16/(5*4) = (4*4)/(5*4) = 4/5 = 0.8
            @SuppressWarnings("unused")
            final double APL =
                lengthsSum / ((1.0 * nodes.length) * (1.0 * nodes.length - 1.0));
        }
        out.println((System.currentTimeMillis() - START_TIME) / 1000.0);
    }
}
