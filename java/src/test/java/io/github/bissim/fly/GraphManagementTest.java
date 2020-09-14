package io.github.bissim.fly;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@DisplayName("Test graph management methods")
@TestMethodOrder(OrderAnnotation.class)
public class GraphManagementTest {

    private Graph<String, Object> graph;
    private static final String[] INITIAL_NODES = {"a", "b", "c", "d", "e", "f"};

    public GraphManagementTest()
    {
        this.graph = new Graph<>(String.class, false, false);
    }

    @BeforeAll
    public static void initAll() {}

    @BeforeEach
    public void init() {
        if (graph.numNodes() != 0)
        {
            assertNotNull(graph);
            graph.clear();
            assertNotNull(graph);
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
    @DisplayName("2. Clear graph nodes and edges")
    @Order(2)
    public void clear()
    {
        assertEquals(INITIAL_NODES.length, graph.numNodes());
        assertEquals(5, graph.numEdges());
        graph.clear();
        assertNotEquals(INITIAL_NODES.length, graph.numNodes());
        assertNotEquals(5, graph.numEdges());
        assertEquals(0, graph.numNodes());
        assertEquals(0, graph.numEdges());
    }

    @Test
    @DisplayName("3. Add node")
    @Order(3)
    public void addNode()
    {
        final String NODE = "g";

        assertFalse(graph.hasNode(NODE));
        graph.addNode(NODE);
        assertTrue(graph.hasNode(NODE));
    }

    @Test
    @DisplayName("4. Add nodes")
    @Order(4)
    public void addNodes()
    {
        final String[] NODES = {"g", "h", "i"};

        assertEquals(INITIAL_NODES.length, graph.numNodes());
        graph.addNodes(NODES);
        assertNotEquals(INITIAL_NODES.length, graph.numNodes());
        assertEquals(9, graph.numNodes());
    }

    @Test
    @DisplayName("5. Node degree")
    @Order(5)
    public void nodeDegree()
    {
        final String NODE = "a";

        assertEquals(2, graph.nodeDegree(NODE));
    }

    @Test
    @DisplayName("6. Node in degree")
    @Order(6)
    public void nodeInDegree()
    {
        final String NODE = "a";

        assertEquals(2, graph.nodeInDegree(NODE));
    }

    @Test
    @DisplayName("7. Node out degree")
    @Order(7)
    public void nodeOutDegree()
    {
        final String NODE = "a";

        assertEquals(2, graph.nodeOutDegree(NODE));
    }

    @Test
    @DisplayName("8. Neighbourhood")
    @Order(8)
    public void neighbourhood()
    {
        final String NODE = "a";
        final Graph<String, Object> NEIGHBOURHOOD = graph.neighbourhood(NODE);

        assertNotNull(NEIGHBOURHOOD);
        assertEquals(3, NEIGHBOURHOOD.numNodes());
        assertEquals(3, NEIGHBOURHOOD.numEdges());
    }

    @Test
    @DisplayName("9. Node edges")
    @Order(9)
    public void nodeEdges()
    {
        final String NODE = "a";
        final Object[] EDGES = graph.nodeEdges(NODE);

        assertNotNull(EDGES);
        assertNotEquals(0, EDGES.length);
        assertEquals(2, EDGES.length);
    }

    @Test
    @DisplayName("10. Node in edges")
    @Order(10)
    public void nodeInEdges()
    {
        final String NODE = "a";
        final Object[] EDGES = graph.nodeInEdges(NODE);

        assertNotNull(EDGES);
        assertNotEquals(0, EDGES.length);
        assertEquals(2, EDGES.length);
    }

    @Test
    @DisplayName("11. Node out edges")
    @Order(11)
    public void nodeOutEdges()
    {
        final String NODE = "a";
        final Object[] EDGES = graph.nodeOutEdges(NODE);

        assertNotNull(EDGES);
        assertNotEquals(0, EDGES.length);
        assertEquals(2, EDGES.length);
    }

    @Test
    @DisplayName("12. Node set")
    @Order(12)
    public void nodeSet()
    {
        final String[] NODES = graph.nodeSet();

        assertNotEquals(0, NODES.length);
        assertEquals(INITIAL_NODES.length, NODES.length);
    }

    @Test
    @DisplayName("13. Number of nodes")
    @Order(13)
    public void numNodes()
    {
        final int NUM_NODES = graph.numNodes();

        assertNotEquals(0, NUM_NODES);
        assertEquals(INITIAL_NODES.length, NUM_NODES);
    }

    @Test
    @DisplayName("14. Remove node")
    @Order(14)
    public void removeNode()
    {
        final String NODE = "f";

        assertTrue(graph.hasNode(NODE));
        graph.removeNode(NODE);
        assertFalse(graph.hasNode(NODE));
    }

    @Test
    @DisplayName("15. Has node")
    @Order(15)
    public void hasNode()
    {
        final String NODE_PRESENT = "a";
        final String NODE_ABSENT = "g";

        assertTrue(graph.hasNode(NODE_PRESENT));
        assertFalse(graph.hasNode(NODE_ABSENT));
    }

    @Test
    @DisplayName("16. Add edge")
    @Order(16)
    public void addEdge()
    {
        final String SOURCE = "c";
        final String TARGET = "f";
        final String NODE_ABSENT = "g";

        assertFalse(graph.hasEdge(SOURCE, TARGET));
        graph.addEdge(SOURCE, TARGET);
        assertTrue(graph.hasEdge(SOURCE, TARGET));
        assertThrows(
            IllegalArgumentException.class,
            () -> graph.addEdge(SOURCE, NODE_ABSENT)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> graph.addEdge(NODE_ABSENT, TARGET)
        );
    }

    @Test
    @DisplayName("17. Get edge")
    @Order(17)
    public void getEdge()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final String THIRD_NODE = "f";

        assertNotNull(graph.getEdge(SOURCE, TARGET));
        assertNull(graph.getEdge(SOURCE, THIRD_NODE));
        assertNull(graph.getEdge(THIRD_NODE, TARGET));
    }

    @Test
    @DisplayName("18. Edge set")
    @Order(18)
    public void edgeSet()
    {
        final Object[] EDGES = graph.edgeSet();

        assertNotEquals(0, EDGES.length);
        assertEquals(5, EDGES.length);
    }

    @Test
    @DisplayName("19. Number of edges")
    @Order(19)
    public void numEdges()
    {
        final int NUM_EDGES = graph.numEdges();

        assertNotEquals(0, NUM_EDGES);
        assertEquals(5, NUM_EDGES);
    }

    @Test
    @DisplayName("20. Edge source")
    @Order(20)
    public void getEdgeSource()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final Object EDGE = graph.getEdge(SOURCE, TARGET);

        assertNotNull(EDGE);
        final String NODE = graph.getEdgeSource(EDGE);
        assertEquals(NODE, SOURCE);
    }

