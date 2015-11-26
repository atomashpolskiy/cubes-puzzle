package example.unit;

import example.Edge;
import example.Face;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static example.unit.matchers.Matchers.hasEdges;
import static example.unit.matchers.Matchers.hasEdgesReversed;
import static org.junit.Assert.assertThat;

public class FaceTest {

    private Face face1;
    private Face face2;

    @Before
	public void setUp() {
        face1 = new Face(new byte[] {1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5);
        face2 = new Face(new byte[] {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1}, 5);
    }

    @Test
    public void getEdges() {
        Collection<Edge> edges1 = face1.getEdges();
        byte[][] expectedEdges1 = new byte[][] {
            new byte[] {1, 0, 1, 0, 1},
            new byte[] {1, 1, 0, 1, 1},
            new byte[] {1, 0, 1, 0, 1},
            new byte[] {1, 1, 0, 1, 1}
        };
        assertThat(edges1, hasEdges(expectedEdges1));
        assertThat(edges1, hasEdgesReversed(expectedEdges1));

        Collection<Edge> edges2 = face2.getEdges();
        byte[][] expectedEdges2 = new byte[][] {
            new byte[] {0, 1, 0, 1, 0},
            new byte[] {0, 1, 0, 1, 0},
            new byte[] {0, 0, 1, 0, 1},
            new byte[] {1, 1, 0, 1, 0}
        };
        assertThat(edges2, hasEdges(expectedEdges2));
        assertThat(edges2, hasEdgesReversed(expectedEdges2));
    }

    @Test(expected = Exception.class)
    public void incorrectEdge_AllSockets() {
        new Face(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 5);
    }

    @Test(expected = Exception.class)
    public void incorrectEdge_AllPlugs() {
        new Face(new byte[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 5);
    }

    @Test(expected = Exception.class)
    public void incorrectEdge_OneEdgeAllSockets() {
        new Face(new byte[] {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 5);
    }
}
