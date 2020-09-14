package io.github.bissim.fly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@DisplayName("Test graph measurement methods")
@TestMethodOrder(OrderAnnotation.class)
public class GraphMeasurementTest {

    private Graph<String, Object> graph;
    private static final String[] INITIAL_NODES = {"a", "b", "c", "d", "e", "f"};

    @BeforeAll
    public static void initAll() {}

    @BeforeEach
    public void init() {
        if (graph == null)
        {
            assertNull(this.graph);
            graph = new Graph<>(String.class, false, false);
            assertNotNull(this.graph);
        }
        else {
            assertNotNull(this.graph);
            graph.clear();
            assertNotNull(this.graph);
            assertEquals(0, graph.numNodes());
            assertEquals(0, graph.numEdges());
        }

        graph
            .addNodes(INITIAL_NODES)
            .addEdge("a", "b")
            .addEdge("a", "c")
            .addEdge("b", "c")
            .addEdge("b", "e")
            .addEdge("c", "d");
    }

    @Test
    @DisplayName("1. Check initial graph")
    @Order(1)
    public void initialGraph()
    {
        assertEquals(INITIAL_NODES.length, graph.numNodes());
        assertEquals(5, graph.numEdges());

        assertEquals(2, graph.nodeDegree("a"));
        assertEquals(3, graph.nodeDegree("b"));
        assertEquals(3, graph.nodeDegree("c"));
        assertEquals(1, graph.nodeDegree("d"));
        assertEquals(1, graph.nodeDegree("e"));
        assertEquals(0, graph.nodeDegree("f"));
    }

    @Test
    @DisplayName("2. Shortest path")
    @Order(2)
    public void shortestPath()
    {
        final String SOURCE = "a";
        final String TARGET = "d";
        final String NODE = "f";
        final Object[] SHORTEST_PATH_ONE = graph.shortestPath(SOURCE, TARGET);
        final Object[] SHORTEST_PATH_TWO = graph.shortestPath(SOURCE, NODE);
        final Object[] SHORTEST_PATH_THREE = graph.shortestPath(NODE, TARGET);
        final Object[] SHORTEST_PATH_FOUR = graph.shortestPath(NODE, NODE);

        assertNotNull(SHORTEST_PATH_ONE);
        assertNull(SHORTEST_PATH_TWO);
        assertNull(SHORTEST_PATH_THREE);
        assertNotNull(SHORTEST_PATH_FOUR);
        assertEquals(2, SHORTEST_PATH_ONE.length);
        assertEquals(0, SHORTEST_PATH_FOUR.length);
    }

    @AfterEach
    public void tearDown() {}

    @AfterAll
    public static void tearDownAll() {}

}
