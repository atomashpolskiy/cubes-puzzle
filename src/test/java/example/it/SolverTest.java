package example.it;

import example.Face;
import example.Solver;
import example.it.visitor.CountingCubeVisitor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SolverTest {

    @Test
    public void solveHappyCube_NumberOfSolutions() {

        List<Face> faces = new ArrayList<>(6 + 1);
        faces.add(new Face(new byte[] {0,0,1,0,0,0,1,0,0,1,0,1,0,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,1,1,0,0,0,1,0,1,0,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,1,1,0,1,0,0,1,0,1,1,1,0}, 5));
        faces.add(new Face(new byte[] {1,0,1,0,1,1,0,1,0,0,1,0,1,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,0,1,0,1,0,1,0,1,1,0,1,0}, 5));
        faces.add(new Face(new byte[] {0,1,0,1,0,0,1,0,1,1,0,1,0,0,1,0}, 5));

        CountingCubeVisitor visitor1 = new CountingCubeVisitor();
        Solver.happyCube(faces).cubeVisitor(visitor1).solve();

        assertEquals(64, visitor1.getResultCount());
    }

    @Test
    public void solveHappyCube_NumberOfUniqueSolutions() {

        List<Face> faces = new ArrayList<>(6 + 1);
        faces.add(new Face(new byte[] {0,0,1,0,0,0,1,0,0,1,0,1,0,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,1,1,0,0,0,1,0,1,0,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,1,1,0,1,0,0,1,0,1,1,1,0}, 5));
        faces.add(new Face(new byte[] {1,0,1,0,1,1,0,1,0,0,1,0,1,1,0,1}, 5));
        faces.add(new Face(new byte[] {0,0,1,0,0,1,0,1,0,1,0,1,1,0,1,0}, 5));
        faces.add(new Face(new byte[] {0,1,0,1,0,0,1,0,1,1,0,1,0,0,1,0}, 5));

        CountingCubeVisitor visitor1 = new CountingCubeVisitor();
        Solver.happyCube(faces).cubeVisitor(visitor1).uniqueSolutions().solve();

        assertEquals(16, visitor1.getResultCount());
    }
}
