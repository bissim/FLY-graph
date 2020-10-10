package io.github.bissim.fly.examples;

import static java.lang.System.out;
import static java.lang.System.err;

import io.github.bissim.fly.Graph;

public class Diameter
{
    public static void main(String[] args)
    {
        Graph<String, Object> graph = (Graph<String, Object>) null;
        String[] nodes = { "a", "b", "c", "d", "e" };

        if (args.length > 3)
        {
            final String PATH = args[0];
            final String SEPARATOR = args[1].replace("\"", "");
            final boolean IS_DIRECTED = Boolean.parseBoolean(args[2]);
            final boolean IS_WEIGHTED = Boolean.parseBoolean(args[3]);
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
                "java Diameter <path_to_edgelist> \"<separator>\" " +
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
        }

        final long START_TIME = System.currentTimeMillis();
        graph.getDiameter();
        out.println((System.currentTimeMillis() - START_TIME) / 1000.0);
    }
}
