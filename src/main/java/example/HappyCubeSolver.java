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
        cube.setFace(CubeSide.UPPER, freeFaces.remove(0));
        fixedSides.add(upperSide);
        fixedSides.add(bottomSide);
        fixedSides.add(southernSide);

        int len = freeFaces.size();
        for (int i = 0; i < len; i++) {
            int j = i == 0? i + 1 : i - 1;
            Face bottomFace = freeFaces.remove(i);
            Face southernFace = freeFaces.remove(j);
            cube.setFace(CubeSide.BOTTOM, bottomFace);
            cube.setFace(CubeSide.SOUTHERN, southernFace);
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

        if (cube.isComplete()) { // let the algorithm work even if there's no visitor
            if (visitor != null) {
                visitor.visit(cube);
            }
        } else {
            for (Map.Entry<CubeSide, Side> entry : cube.getSides().entrySet()) {
                CubeSide sideType = entry.getKey();
                Side side = entry.getValue();
                if (!fixedSides.contains(side)) {
                    Face face = faces.get(fixedCount);
                    cube.setFace(sideType, face);
                    fixedSides.add(side);
                    buildCubes(cube, faces, fixedCount + 1, fixedSides);
                    cube.removeFace(sideType);
                    fixedSides.remove(side);
                }
            }
        }
    }
}
