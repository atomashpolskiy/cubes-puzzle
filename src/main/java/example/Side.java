package example;

/**
 * Side of a cube. Can be occupied by a face.
 */
public interface Side extends RotatableFace {

    boolean isOccupied();
    void setFace(Face face);
    void removeFace();
    Edge getEdge(CubeVertex vertex);
    boolean hasVertex(CubeVertex vertex);
}
