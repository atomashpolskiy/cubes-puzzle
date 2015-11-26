package example.unit;

import example.Cube;
import example.CubeVisitor;
import example.Face;
import example.Side;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CubeTest {

    @Test
    public void isComplete() {
        Cube cube = new Cube();
        for (Side side : cube.getSides()) {
            side.setFace(new Face(new byte[] {1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        assertTrue(cube.isComplete());
    }

    @Test(expected = Exception.class)
    public void isConnected_Exception() {
        Cube cube = new Cube();
        List<Side> sides = cube.getSides();
        // do not set face for the "last" side
        for (int i = 0; i < sides.size() - 1; i++) {
            sides.get(i).setFace(new Face(new byte[]{1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        cube.isConnected();
    }

    @Test
    public void visitRotations() {

        Cube cube = new Cube();
        List<Side> sides = cube.getSides();
        sides.get(0).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(1).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(2).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(3).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(4).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        sides.get(5).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        final int[] rotationCount = new int[] {0, 0};
        CubeVisitor visitor = new CubeVisitor() {
            @Override
            public void visit(Cube cube) {
                rotationCount[0]++;

                if (cube.isConnected()) {
                    rotationCount[1]++;
                }
            }
        };

        cube.visitRotations(visitor);

        // # of cube rotations is equal to 4^6 = 4096 (see example.Cube description)
        assertEquals(4096, rotationCount[0]);
        // TODO: Set correct number
        assertEquals(0, rotationCount[1]);
    }
}
