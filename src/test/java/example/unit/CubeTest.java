package example.unit;

import example.Configuration;
import example.Cube;
import example.CubeSide;
import example.CubeVisitor;
import example.Face;
import example.Side;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CubeTest {

    @Test
    public void isComplete() {
        Cube cube = new Cube();
        for (Map.Entry<CubeSide, Side> entry : cube.getSides().entrySet()) {
            cube.setFace(entry.getKey(), new Face(new byte[] {1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        assertTrue(cube.isComplete());
    }

    @Test(expected = Exception.class)
    public void isConnected_Exception() {
        Cube cube = new Cube();
        CubeSide[] sides = new CubeSide[]{CubeSide.NORTHERN, CubeSide.SOUTHERN, CubeSide.EASTERN, CubeSide.WESTERN};
        for (CubeSide side : sides) {
            cube.setFace(side, new Face(new byte[]{1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1}, 5));
        }

        cube.isConnected();
    }

    @Test
    public void visitRotations() {

        Cube cube = new Cube();
        cube.setFace(CubeSide.UPPER, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.BOTTOM, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.WESTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.EASTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.NORTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        cube.setFace(CubeSide.SOUTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

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
        cube.setFace(CubeSide.UPPER, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.BOTTOM, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.WESTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.EASTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.NORTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        cube.setFace(CubeSide.SOUTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

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
