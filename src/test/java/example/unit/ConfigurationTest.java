package example.unit;

import example.Configuration;
import example.Cube;
import example.Face;
import example.Side;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    @Test
    public void fromSameCube_Equal() {

        Cube cube = new Cube();
        List<Side> sides = new ArrayList<>(cube.getSides().values());
        sides.get(0).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(1).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(2).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(3).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(4).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        sides.get(5).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        Configuration c1 = Configuration.fromCube(cube);
        Configuration c2 = Configuration.fromCube(cube);

        assertTrue(c1.equals(c2));
    }

    @Test
    public void buildSymmetricConfigurations_AllDistinct() {

        Cube cube = new Cube();
        List<Side> sides = new ArrayList<>(cube.getSides().values());
        sides.get(0).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(1).setFace(new Face(new byte[]{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0}, 5));
        sides.get(2).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(3).setFace(new Face(new byte[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0}, 5));
        sides.get(4).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));
        sides.get(5).setFace(new Face(new byte[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, 5));

        Set<Configuration> symmetricConfigurations =
                Configuration.buildSymmetricConfigurations(Configuration.fromCube(cube));

        assertEquals(24, symmetricConfigurations.size());

        symmetricConfigurations.addAll(
                Configuration.buildSymmetricConfigurations(Configuration.fromCube(cube))
        );

        assertEquals(24, symmetricConfigurations.size());
    }
}
