package example.unit.matchers;

import example.Edge;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Matchers {

    public static Matcher<Iterable<Edge>> hasEdges(byte[]... edges) {
        return hasEdges(false, edges);
    }

    public static Matcher<Iterable<Edge>> hasEdgesReversed(byte[]... edges) {
        return hasEdges(true, edges);
    }

    private static Matcher<Iterable<Edge>> hasEdges(boolean shouldCompareReversed, byte[]... edges) {
        EdgeMatcher[] matchers = new EdgeMatcher[edges.length];

        for (int i = 0; i < edges.length; i++) {
            Edge mockedEdge = mock(Edge.class);
            when(mockedEdge.getPoints()).thenReturn(edges[i]);
            matchers[i] = new EdgeMatcher(mockedEdge, shouldCompareReversed);
        }

        return CoreMatchers.hasItems(matchers);
    }
}
