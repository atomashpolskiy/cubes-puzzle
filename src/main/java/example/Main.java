package example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Face> blueFaces = new ArrayList<>(6 + 1);
        blueFaces.add(new Face(new byte[] {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0}, 5));
        blueFaces.add(new Face(new byte[] {1,0,1,0,1,1,0,1,1,0,1,0,1,1,0,1}, 5));
        blueFaces.add(new Face(new byte[] {0,0,1,0,0,1,0,1,0,0,1,0,0,0,1,0}, 5));
        blueFaces.add(new Face(new byte[] {0,1,0,1,0,1,0,1,1,1,0,1,1,0,1,0}, 5));
        blueFaces.add(new Face(new byte[] {0,1,0,1,0,1,0,1,0,0,1,0,1,1,0,1}, 5));
        blueFaces.add(new Face(new byte[] {0,1,0,1,0,0,1,0,0,1,0,1,1,1,0,1}, 5));

        solveExample(blueFaces, new File("blue.txt"));

        List<Face> redFaces = new ArrayList<>(6 + 1);
        redFaces.add(new Face(new byte[] {0,0,0,1,1,0,1,0,1,1,0,1,0,0,1,0}, 5));
        redFaces.add(new Face(new byte[] {0,1,0,1,0,0,1,0,0,0,0,1,0,1,0,1}, 5));
        redFaces.add(new Face(new byte[] {0,1,1,0,1,1,0,1,1,1,0,0,1,1,0,1}, 5));
        redFaces.add(new Face(new byte[] {0,1,1,0,0,0,1,0,1,1,0,1,1,0,1,0}, 5));
        redFaces.add(new Face(new byte[] {0,0,1,1,0,1,0,1,0,0,1,0,1,1,0,1}, 5));
        redFaces.add(new Face(new byte[] {0,0,1,0,0,0,1,0,0,0,1,0,0,1,0,1}, 5));

        solveExample(redFaces, new File("red.txt"));

        List<Face> purpleFaces = new ArrayList<>(6 + 1);
        purpleFaces.add(new Face(new byte[] {1,1,0,1,0,0,0,1,0,0,1,0,0,0,1,1}, 5));
        purpleFaces.add(new Face(new byte[] {0,0,0,1,1,0,1,0,0,1,0,1,0,0,1,1}, 5));
        purpleFaces.add(new Face(new byte[] {0,1,0,0,0,0,1,0,0,0,1,0,0,1,0,1}, 5));
        purpleFaces.add(new Face(new byte[] {0,1,0,1,1,0,1,0,0,1,0,1,1,1,0,0}, 5));
        purpleFaces.add(new Face(new byte[] {0,0,1,0,1,1,1,0,0,1,1,0,1,1,1,0}, 5));
        purpleFaces.add(new Face(new byte[] {1,1,0,1,1,1,0,0,0,1,0,1,0,0,1,0}, 5));

        solveExample(purpleFaces, new File("purple.txt"));

        List<Face> goldenFaces = new ArrayList<>(6 + 1);
        goldenFaces.add(new Face(new byte[] {0,0,1,0,0,0,1,0,0,1,0,1,0,1,0,1}, 5));
        goldenFaces.add(new Face(new byte[] {0,0,1,0,1,1,0,0,0,1,0,1,0,1,0,1}, 5));
        goldenFaces.add(new Face(new byte[] {0,0,1,0,1,1,0,1,0,0,1,0,1,1,1,0}, 5));
        goldenFaces.add(new Face(new byte[] {1,0,1,0,1,1,0,1,0,0,1,0,1,1,0,1}, 5));
        goldenFaces.add(new Face(new byte[] {0,0,1,0,0,1,0,1,0,1,0,1,1,0,1,0}, 5));
        goldenFaces.add(new Face(new byte[] {0,1,0,1,0,0,1,0,1,1,0,1,0,0,1,0}, 5));

        solveExample(goldenFaces, new File("golden.txt"));
    }

    private static void solveExample(List<Face> faces, File file) {

        try (ConfigurationWriter writer = ConfigurationWriter.fileWriter(file)) {
            writer.writeFaces(faces);

            CountingCubeVisitor visitor = new CountingCubeVisitor(writer);
            long t1 = System.currentTimeMillis();

            Solver.happyCube(faces).cubeVisitor(visitor).uniqueSolutions().solve();

            System.out.println(String.format("Solved in %d ms. Total solutions: %d. Saved to: %s",
                    (System.currentTimeMillis() - t1), visitor.getResultCount(), file.getAbsolutePath()));
            System.out.println();
            System.out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class CountingCubeVisitor implements CubeVisitor {

        private ConfigurationWriter writer;
        private int resultCount;

        CountingCubeVisitor(ConfigurationWriter writer) {
            this.writer = writer;
        }

        @Override
        public void visit(Cube cube) {
            resultCount++;
            writer.writeConfiguration(Configuration.fromCube(cube));
        }

        public int getResultCount() {
            return resultCount;
        }
    }
}