    @Test
    @DisplayName("21. Set edge source")
    @Order(21)
    public void setEdgeSource()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final String NODE = "f";
        final Object EDGE = graph.getEdge(SOURCE, TARGET);

        assertNotNull(EDGE);
        graph.setEdgeSource(EDGE, NODE);
        assertFalse(graph.hasEdge(SOURCE, TARGET));
        assertTrue(graph.hasEdge(NODE, TARGET));
        assertNull(graph.getEdge(SOURCE, TARGET));
        assertNotNull(graph.getEdge(NODE, TARGET));
    }

    @Test
    @DisplayName("22. Edge target")
    @Order(22)
    public void getEdgeTarget()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final Object EDGE = graph.getEdge(SOURCE, TARGET);

        assertNotNull(EDGE);
        final String NODE = graph.getEdgeTarget(EDGE);
        assertEquals(NODE, TARGET);
    }

    @Test
    @DisplayName("23. Set edge target")
    @Order(23)
    public void setEdgeTarget()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final String NODE = "f";
        final Object EDGE = graph.getEdge(SOURCE, TARGET);

        assertNotNull(EDGE);
        graph.setEdgeTarget(EDGE, NODE);
        assertFalse(graph.hasEdge(SOURCE, TARGET));
        assertTrue(graph.hasEdge(SOURCE, NODE));
        assertNull(graph.getEdge(SOURCE, TARGET));
        assertNotNull(graph.getEdge(SOURCE, NODE));
    }

    @Test
    @DisplayName("24. Edge weight")
    @Order(24)
    public void getEdgeWeight()
    {
        final String SOURCE = "a";
        final String TARGET = "b";

        assertNotNull(graph.hasEdge(SOURCE, TARGET));
        final double WEIGHT = graph.getEdgeWeight(SOURCE, TARGET);
        assertEquals(1.0, WEIGHT);
    }

    @Test
    @DisplayName("25. Set edge weight")
    @Order(25)
    public void setEdgeWeight()
    {
        final String SOURCE = "a";
        final String TARGET = "b";

        assertNotNull(graph.hasEdge(SOURCE, TARGET));
        assertThrows(
            UnsupportedOperationException.class,
            () -> graph.setEdgeWeight(SOURCE, TARGET, 2.0)
        );
    }

    @Test
    @DisplayName("26. Remove edge from graph")
    @Order(26)
    public void removeEdge()
    {
        final String SOURCE = "a";
        final String TARGET = "b";

        assertTrue(graph.hasEdge(SOURCE, TARGET));
        graph.removeEdge(SOURCE, TARGET);
        assertFalse(graph.hasEdge(SOURCE, TARGET));
    }

    @Test
    @DisplayName("27. Has edge")
    @Order(27)
    public void hasEdge()
    {
        final String SOURCE = "a";
        final String TARGET = "b";
        final String THIRD = "f";

        assertTrue(graph.hasEdge(SOURCE, TARGET));
        assertFalse(graph.hasEdge(SOURCE, THIRD));
        assertFalse(graph.hasEdge(TARGET, THIRD));
    }

    @AfterEach
    public void tearDown() {}

    @AfterAll
    public static void tearDownAll() {}

}
