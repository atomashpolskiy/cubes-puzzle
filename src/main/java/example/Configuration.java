package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration {

    public static Configuration fromCube(Cube cube) {

        Map<CubeSide, RotatableFace> configuration = new HashMap<>((int)(6 / 0.75));
        for (CubeSide sideType : CubeSide.values()) {
            Side cubeSide = cube.getSides().get(sideType);
            configuration.put(sideType,
                    new RotatedFace(cubeSide.getFace(), cubeSide.getRotationFactor(), cubeSide.isFlipped()));
        }

        return new Configuration(configuration);
    }

    public Map<CubeSide, RotatableFace> getSides() {
        return configuration;
    }

    private Map<CubeSide, RotatableFace> configuration;

    private Configuration(Map<CubeSide, RotatableFace> configuration) {
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

    boolean hasEqualConfiguration(Map<CubeSide, RotatableFace> solution) {
        return this.configuration.equals(solution);
    }

    public static Set<Configuration> buildSymmetricConfigurations(Configuration configuration) {

        Map<CubeSide, RotatableFace> sides = configuration.getSides();

        List<Configuration> horizontalRotations = new ArrayList<>(4 + 1);
        // add original solution
        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 0),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, 0),
                new Shift(CubeSide.WESTERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.NORTHERN, 0)
        ));

        // add 3 horizontal rotations (N -> E -> S -> W); U & B rotating clockwise by 90' at a time
        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 1),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, 1),
                new Shift(CubeSide.WESTERN, CubeSide.NORTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.WESTERN, 0)
        ));

        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 2),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, 2),
                new Shift(CubeSide.WESTERN, CubeSide.EASTERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.NORTHERN, 0)
        ));

        horizontalRotations.add(buildConfiguration(sides,
                new Shift(CubeSide.UPPER, CubeSide.UPPER, 3),
                new Shift(CubeSide.BOTTOM, CubeSide.BOTTOM, 3),
                new Shift(CubeSide.WESTERN, CubeSide.SOUTHERN, 0),
                new Shift(CubeSide.NORTHERN, CubeSide.WESTERN, 0),
                new Shift(CubeSide.EASTERN, CubeSide.NORTHERN, 0),
                new Shift(CubeSide.SOUTHERN, CubeSide.EASTERN, 0)
        ));

        return buildSymmetricConfigurations(horizontalRotations);
    }

    // TODO: detect symmetry for flips (we can do a "real" flip and eliminate several duplicate configurations)
    // by "real" flip we mean flip+rotation (in contrast with cube's flips)
    private static Set<Configuration> buildSymmetricConfigurations(List<Configuration> horizontalRotations) {

        Set<Configuration> configurations = new HashSet<>((int)(horizontalRotations.size() * 6 / 0.75));

        for (Configuration horizontalRotation : horizontalRotations) {

            Map<CubeSide, RotatableFace> sides = horizontalRotation.getSides();
            // 3 clockwise rotations (U -> E -> B -> W); S & N rotating clockwise by 90' at a time
            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.EASTERN, 0),
                    new Shift(CubeSide.EASTERN, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.WESTERN, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.UPPER, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.SOUTHERN, 1),
                    new Shift(CubeSide.NORTHERN, CubeSide.NORTHERN, 1)
            ));

            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.EASTERN, CubeSide.WESTERN, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.UPPER, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.EASTERN, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.SOUTHERN, 2),
                    new Shift(CubeSide.NORTHERN, CubeSide.NORTHERN, 2)
            ));

            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.WESTERN, 0),
                    new Shift(CubeSide.EASTERN, CubeSide.UPPER, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.EASTERN, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.SOUTHERN, 3),
                    new Shift(CubeSide.NORTHERN, CubeSide.NORTHERN, 3)
            ));

            // 3 forward rotations (U -> N -> B -> S); W & E rotating forward by 90' at a time
            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.NORTHERN, 0),
                    new Shift(CubeSide.NORTHERN, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.SOUTHERN, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.UPPER, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.WESTERN, 1),
                    new Shift(CubeSide.EASTERN, CubeSide.EASTERN, 1)
            ));

            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.NORTHERN, CubeSide.SOUTHERN, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.UPPER, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.NORTHERN, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.WESTERN, 2),
                    new Shift(CubeSide.EASTERN, CubeSide.EASTERN, 2)
            ));

            configurations.add(buildConfiguration(sides,
                    new Shift(CubeSide.UPPER, CubeSide.SOUTHERN, 0),
                    new Shift(CubeSide.NORTHERN, CubeSide.UPPER, 0),
                    new Shift(CubeSide.BOTTOM, CubeSide.NORTHERN, 0),
                    new Shift(CubeSide.SOUTHERN, CubeSide.BOTTOM, 0),
                    new Shift(CubeSide.WESTERN, CubeSide.WESTERN, 3),
                    new Shift(CubeSide.EASTERN, CubeSide.EASTERN, 3)
            ));
        }

        return configurations;
    }

    private static Configuration buildConfiguration(Map<CubeSide, ? extends RotatableFace> horizontalRotation, Shift... shifts) {

        Map<CubeSide, RotatableFace> configuration = new HashMap<>((int)(6 / 0.75));
        for (Shift shift : shifts) {
            RotatableFace rotatedFace = horizontalRotation.get(shift.getFrom());
            int rotationFactor = rotatedFace.getRotationFactor() + shift.getRotationFactor();
            if (rotationFactor < 0) {
                rotationFactor = Math.abs(4 - Math.abs(rotationFactor)) % 4;
            } else {
                rotationFactor = rotationFactor % 4;
            }
            configuration.put(shift.getTo(),
                    new RotatedFace(rotatedFace.getFace(), rotationFactor, rotatedFace.isFlipped()));
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
