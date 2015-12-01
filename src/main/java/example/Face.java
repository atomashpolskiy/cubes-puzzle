package example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Face is a square. Therefore it has 4 edges.
 * Each edge is represented by a list of "connection points",
 * with each point being either a "plug" or a "socket".
 *
 * E.g. the following edge:
 * _П_П_
 * has a total of 5 points: socket, plug, socket, plug, socket.
 *
 * All edges of a face must have the same length m.
 * Each pair of adjacent edges share a common point, which is called a "vertex".
 * Thus, the total number of points for all edges is equal to (4*m - 4).
 * E.g. a face with edges of length 5 has (4*5 - 4) = 16 points in total.
 *
 * Each edge must contain points of both types.
 * This means that each edge must have at least one plug point
 * and at least one socket point.
 *
 */
public class Face {

    private final static int MAX_EDGE_SIZE = Integer.MAX_VALUE / 4;
    private final byte[] edges;

    private List<Edge> edgeList;

    /**
     * @param edges Sequence of connection points for all edges of the face, in clockwise order.
     *              The first element must be a vertex.
     * @param edgeSize Length of an edge.
     */
    public Face(byte[] edges, int edgeSize) {

        if (edgeSize > MAX_EDGE_SIZE) {
            throw new RuntimeException("Maximum edge size exceeded, must be less than or equal to " + MAX_EDGE_SIZE);
        }

        long expectedTotal = edgeSize * 4 - 4;
        if (edges.length != expectedTotal) {
            throw new RuntimeException("Invalid number of points, must be: " + expectedTotal);
        }

        this.edges = edges;
        this.edgeList = collectEdges(edges, edgeSize);
    }

    private List<Edge> collectEdges(byte[] edges, int edgeSize) {

        List<Edge> edgeList = new ArrayList<>(4 + 1);

        for (int i = 0; i < edges.length; i = i + edgeSize - 1) {
            edgeList.add(new DefaultEdge(i, edgeSize));
        }
        return edgeList;
    }

    /**
     * @return List of edges in clockwise order.
     * First edge is the one that starts with the first vertex in the list of vertices,
     * that was used to construct this face.
     */
    public List<Edge> getEdges() {
        return edgeList;
    }

    private class DefaultEdge implements Edge {

        private final int startingPoint;
        private final int endingPoint;
        private final int size;

        private final byte[] points;
        private final byte[] pointsReverse;

        DefaultEdge(int startingPoint, int size) {

            this.startingPoint = startingPoint;
            this.endingPoint = startingPoint + size - 1;
            this.size = size;

            boolean hasDifferentPointTypes = false;
            for (int i = startingPoint; i < endingPoint; i++) {
                if (edges[i] != edges[i + 1]) {
                    hasDifferentPointTypes = true;
                    break;
                }
            }
            if (!hasDifferentPointTypes) {
                throw new IllegalStateException("Invalid edge: all points are " + (edges[0] == 0? "sockets" : "plugs"));
            }

            points = Arrays.copyOfRange(edges, startingPoint, endingPoint + 1);
            if (endingPoint == edges.length) {
                points[points.length - 1] = edges[0];
            }
            pointsReverse = new byte[points.length];
            for (int i = 0; i < points.length; i++) {
                pointsReverse[i] = points[points.length - i - 1];
            }
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public byte[] getPoints() {
            return points;
        }

        @Override
        public byte[] getPointsReverse() {
            return pointsReverse;
        }

        @Override
        public Iterator<Byte> iterator() {
            return new Iterator<Byte>() {

                private int currentPoint = startingPoint;

                public boolean hasNext() {
                    return currentPoint <= endingPoint;
                }

                public Byte next() {
                    try {
                        return ++currentPoint == edges.length + 1? edges[0] : edges[currentPoint - 1];
                    } catch (Exception e) {
                        throw new NoSuchElementException();
                    }
                }

                public void remove() {
                    throw new RuntimeException("Can't remove points from the edge");
                }
            };
        }

        @Override
        public Iterator<Byte> iteratorReverse() {
            return new Iterator<Byte>() {

                private int currentPoint = endingPoint;

                public boolean hasNext() {
                    return currentPoint >= startingPoint;
                }

                public Byte next() {
                    try {
                        return --currentPoint == edges.length - 1? edges[0] : edges[currentPoint + 1];
                    } catch (Exception e) {
                        throw new NoSuchElementException();
                    }
                }

                public void remove() {
                    throw new RuntimeException("Can't remove points from the edge");
                }
            };
        }

        @Override
        public String toString() {
            return Arrays.toString(getPoints());
        }
    }

    @Override
    public String toString() {
        return getEdges().toString();
    }
}
