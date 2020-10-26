package io.github.bissim.fly.examples;

import static java.lang.System.out;
import static java.lang.System.err;

import io.github.bissim.fly.Graph;

public class LCA
{
    public static void main(String[] args)
    {
        Graph<String, Object> dag = (Graph<String, Object>) null;
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
                iterations = Integer.parseInt(args[4]);
                try
                {
                    dag = Graph.importGraph(PATH, SEPARATOR, String.class, IS_WEIGHTED, IS_DIRECTED);
                }
                catch (Exception e)
                {
                    err.println(
                        e.getClass().getSimpleName() +
                        ": " +
                        e.getLocalizedMessage()
                    );
                }
                nodes = dag.nodeSet();
            }
            else
            {
                out.println("You may want to specify the following parameters:");
                out.println(
                    "java Diameter <path_to_edgelist> \"<separator>\" " +
                    "<boolean_is_directed> <boolean_is_weighted>"
                );
                dag =
                        new Graph<>(String.class, true, false)
                        .addNodes(nodes)
                        .addEdge("a", "b")
                        .addEdge("a", "c")
                        .addEdge("b", "c")
                        .addEdge("b", "e")
                        .addEdge("c", "d");
                iterations = 1;
            }

            for (String first: dag.nodeSet())
            {
                for (String second: dag.nodeSet())
                {
                    dag.getLCA(first, second);
                }
            }
        }
        out.println((System.currentTimeMillis() - START_TIME) / 1000.00);
    }
}
