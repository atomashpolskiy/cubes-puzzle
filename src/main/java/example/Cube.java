package example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Cube is a typical 3-D cube. It has:
 * - 6 sides
 * - 12 edges
 * - 8 vertices
 *
 * Cube is stateful. The initial state is determined by a list of faces that it's constructed of.
 * Each side of a cube can be rotated 4 times by 90 degrees at a time.
 * After the 4th rotation, the side gets into the initial position.
 * Each distinct rotation of a cube's side results in the side's face being superimposed
 * over the cube's side by a different angle, thus changing the way that this side's face is connected
 * with faces in the adjacent sides.
 *
 * The individual states of a cube are called "rotations". They can't be accessed directly.
 * Rather, it is possible to iterate through all the 4^6 = 4096 possible rotations
 * via example.Cube#visitRotations(example.CubeVisitor).
 *
 * Each rotation of a cube can be either "connected" or "disconnected".
 * If all 12 edges of the cube are connected (which means that corresponding edges of the adjacent sides
 * can be connected), then such rotation of a cube is called "connected", otherwise it's not.
 */
public class Cube {

    private final List<Side> sides;
    private final int sidesCount;
    private Map<Side, SideEdges> sideEdges;

    {
        sides = new ArrayList<>(6 + 1);
        // upper side
        Side upperSide = new DefaultSide(CubeVertex.UWN, CubeVertex.UEN, CubeVertex.UES, CubeVertex.UWS);
        sides.add(upperSide);
        // bottom side
        Side bottomSide = new DefaultSide(CubeVertex.DEN, CubeVertex.DWN, CubeVertex.DWS, CubeVertex.DES);
        sides.add(bottomSide);
        // northern side
        Side northernSide = new DefaultSide(CubeVertex.UEN, CubeVertex.UWN, CubeVertex.DWN, CubeVertex.DEN);
        sides.add(northernSide);
        // southern side
        Side southernSide = new DefaultSide(CubeVertex.UWS, CubeVertex.UES, CubeVertex.DES, CubeVertex.DWS);
        sides.add(southernSide);
        // eastern side
        Side easternSide = new DefaultSide(CubeVertex.UES, CubeVertex.UEN, CubeVertex.DEN, CubeVertex.DES);
        sides.add(easternSide);
        // western side
        Side westernSide = new DefaultSide(CubeVertex.UWN, CubeVertex.UWS, CubeVertex.DWS, CubeVertex.DWN);
        sides.add(westernSide);

        sidesCount = sides.size();

        CubeEdge UN = new CubeEdge(upperSide, CubeVertex.UWN, northernSide, CubeVertex.UEN);
        CubeEdge UE = new CubeEdge(upperSide, CubeVertex.UEN, easternSide, CubeVertex.UES);
        CubeEdge UW = new CubeEdge(upperSide, CubeVertex.UWS, westernSide, CubeVertex.UWN);
        CubeEdge US = new CubeEdge(upperSide, CubeVertex.UES, southernSide, CubeVertex.UWS);
        CubeEdge DN = new CubeEdge(bottomSide, CubeVertex.DEN, northernSide, CubeVertex.DWN);
        CubeEdge DE = new CubeEdge(bottomSide, CubeVertex.DES, easternSide, CubeVertex.DEN);
        CubeEdge DW = new CubeEdge(bottomSide, CubeVertex.DWN, westernSide, CubeVertex.DWS);
        CubeEdge DS = new CubeEdge(bottomSide, CubeVertex.DWS, southernSide, CubeVertex.DES);
        CubeEdge ES = new CubeEdge(easternSide, CubeVertex.DES, southernSide, CubeVertex.UES);
        CubeEdge EN = new CubeEdge(easternSide, CubeVertex.UEN, northernSide, CubeVertex.DEN);
        CubeEdge WS = new CubeEdge(westernSide, CubeVertex.UWS, southernSide, CubeVertex.DWS);
        CubeEdge WN = new CubeEdge(westernSide, CubeVertex.DWN, northernSide, CubeVertex.UWN);

        sideEdges = new HashMap<>((int)(4 / 0.75));
        sideEdges.put(upperSide, new SideEdges(UN, UE, UW, US));
        sideEdges.put(bottomSide, new SideEdges(DN, DE, DW, DS));
        sideEdges.put(northernSide, new SideEdges(UN, DN, EN, WN));
        sideEdges.put(southernSide, new SideEdges(US, DS, ES, WS));
        sideEdges.put(easternSide, new SideEdges(UE, DE, ES, EN));
        sideEdges.put(westernSide, new SideEdges(UW, DW, WS, WN));
    }

    public List<Side> getSides() {
        return sides;
    }

