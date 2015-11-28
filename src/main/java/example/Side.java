package example;

/**
 * Side of a cube. Can be occupied by a face.
 */
public interface Side extends RotatableFace {

    Edge getEdge(CubeVertex vertex);
    boolean hasVertex(CubeVertex vertex);
}
