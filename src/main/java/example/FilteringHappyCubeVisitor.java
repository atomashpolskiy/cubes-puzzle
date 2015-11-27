package example;

import java.util.HashSet;
import java.util.Set;

class FilteringHappyCubeVisitor implements CubeVisitor {

    final private CubeVisitor delegate;
    final private boolean shouldFindUniqueSolutions;
    private Set<Configuration> solutions = new HashSet<>();

    FilteringHappyCubeVisitor(CubeVisitor delegate, boolean shouldFindUniqueSolutions) {
        this.delegate = delegate;
        this.shouldFindUniqueSolutions = shouldFindUniqueSolutions;
    }

    @Override
    public void visit(Cube cube) {

        CubeVisitor rotationVisitor = new CubeVisitor() {
            @Override
            public void visit(Cube cube) {
                if (cube.isConnected()) {
                    if (!shouldFindUniqueSolutions || ensureSolutionIsUnique(cube)) {
                        if (delegate != null) {
                            delegate.visit(cube);
                        }
                    }
                }
            }
        };

        cube.visitRotations(rotationVisitor);
    }

    private boolean ensureSolutionIsUnique(Cube cube) {

        Configuration solution = Configuration.fromCube(cube);

        if (solutions.contains(solution)) {
            return false;
        }

        solutions.addAll(Configuration.buildSymmetricConfigurations(solution));
        return true;
    }



}
