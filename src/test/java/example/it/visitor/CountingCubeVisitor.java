package example.it.visitor;

import example.Cube;
import example.CubeVisitor;

public class CountingCubeVisitor implements CubeVisitor {

    private int resultCount;

    @Override
    public void visit(Cube cube) {
        resultCount++;
    }

    public int getResultCount() {
        return resultCount;
    }
}
