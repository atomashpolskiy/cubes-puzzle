package example;

/**
 * Side of a cube. Can be occupied by a face.
 */
public interface Side {

    boolean isOccupied();
    void setFace(Face face);
    Face getFace();
    void removeFace();
    Edge getEdge(CubeVertex vertex);
}
