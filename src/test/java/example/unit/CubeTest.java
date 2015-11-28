package example.unit;

import example.Configuration;
import example.Cube;
import example.CubeVisitor;
import example.Face;
import example.Side;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CubeTest {

    @Test
    public void isComplete() {
        Cube cube = new Cube();
        for (Side side : cube.getSides().values()) {
            side.setFace(new Face(new byte[] {1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        assertTrue(cube.isComplete());
    }

    @Test(expected = Exception.class)
    public void isConnected_Exception() {
        Cube cube = new Cube();
        List<Side> sides = new ArrayList<>(cube.getSides().values());
        // do not set face for the "last" side
        for (int i = 0; i < sides.size() - 1; i++) {
            sides.get(i).setFace(new Face(new byte[]{1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        cube.isConnected();
    }

    @Test
    public void visitRotations() {

        Cube cube = new Cube();
        List<Side> sides = new ArrayList<>(cube.getSides().values());
        sides.get(0).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(1).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(2).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(3).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(4).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        sides.get(5).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        final Set<Configuration> configurations = new HashSet<>();
        final int[] rotationCount = new int[] {0, 0};
        CubeVisitor visitor = new CubeVisitor() {
            @Override
            public void visit(Cube cube) {
                rotationCount[0]++;
                configurations.add(Configuration.fromCube(cube));
            }
        };

        cube.visitRotations(visitor);

        // # of cube rotations is equal to 8^6 (see example.Cube description)
        assertEquals((int)Math.pow(8, 6), rotationCount[0]);
        // all rotations must be unique
        assertEquals((int)Math.pow(8, 6), configurations.size());
    }

    @Test
    public void visitRotations_InitialStatePreserved() {

        Cube cube = new Cube();
        List<Side> sides = new ArrayList<>(cube.getSides().values());
        sides.get(0).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(1).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(2).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(3).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(4).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        sides.get(5).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        CubeVisitor visitor = new CubeVisitor() {
            @Override
            public void visit(Cube cube) {
                // do nothing..
            }
        };

        Configuration before = Configuration.fromCube(cube);
        cube.visitRotations(visitor);
        Configuration after = Configuration.fromCube(cube);

        assertTrue(before.equals(after));
    }
}
