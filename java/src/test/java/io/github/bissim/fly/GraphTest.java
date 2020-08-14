package io.github.bissim.fly;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.github.bissim.fly.Graph;

/**
 * Unit test for simple App.
 */
public class GraphTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void rigorousTest()
    {
        Graph<String, Object> graph = new Graph<>(String.class, true, false);

        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");

        graph.addEdge("a", "b");
        graph.addEdge("a", "c");
        graph.addEdge("b", "c");

        assertEquals(3, graph.numNodes());
        assertEquals(3, graph.numEdges());

        assertEquals(2, graph.nodeOutDegree("a"));
        assertEquals(1, graph.nodeOutDegree("b"));
        assertEquals(0, graph.nodeOutDegree("c"));
    }
}
