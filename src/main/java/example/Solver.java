package example;

import java.util.List;

public class Solver {

    private Solver() {
    }

    public static HappyCube happyCube(List<Face> faces) {
        return new HappyCubeBuilder(faces);
    }

    private static class HappyCubeBuilder implements HappyCube {

        private List<Face> faces;
        private CubeVisitor visitor;
        private boolean shouldFindUniqueSolutions;

        HappyCubeBuilder(List<Face> faces) {
            this.faces = faces;
        }

        @Override
        public HappyCube cubeVisitor(CubeVisitor visitor) {
            this.visitor = visitor;
            return this;
        }

        @Override
        public HappyCube uniqueSolutions() {
            shouldFindUniqueSolutions = true;
            return this;
        }

        @Override
        public void solve() {
            new HappyCubeSolver(new FilteringHappyCubeVisitor(visitor, shouldFindUniqueSolutions)).solve(faces);
        }
    }
}
