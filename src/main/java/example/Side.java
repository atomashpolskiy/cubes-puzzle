package example;

import java.util.List;

/**
 * Side of a cube. Can be occupied by a face.
 */
public interface Side {

    int getRotationFactor();
    boolean isFlipped();
    Face getFace();
    Edge getEdge(CubeVertex vertex);
    boolean hasVertex(CubeVertex vertex);
    List<CubeVertex> getVertices();
}
