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

    private final Map<CubeSide, Side> sides;
    private final List<Side> sideList;
    private final int sidesCount;
    private Map<Side, SideEdges> sideEdges;

    {
        sides = new HashMap<>((int)(6 / 0.75));
        sideList = new ArrayList<>(6 + 1);
        // upper side
        Side upperSide = new ModificationAwareSide(CubeVertex.UWN, CubeVertex.UEN, CubeVertex.UES, CubeVertex.UWS);
        sides.put(CubeSide.UPPER, upperSide);
        sideList.add(upperSide);
        // bottom side
        Side bottomSide = new ModificationAwareSide(CubeVertex.DEN, CubeVertex.DWN, CubeVertex.DWS, CubeVertex.DES);
        sides.put(CubeSide.BOTTOM, bottomSide);
        sideList.add(bottomSide);
        // northern side
        Side northernSide = new ModificationAwareSide(CubeVertex.UEN, CubeVertex.UWN, CubeVertex.DWN, CubeVertex.DEN);
        sides.put(CubeSide.NORTHERN, northernSide);
        sideList.add(northernSide);
        // southern side
        Side southernSide = new ModificationAwareSide(CubeVertex.UWS, CubeVertex.UES, CubeVertex.DES, CubeVertex.DWS);
        sides.put(CubeSide.SOUTHERN, southernSide);
        sideList.add(southernSide);
        // eastern side
        Side easternSide = new ModificationAwareSide(CubeVertex.UES, CubeVertex.UEN, CubeVertex.DEN, CubeVertex.DES);
        sides.put(CubeSide.EASTERN, easternSide);
        sideList.add(easternSide);
        // western side
        Side westernSide = new ModificationAwareSide(CubeVertex.UWN, CubeVertex.UWS, CubeVertex.DWS, CubeVertex.DWN);
        sides.put(CubeSide.WESTERN, westernSide);
        sideList.add(westernSide);

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

    public Map<CubeSide, Side> getSides() {
        return sides;
    }

    public boolean isConnected() {

        if (!isComplete()) {
            throw new IllegalStateException("Cube is not complete");
        }

        for (Side side : sideList) {
            if (!sideEdges.get(side).isConnected()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true, if all sides are occupied by faces
     */
    public boolean isComplete() {

        for (Side side : sideList) {
            if (!((ModificationAwareSide)side).isOccupied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Visit all 8^6 or 4^6 possible rotations of this cube
     * (in case when flips are enabled or disabled, respectively)
     *
     * When this routine returns, the cube will in the same state
     * as it was prior to calling the routine.
     */
    public void visitRotations(CubeVisitor visitor) {

        if (!isComplete()) {
            throw new IllegalStateException("Can't visit rotations: cube is not complete");
        }

        visitRotations(visitor, sideList, 0);
    }

    private void visitRotations(CubeVisitor visitor, List<Side> sideList, int fixedCount) {

        if (fixedCount == sidesCount) {
            visitor.visit(this);
        } else {

            ModificationAwareSide side = (ModificationAwareSide) sideList.get(fixedCount);
            for (int i = 0; i < 4; i++) {
                visitRotations(visitor, sideList, fixedCount + 1); // initial position, if i = 0
                side.flip();
                visitRotations(visitor, sideList, fixedCount + 1);
                side.flip();

                side.rotate(); // return to initial position, if i = 3
            }
        }
    }

    public void setFace(CubeSide side, Face face) {
        ((ModificationAwareSide)sides.get(side)).setFace(face);
    }

    public void removeFace(CubeSide side) {
        ((ModificationAwareSide)sides.get(side)).removeFace();
    }

    private class ModificationAwareSide extends DefaultSide {

        /**
         * @param vertices List of vertices in traversal order.
         */
        ModificationAwareSide(CubeVertex... vertices) {
            super(Arrays.asList(vertices));
        }

        @Override
        protected void setFace(Face face) {
            super.setFace(face);
        }

        protected void rotate() {
            super.rotate();
        }

        protected void flip() {
            super.flip();
        }
    }

    private class CubeEdge {

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
            return canConnect(s1, s2);
        }

        private boolean canConnect(Side s1, Side s2) {

            Edge e1 = s1.getEdge(v1);
            Edge e2 = s2.getEdge(v2);

            // sanity check
            if (e1.getSize() != e2.getSize()) {
                throw new IllegalStateException("Invalid cube configuration: edges have different lengths");
            }

            // if two sides can connect in straight position,
            // then they will also connect in flipped position
            Iterator<Byte> iter1 = s1.isFlipped()? e1.getPointsReverse() : e1.getPoints();
            Iterator<Byte> iter2 = s2.isFlipped()? e2.getPoints() : e2.getPointsReverse();

            int edgeSize = e1.getSize();
            int i = 0;
            while (iter1.hasNext()) {
                // check that one point is a plug and another is a socket
                Byte p1 = iter1.next(), p2 = iter2.next();
                if (p1.equals(p2)) {
                    if (p1 == 0 && p2 == 0 && ((i == 0) || (i == edgeSize-1))) {
                        // special case here: cube's vertex
                        // even if compared face's edges both have sockets,
                        // there's still two adjacent cube's edges,
                        // that might have a plug in this point
                        // -- need to check
                        CubeVertex vertex = (i == 0)? v1 : v2;
                        boolean hasPlug = false;
                        for (Side side : sideList) {
                            if (side.hasVertex(vertex)) {
                                Edge edge = side.getEdge(vertex);
                                Iterator<Byte> iter = side.isFlipped()? edge.getPointsReverse() : edge.getPoints();
                                hasPlug = iter.next() == 1;
                                if (hasPlug) {
                                    break;
                                }
                            }
                        }
                        if (!hasPlug) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                i++;
            }
            return true;
        }
    }

    private static class SideEdges {

        private boolean isConnected;
        private CubeEdge[] cubeEdges;

        SideEdges(CubeEdge... cubeEdges) {
            this.cubeEdges = cubeEdges;
        }

        public boolean isConnected() {

            boolean isConnected = this.isConnected;
            for (CubeEdge cubeEdge : cubeEdges) {
                isConnected = cubeEdge.isConnected();
                if (!isConnected) {
                    break;
                }
            }

            this.isConnected = isConnected;
            return isConnected;
        }
    }
}