    public boolean isConnected() {

        if (!isComplete()) {
            throw new IllegalStateException("Cube is not complete");
        }

        for (SideEdges sideEdge : sideEdges.values()) {
            if (!sideEdge.isConnected()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true, if all sides are occupied by faces
     */
    public boolean isComplete() {

        for (Side side : sides) {
            if (!side.isOccupied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Visit all 4^6 possible rotations of this cube.
     * When this routine returns, the cube will in the same state
     * as it was prior to calling the routine.
     */
    public void visitRotations(CubeVisitor visitor) {

        if (!isComplete()) {
            throw new IllegalStateException("Cube is not complete");
        }

        visitRotations(visitor, 0);
    }

    private void visitRotations(CubeVisitor visitor, int fixedCount) {

        if (fixedCount == sidesCount) {
            visitor.visit(this);
        } else {
            DefaultSide side = (DefaultSide) sides.get(fixedCount);
            visitRotations(visitor, fixedCount + 1);
            side.rotate();
            visitRotations(visitor, fixedCount + 1);
            side.rotate();
            visitRotations(visitor, fixedCount + 1);
            side.rotate();
            visitRotations(visitor, fixedCount + 1);
            side.rotate(); // return to initial position
        }
    }

    private class DefaultSide implements Side {

        private List<CubeVertex> vertices;
        /**
         * The side of a cube has junctions with the occupying face;
         * each of the 4 vertices is a junction.
         * Junction of the first vertex of the face and some vertex of the side
         * is called a "junction point".
         * The junction point is a starting point for controlling
         * rotation of the face with regard to the containing cube.
         *
         * Values: 0,1,2,3 or -1 if this side is unoccupied.
         */
        private int junctionPoint = -1;
        private Face face;

        /**
         * @param vertices List of vertices in traversal order.
         */
        DefaultSide(CubeVertex... vertices) {
            this.vertices = Arrays.asList(vertices);
        }

        @Override
        public boolean isOccupied() {
            return face != null && junctionPoint >= 0;
        }

        @Override
        public void setFace(Face face) {
            this.face = face;
            this.junctionPoint = 0;
            sideEdges.get(this).setChanged();
        }

        @Override
        public void removeFace() {
            this.face = null;
            this.junctionPoint = -1;
        }

        @Override
        public Face getFace() {
            return face;
        }

        void rotate() {

            if (!isOccupied()) {
                throw new IllegalStateException("Failed to get face's edge: side is unoccupied");
            }

            junctionPoint = (++junctionPoint % 4);
            sideEdges.get(this).setChanged();
        }

        /**
         * @return Edge of the occupying face, that begins with the specified vertex.
         * @throws IllegalStateException if side is unoccupied.
         */
        @Override
        public Edge getEdge(CubeVertex vertex) {

            if (!isOccupied()) {
                throw new IllegalStateException("Failed to get face's edge: side is unoccupied");
            }

            if (!vertices.contains(vertex)) {
                throw new IllegalArgumentException("Side does not have a vertex: " + vertex.name());
            }

            // Edge's index depends on the rotation of the face.
            // If the face is not rotated (junction point is 0), then edge's index in face's list of edges
            // is just the index of the vertex, passed as an argument.
            // If the face is rotated,
            //  then edge's index is (4 - [junction point index] + [vertex index]) % 4
            return face.getEdges().get((4 - junctionPoint + vertices.indexOf(vertex)) % 4);
        }
    }

    private static class CubeEdge {

        private Side s1, s2;
        private CubeVertex v1, v2;

        /**
         * @param s1 Side #1
         * @param v1 Starting vertex of an edge between s1 and s2, when traversing s1 clockwise.
         * @param s2 Side #2
         * @param v2 Starting vertex of an edge between s2 and s1, when traversing s2 clockwise.
         */
        CubeEdge(Side s1, CubeVertex v1, Side s2, CubeVertex v2) {
            this.s1 = s1;
            this.s2 = s2;
            this.v1 = v1;
            this.v2 = v2;
        }

        boolean isConnected() {

            Edge edge1 = s1.getEdge(v1);
            Edge edge2 = s2.getEdge(v2);
            return canConnect(edge1, edge2);
        }

        private static boolean canConnect(Edge e1, Edge e2) {

            Iterator<Byte> iter1 = e1.getPoints();
            Iterator<Byte> iter2 = e2.getPointsReverse();

            while (iter1.hasNext() && iter2.hasNext()) {
                // check that one point is a plug and another is a socket
                // TODO: need to treat cube's vertices in a special way:
                // if it's a socket in the faces that are currently compared,
                // then we must check if it's occupied by
                // the third face, that is adjacent with this vertex.
                if (iter1.next().equals(iter2.next())) {
                    return false;
                }
            }

            // sanity check
            if (iter1.hasNext() || iter2.hasNext()) {
                throw new IllegalStateException("Invalid cube configuration: edges have different lengths");
            }

            return true;
        }
    }

    private static class SideEdges {

        private boolean isChanged;
        private boolean isConnected;
        private CubeEdge[] cubeEdges;

        SideEdges(CubeEdge... cubeEdges) {
            this.cubeEdges = cubeEdges;
        }

        public boolean isConnected() {

            if (isChanged) {
                for (CubeEdge cubeEdge : cubeEdges) {
                    isConnected = cubeEdge.isConnected();
                    if (!isConnected) {
                        break;
                    }
                }
            }

            isChanged = false;
            return isConnected;
        }

        public void setChanged() {
            isChanged = true;
        }
    }
}
