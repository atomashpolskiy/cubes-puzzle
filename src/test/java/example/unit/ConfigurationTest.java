package example.unit;

import example.Configuration;
import example.Cube;
import example.CubeSide;
import example.Face;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    @Test
    public void fromSameCube_Equal() {

        Cube cube = new Cube();
        cube.setFace(CubeSide.UPPER, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.BOTTOM, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.WESTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.EASTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.NORTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        cube.setFace(CubeSide.SOUTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        Configuration c1 = Configuration.fromCube(cube);
        Configuration c2 = Configuration.fromCube(cube);

        assertTrue(c1.equals(c2));
    }

    @Test
    public void buildSymmetricConfigurations_AllDistinct() {

        Cube cube = new Cube();
        cube.setFace(CubeSide.UPPER, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.BOTTOM, new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        cube.setFace(CubeSide.WESTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.EASTERN, new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        cube.setFace(CubeSide.NORTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        cube.setFace(CubeSide.SOUTHERN, new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        Set<Configuration> symmetricConfigurations =
                Configuration.buildSymmetricConfigurations(Configuration.fromCube(cube));

        assertEquals(24, symmetricConfigurations.size());

        symmetricConfigurations.addAll(
                Configuration.buildSymmetricConfigurations(Configuration.fromCube(cube))
        );

        assertEquals(24, symmetricConfigurations.size());
    }
}
