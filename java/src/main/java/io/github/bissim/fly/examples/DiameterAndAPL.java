package io.github.bissim.fly.examples;

import static java.lang.System.out;

import io.github.bissim.fly.Graph;

public class DiameterAndAPL
{
    public static void main(String[] args)
    {
        final String[] NODES = {"a", "b", "c", "d", "e"};
        final Graph<String, Object> GRAPH =
                new Graph<>(String.class, false, false)
                .addNodes(NODES)
                .addEdge("a", "b")
                .addEdge("a", "c")
                .addEdge("b", "c")
                .addEdge("b", "e")
                .addEdge("c", "d");
        final double EXPECTED_DIAMETER = GRAPH.getDiameter();

        out.println("Graph: " + GRAPH);
        // longest distance is between 'd' and 'e':
        // it takes {c, d}, {b, c} and {b, e} edges
        out.println("\nExpected diameter: " + EXPECTED_DIAMETER);

        // by definition, graph diameter is the maximum distance
        // between two graph nodes, whathever the two graph nodes are
        // so, in order to find graph diameter, we have to build
        // a matrix of distances, useful for APL as well
        double[][] distancesLengths = new double[NODES.length][NODES.length];
        String source, target;
        for (int row = 0; row < NODES.length; row++)
        {
            // we only calculate upper triangle except diagonal
            for (int col = row + 1; col < NODES.length; col++)
            {
                source = NODES[row];
                target = NODES[col];
                distancesLengths[row][col] = GRAPH.shortestPath(source, target).length;
            }
        }

        // diameter is the maximum distance in upper triangle
        double diameter = distancesLengths[0][1];
        double current = 0.0;
        for (int row = 1; row < NODES.length; row++)
        {
            for (int col = row + 1; col < NODES.length; col++)
            {
                current = distancesLengths[row][col];
                if (current > diameter)
                {
                    diameter = current;
                }
            }
        }

        out.println("Found graph diameter: " + diameter);

        // Average Path Length (APL for its friends) is
        // the average length of distances among nodes
        double lengthsSum = 0.0;
        for (int row = 0; row < NODES.length; row++)
        {
            for (int col = row + 1; col < NODES.length; col++)
            {
                lengthsSum += distancesLengths[row][col];
            }
        }

        // sum of all distances lengths is 16, so APL
        // should be 16/(5*4) = (4*4)/(5*4) = 4/5 = 0.8
        final double APL = lengthsSum / ((1.0 * NODES.length) * (1.0 * NODES.length - 1.0));
        out.println("\nAverage path length: " + APL);
    }
}
