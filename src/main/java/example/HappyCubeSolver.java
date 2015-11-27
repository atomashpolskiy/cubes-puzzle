package example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class HappyCubeSolver {

    HappyCubeSolver(CubeVisitor visitor) {
        this.visitor = visitor;
    }

    private CubeVisitor visitor;

    void solve(List<Face> faces) {

        Cube cube = new Cube();

        Map<CubeSide, Side> sides = cube.getSides();
        Side upperSide = sides.get(CubeSide.UPPER), bottomSide = sides.get(CubeSide.BOTTOM),
            southernSide = sides.get(CubeSide.SOUTHERN);

        List<Face> freeFaces = new LinkedList<>(faces);
        List<Side> fixedSides = new ArrayList<>(faces.size() + 1);
        upperSide.setFace(freeFaces.remove(0));
        fixedSides.add(upperSide);
        fixedSides.add(bottomSide);
        fixedSides.add(southernSide);

        int len = freeFaces.size();
        for (int i = 0; i < len; i++) {
            int j = i == 0? i + 1 : i - 1;
            Face bottomFace = freeFaces.remove(i);
            Face southernFace = freeFaces.remove(j);
            bottomSide.setFace(bottomFace);
            southernSide.setFace(southernFace);
            buildCubes(cube, freeFaces, 0, fixedSides);
            if (i > freeFaces.size()) {
                freeFaces.add(bottomFace);
            } else {
                freeFaces.add(i, bottomFace);
            }
            if (j > freeFaces.size()) {
                freeFaces.add(southernFace);
            } else {
                freeFaces.add(j, southernFace);
            }
        }
    }

    private void buildCubes(Cube cube, List<Face> faces, int fixedCount, List<Side> fixedSides) {

        List<Side> sides = new ArrayList<>(cube.getSides().values());
        if (cube.isComplete()) { // let the algorithm work even if there's no visitor
            if (visitor != null) {
                visitor.visit(cube);
            }
        } else {
            for (Side side : sides) {
                if (!fixedSides.contains(side)) {
                    Face face = faces.get(fixedCount);
                    side.setFace(face);
                    fixedSides.add(side);
                    buildCubes(cube, faces, fixedCount + 1, fixedSides);
                    side.removeFace();
                    fixedSides.remove(side);
                }
            }
        }
    }
}
