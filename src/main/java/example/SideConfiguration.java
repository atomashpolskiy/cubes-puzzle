package example;

import java.util.Arrays;
import java.util.List;

class SideConfiguration implements Side {

    private Side side;
    private Object[] fields = new Object[3];

    SideConfiguration(List<CubeVertex> vertices, Face face, int rotationFactor, boolean flipped) {

        this.side = buildSide(vertices, face, rotationFactor, flipped);
        this.fields[0] = face;
        this.fields[1] = rotationFactor;
        this.fields[2] = flipped;
    }

    private static Side buildSide(List<CubeVertex> vertices, Face face, int rotationFactor, boolean flipped) {

        DefaultSide side = new DefaultSide(vertices);
        side.setFace(face);
        for (int i = 0; i < rotationFactor; i++) {
            side.rotate();
        }
        if (flipped) {
            side.flip();
        }
        return side;
    }

    public int getRotationFactor() {
        return side.getRotationFactor();
    }

    public boolean isFlipped() {
        return side.isFlipped();
    }

    public Face getFace() {
        return side.getFace();
    }

    @Override
    public Edge getEdge(CubeVertex vertex) {
        return side.getEdge(vertex);
    }

    @Override
    public boolean hasVertex(CubeVertex vertex) {
        return side.hasVertex(vertex);
    }

    @Override
    public List<CubeVertex> getVertices() {
        return side.getVertices();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fields);
    }

    @Override
    public boolean equals(Object object) {

        if (object == null || !SideConfiguration.class.equals(object.getClass())) {
            return false;
        }

        SideConfiguration that = (SideConfiguration) object;
        return that.hasEqualFields(fields);
    }

    public boolean hasEqualFields(Object[] fields) {
        return Arrays.equals(fields, this.fields);
    }

    @Override
    public String toString() {
        return (side.isFlipped()? "*" : "") + getFace().toString() +
                (side.isFlipped()? "* : " : " : ") + getRotationFactor();
    }
}
