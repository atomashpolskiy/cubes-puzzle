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

        List<Face> freeFaces = new LinkedList<>();
        List<Side> fixedSides = new ArrayList<>(faces.size() + 1);
        cube.setFace(CubeSide.UPPER, faces.get(0));
        fixedSides.add(upperSide);
        fixedSides.add(bottomSide);
        fixedSides.add(southernSide);

        int len = faces.size();
        for (int i = 1; i < len; i++) {
            Face bottomFace = faces.get(i);
            for (int j = 1; j < len; j++) {
                if (i == j) {
                    continue;
                }
                Face southernFace = faces.get(j);
                cube.setFace(CubeSide.BOTTOM, bottomFace);
                cube.setFace(CubeSide.SOUTHERN, southernFace);
                for (int k = 1; k < faces.size(); k++) {
                    if (k != i && k != j) {
                        freeFaces.add(faces.get(k));
                    }
                }
                buildCubes(cube, freeFaces, 0, fixedSides);
                freeFaces.clear();
            }
        }
    }

    private void buildCubes(Cube cube, List<Face> faces, int fixedCount, List<Side> fixedSides) {

        if (cube.isComplete()) { // let the algorithm work even if there's no visitor
            if (visitor != null) {
                visitor.visit(cube);
            }
        } else {
            for (CubeSide cubeSide : CubeSide.values()) {
                Side side = cube.getSides().get(cubeSide);
                if (!fixedSides.contains(side)) {
                    Face face = faces.get(fixedCount);
                    cube.setFace(cubeSide, face);
                    fixedSides.add(side);
                    buildCubes(cube, faces, fixedCount + 1, fixedSides);
                    cube.removeFace(cubeSide);
                    fixedSides.remove(side);
                }
            }
        }
    }
}
