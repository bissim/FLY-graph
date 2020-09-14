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

@DisplayName("Test graph metrics methods")
@TestMethodOrder(OrderAnnotation.class)
public class GraphMetricsTest {

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

    //

    @AfterEach
    public void tearDown() {}

    @AfterAll
    public static void tearDownAll() {}

}
