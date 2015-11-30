package example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Configuration {

    public static Configuration fromCube(Cube cube) {

        Map<CubeSide, SideConfiguration> configuration = new HashMap<>((int)(6 / 0.75));
        for (CubeSide sideType : CubeSide.values()) {
            Side cubeSide = cube.getSides().get(sideType);
            configuration.put(sideType,
                    new SideConfiguration(cubeSide.getVertices(), cubeSide.getFace(),
                            cubeSide.getRotationFactor(), cubeSide.isFlipped()));
        }

        return new Configuration(configuration);
    }

    public Map<CubeSide, SideConfiguration> getSides() {
        return configuration;
    }

    private Map<CubeSide, SideConfiguration> configuration;

    private Configuration(Map<CubeSide, SideConfiguration> configuration) {
        this.configuration = configuration;
    }

    @Override
    public int hashCode() {
        return configuration.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !Configuration.class.equals(object.getClass())) {
            return false;
        }
        return ((Configuration)object).hasEqualConfiguration(configuration);
    }

    boolean hasEqualConfiguration(Map<CubeSide, SideConfiguration> solution) {
        return this.configuration.equals(solution);
    }

    public static Set<Configuration> buildSymmetricConfigurations(Configuration configuration) {

        Map<CubeSide, SideConfiguration> sides = configuration.getSides();

        Set<Configuration> horizontalRotations = new HashSet<>((int)(4 / 0.75));
        // add original solution
        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 0),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, 0),
                new Shift(CubeSide.WESTERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.NORTHERN, 0)
        ));

        // add 3 horizontal rotations (N -> E -> S -> W); U rotating clockwise & B rotating counter-clockwise by 90' at a time
        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 1),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, -1),
                new Shift(CubeSide.WESTERN, CubeSide.NORTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.WESTERN, 0)
        ));

        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 2),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, -2),
                new Shift(CubeSide.WESTERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.NORTHERN, 0)
        ));

        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 3),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, -3),
                new Shift(CubeSide.WESTERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.NORTHERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.EASTERN, 0)
        ));

        return horizontalRotations;
    }

    private static Configuration buildConfiguration(Map<CubeSide, ? extends SideConfiguration> horizontalRotation, Shift... shifts) {

        Map<CubeSide, SideConfiguration> configuration = new HashMap<>((int)(6 / 0.75));
        for (Shift shift : shifts) {
            SideConfiguration rotatedFace = horizontalRotation.get(shift.getFrom());
            int rotationFactor = rotatedFace.getRotationFactor() + shift.getRotationFactor();
            if (rotationFactor < 0) {
                rotationFactor = Math.abs(4 - Math.abs(rotationFactor)) % 4;
            } else {
                rotationFactor = rotationFactor % 4;
            }
            configuration.put(shift.getTo(),
                    new SideConfiguration(rotatedFace.getVertices(), rotatedFace.getFace(),
                            rotationFactor, rotatedFace.isFlipped()));
        }
        return new Configuration(configuration);
    }

    private static class Shift {

        private CubeSide from, to;
        private int rotationFactor;

        public Shift(CubeSide from, CubeSide to, int rotationFactor) {

            this.from = from;
            this.to = to;
            this.rotationFactor = rotationFactor;
        }

        public CubeSide getFrom() {
            return from;
        }

        public CubeSide getTo() {
            return to;
        }

        public int getRotationFactor() {
            return rotationFactor;
        }
    }
}
