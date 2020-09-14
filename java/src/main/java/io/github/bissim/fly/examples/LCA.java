package io.github.bissim.fly.examples;

import static java.lang.System.out;

import java.util.HashSet;
import java.util.Set;

import io.github.bissim.fly.Graph;

public class LCA
{
    public static void main(String[] args)
    {
        /*
         * Source: https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/alg/lca/NaiveLCAFinder.html
         *
         * Here is implemented a naive algorithm for finding the
         * lowest common ancestor of two nodes in a DAG
         *
         * The algorithm:
         *
         * 1. Start at each of nodes you wish to find the lca for (a and b)
         * 2. Create sets aSet containing a, and bSet containing b
         * 3. If either set intersects with the union of the other sets
         *    previous values (i.e. the set of nodes visited) then that
         *    intersection is LCA. If there are multiple intersections
         *    then the earliest one added is the LCA.
         * 4. Repeat from step 3, with aSet now the parents of everything
         *    in aSet, and bSet the parents of everything in bSet
         * 5. If there are no more parents to descend to then there is no LCA
         */
        final String[] NODES = {"a", "b", "c", "d", "e"};
        final Graph<String, Object> DAG =
                new Graph<>(String.class, true, false)
                .addNodes(NODES)
                .addEdge("a", "b")
                .addEdge("a", "c")
                .addEdge("b", "c")
                .addEdge("b", "e")
                .addEdge("c", "d");
        final String FIRST = "d";
        final String SECOND = "e";
        final String EXPECTED_LCA = DAG.getLCA(FIRST, SECOND);

        out.println("Graph: " + DAG);
        // lowest common ancestor of 'd' and 'e' is 'b'
        out.println(
                "\nExpected LCA of " +
                FIRST +
                " and " +
                SECOND +
                ": " +
                EXPECTED_LCA
        );

        // according to algorithm, let's first create two sets
        // containing each one a given node's ancestors
        final Graph<String, Object> EDGE_REVERSED = edgeReversed(DAG);
        Set<String> setFirst = ancestors(EDGE_REVERSED, FIRST);
        Set<String> setSecond = ancestors(EDGE_REVERSED, SECOND);

        // then intersect them
        Set<String> commonAncestors = intersect(setFirst, setSecond);

        // leave nodes in intersections are common ancestors
        final Set<String> LEAVES = findLeaves(DAG, commonAncestors);

        // leaves are all lowest common ancestors
        // just pick one
        final String LCA = LEAVES.isEmpty()?
                null:
                LEAVES.iterator().next();

        out.println("Found LCA of " + FIRST + " and " + SECOND + ": " + LCA);
    }

    private static Graph<String, Object> edgeReversed(
            Graph<String, Object> graph
    )
    {
        Graph<String, Object> edgeReversed =
                new Graph<>(String.class, true, false)
                .addNodes(graph.nodeSet());
        final Object[] EDGES = graph.edgeSet();
        for (int pos = 0; pos < EDGES.length; pos++)
        {
            String source = graph.getEdgeSource(EDGES[pos]);
            String target = graph.getEdgeTarget(EDGES[pos]);
            edgeReversed.addEdge(target, source);
        }
        // stuff
        return edgeReversed;
    }

    private static Set<String> ancestors(
            Graph<String, Object> graph,
            String start
    )
    {
        final String[] BFS_NODES = graph.bfsNodes(start);
        Set<String> ancestors = new HashSet<>(BFS_NODES.length);

        for (int pos = 0; pos < BFS_NODES.length; pos++)
        {
            ancestors.add(BFS_NODES[pos]);
        }

        return ancestors;
    }

    private static Set<String> intersect(
            Set<String> a,
            Set<String> b
    )
    {
        // optimization trick: save the intersection using the smaller set
        // retainAll deals with intersection
        if (a.size() < b.size())
        {
            a.retainAll(b);
            return a;
        }
        else
        {
            b.retainAll(a);
            return b;
        }
    }

    private static Set<String> findLeaves(
            Graph<String, Object> digraph,
            Set<String> nodes
    )
    {
        Set<String> leaves = new HashSet<>();

        // Find the set of all non-leaves by iterating through the set of common
        // ancestors. When we encounter a node which is still part of the
        // SLCA(a, b) we remove its parent(s).
        for (String ancestor: nodes)
        {
            boolean isLeaf = true;
            for (Object edge : digraph.nodeOutEdges(ancestor))
            {
                String target = digraph.getEdgeTarget(edge);
                if (nodes.contains(target))
                {
                    isLeaf = false;
                    break;
                }
            }
            if (isLeaf)
            {
                leaves.add(ancestor);
            }
        }

        return leaves;
    }
}
