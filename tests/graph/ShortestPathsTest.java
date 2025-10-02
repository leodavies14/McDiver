package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {
    /** The graph example from lecture */
    static final String[] vertices1 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges1 = {
        {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
        {1, 4, 23},
        {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
        {3, 5, 20}, {3, 6, 37},
        {4, 5, 3}, {4, 6, 20},
        {5, 6, 16}
    };

    static final int[][] edges2 = {
            {0,1,1}, {0,5, 30},
            {1,2,1}, {1,3,1},
            {2,3,2},
            {3,4,3},
            {4,5,4},
            {5,6,3}
    };

    static final int[][] edges3 = {
            {0,1,7}, {0,2,1}, {0,6,15},
            {1,3,6},
            {2,5,2},
            {3,2,2}, {3,6,2},
            {4,6,6},
            {5,1,3},{5,3,9}, {5,4,5}
    };
    static class TestGraph implements WeightedDigraph<String, int[]> {
        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
            }
        }
        public Iterable<int[]> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(int[] edge) { return vertices[edge[0]]; }
        public String dest(int[] edge) { return vertices[edge[1]]; }
        public double weight(int[] edge) { return edge[2]; }
    }
    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }
    static TestGraph testGraph2() {
        return new TestGraph(vertices1, edges2);
    }
    static TestGraph testGraph3() {
        return new TestGraph(vertices1, edges3);
    }

    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    @Test
    void test2() {
        TestGraph graph = testGraph2();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(12, ssp.getDistance("g"));
        assertEquals(2, ssp.getDistance("d"));

        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("f")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" f");
        assertEquals("best path: a b d e f", sb.toString());
    }
    @Test
    void test3() {
        TestGraph graph = testGraph3();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(6, ssp.getDistance("b"));
        assertEquals(12, ssp.getDistance("d"));
        assertEquals(14, ssp.getDistance("g"));

        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("d")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" d");
        assertEquals("best path: a c f d", sb.toString());

        StringBuilder sb1 = new StringBuilder();
        sb1.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb1.append(" " + vertices1[e[0]]);
        }
        sb1.append(" g");
        assertEquals("best path: a c f e g", sb1.toString());

    }


}
