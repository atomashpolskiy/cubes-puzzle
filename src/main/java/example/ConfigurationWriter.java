package example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigurationWriter implements AutoCloseable {

    private final static char WHITESPACE = ' ';
    private final static char SOCKET = ' ';
    private final static char PLUG = 'X';
    private final static int MARGIN_SIZE = 3;

    private final PrintWriter writer;

    private int solutionsPrinted;

    private ConfigurationWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public static ConfigurationWriter fileWriter(File file) {

        if (file.isDirectory()) {
            throw new IllegalArgumentException("Can't write solutions: file is directory: " + file.getAbsolutePath());
        }

        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("Failed to create file: " + file.getAbsolutePath());
            }
            return new ConfigurationWriter(new PrintWriter(file));

        } catch (IOException e) {
            throw new RuntimeException("Failed to create file: " + file.getAbsolutePath(), e);
        }
    }

    public void writeFaces(List<Face> faces) {

        if (faces.isEmpty()) {
            throw new IllegalArgumentException("Can't print: no faces");
        }

        int edgeSize = faces.get(0).getEdges().get(0).getSize();

        writer.print(String.format("Initial faces (edge size = %d):", edgeSize));
        writer.println();
        writer.println();

        BlockWriter blockWriter = new BlockWriter(edgeSize);

        int faceCount = faces.size();
        Edge[] edges = new Edge[4];
        for (int i = 0; i < faceCount; i = i + 3) {

            Face leftFace = faces.get(i);
            Block leftBlock = buildBlock(false, leftFace.getEdges().toArray(edges));
            Block centerBlock = null, rightBlock = null;
            if (i + 1 < faceCount) {
                Face centerFace = faces.get(i + 1);
                centerBlock = buildBlock(false, centerFace.getEdges().toArray(edges));
            }
            if (i + 2 < faceCount) {
                Face rightFace = faces.get(i + 2);
                rightBlock = buildBlock(false, rightFace.getEdges().toArray(edges));
            }

            blockWriter.writeBlocks(leftBlock, centerBlock, rightBlock);
        }
    }

    public void writeConfiguration(Configuration configuration) {

        Map<CubeSide, SideConfiguration> sides = configuration.getSides();

        SideConfiguration northernSide = sides.get(CubeSide.NORTHERN);
        int edgeSize = northernSide.getFace().getEdges().get(0).getSize();

        int pageWidth = 3 * MARGIN_SIZE + 3 * edgeSize;
        for (int i = 0; i < pageWidth; i++) {
            writer.print('*');
        }
        writer.println();
        writer.print("Solution #" + ++solutionsPrinted);
        writer.println();
        writer.println();

        BlockWriter blockWriter = new BlockWriter(edgeSize);

        blockWriter.writeBlocks(null, buildBlock(sides.get(CubeSide.NORTHERN),
                CubeVertex.DWN, CubeVertex.DEN, CubeVertex.UEN, CubeVertex.UWN), null);

        blockWriter.writeBlocks(
                buildBlock(sides.get(CubeSide.WESTERN), CubeVertex.DWN, CubeVertex.UWN, CubeVertex.UWS, CubeVertex.DWS),
                buildBlock(sides.get(CubeSide.UPPER), CubeVertex.UWN, CubeVertex.UEN, CubeVertex.UES, CubeVertex.UWS),
                buildBlock(sides.get(CubeSide.EASTERN), CubeVertex.UEN, CubeVertex.DEN, CubeVertex.DES, CubeVertex.UES)
        );

        blockWriter.writeBlocks(null, buildBlock(sides.get(CubeSide.SOUTHERN),
                CubeVertex.UWS, CubeVertex.UES, CubeVertex.DES, CubeVertex.DWS), null);

        blockWriter.writeBlocks(null, buildBlock(sides.get(CubeSide.BOTTOM),
                CubeVertex.DWS, CubeVertex.DES, CubeVertex.DEN, CubeVertex.DWN), null);

        writer.println();
    }

    /**
     * @param vertices Should be in the following order: upper-right, upper-left, bottom-right, bottom-left
     */
    private Block buildBlock(SideConfiguration side, CubeVertex... vertices) {

        Edge upperEdge = side.getEdge(vertices[0]);
        Edge rightEdge = side.getEdge(vertices[1]);

        // bottom and left edges are printed in reverse order
        Edge bottomEdge = side.getEdge(vertices[2]);
        Edge leftEdge = side.getEdge(vertices[3]);

        return buildBlock(side.isFlipped(), upperEdge, rightEdge, bottomEdge, leftEdge);
    }

    /**
     * @param edges Should be in the following order: upper, right, bottom, left
     */
    private Block buildBlock(boolean reverse, Edge... edges) {

        Edge upperEdge = edges[0];
        final Iterator<Byte> upperPoints = reverse? upperEdge.getPointsReverse() : upperEdge.getPoints();
        Edge rightEdge = edges[1];
        final Iterator<Byte> rightPoints = reverse? rightEdge.getPointsReverse() : rightEdge.getPoints();

        // bottom and left edges are printed in reverse order
        Edge bottomEdge = edges[2];
        final Iterator<Byte> bottomPoints = reverse? bottomEdge.getPoints() : bottomEdge.getPointsReverse();
        Edge leftEdge = edges[3];
        final Iterator<Byte> leftPoints = reverse? leftEdge.getPoints() : leftEdge.getPointsReverse();

        return new Block() {
            @Override
            Iterator<Byte> getUpperEdge() {
                return upperPoints;
            }

            @Override
            Iterator<Byte> getLeftEdge() {
                return leftPoints;
            }

            @Override
            Iterator<Byte> getRightEdge() {
                return rightPoints;
            }

            @Override
            Iterator<Byte> getBottomEdge() {
                return bottomPoints;
            }
        };
    }

    private abstract class Block {
        abstract Iterator<Byte> getUpperEdge();
        abstract Iterator<Byte> getLeftEdge();
        abstract Iterator<Byte> getRightEdge();
        abstract Iterator<Byte> getBottomEdge();
    }

    private class BlockWriter {

        private final int[] blockIndices;
        private final int LEFT_BLOCK_START = 0;
        private final int CENTER_BLOCK_START = 2;
        private final int RIGHT_BLOCK_START = 4;
        private final int edgeSize;

        private BlockWriter(int edgeSize) {

            this.edgeSize = edgeSize;

            blockIndices = new int[6];
            for (int i = 0; i < 5; i = i + 2) {
                if (i == 0) {
                    blockIndices[i] = MARGIN_SIZE;
                } else {
                    blockIndices[i] = blockIndices[i-1] + MARGIN_SIZE; // block start
                }
                blockIndices[i+1] = blockIndices[i] + edgeSize; // block end
            }
        }

        public void writeBlocks(Block leftBlock, Block centerBlock, Block rightBlock) {

            // skip first point in left and right edges,
            // because they will be printed as part of the upper edge
            if (leftBlock != null) {
                leftBlock.getLeftEdge().next();
                leftBlock.getRightEdge().next();
            }
            if (centerBlock != null) {
                centerBlock.getLeftEdge().next();
                centerBlock.getRightEdge().next();
            }
            if (rightBlock != null) {
                rightBlock.getLeftEdge().next();
                rightBlock.getRightEdge().next();
            }

            for (int line = 0; line < edgeSize; line++) {
                for (int col = 0; col < blockIndices[RIGHT_BLOCK_START+1]; col++) {
                    if (leftBlock != null && col >= blockIndices[LEFT_BLOCK_START] && col < blockIndices[LEFT_BLOCK_START+1]) {
                        writeBlockSymbol(line, col, blockIndices[LEFT_BLOCK_START], blockIndices[LEFT_BLOCK_START+1], leftBlock);
                    } else if (centerBlock != null && col >= blockIndices[CENTER_BLOCK_START] && col < blockIndices[CENTER_BLOCK_START+1]) {
                        writeBlockSymbol(line, col, blockIndices[CENTER_BLOCK_START], blockIndices[CENTER_BLOCK_START+1], centerBlock);
                    } else if (rightBlock != null && col >= blockIndices[RIGHT_BLOCK_START] && col < blockIndices[RIGHT_BLOCK_START+1]) {
                        writeBlockSymbol(line, col, blockIndices[RIGHT_BLOCK_START], blockIndices[RIGHT_BLOCK_START+1], rightBlock);
                    } else {
                        writer.write(WHITESPACE);
                    }
                }
                writer.println();
            }
            writer.println();
        }

        private void writeBlockSymbol(int line, int col, int blockStart, int blockEnd, Block block) {

            if (line == 0) {
                writer.write(getCharForPoint(block.getUpperEdge().next()));
            } else if (line >= 1 && line < edgeSize-1) {
                if (col == blockStart) {
                    writer.write(getCharForPoint(block.getLeftEdge().next()));
                } else if (col == blockEnd-1) {
                    writer.write(getCharForPoint(block.getRightEdge().next()));
                } else {
                    writer.write(PLUG);
                }
            } else if (line == edgeSize-1) {
                writer.write(getCharForPoint(block.getBottomEdge().next()));
            }
        }

        private char getCharForPoint(Byte point) {
            return point == 0? SOCKET : PLUG;
        }
    }

    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}
