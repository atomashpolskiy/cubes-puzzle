package example;

import java.util.List;

class DefaultSide implements Side {

    private List<CubeVertex> vertices;
    /**
     * The side of a cube has junctions with the occupying face;
     * each of the 4 vertices is a junction.
     * Junction of the first vertex of the face and some vertex of the side
     * is called a "junction point".
     * The junction point is a starting point for controlling
     * rotation of the face with regard to the containing cube.
     *
     * Values: 0,1,2,3 or -1 if this side is unoccupied.
     */
    private int junctionPoint = -1;
    private boolean flipped;
    private Face face;

    /**
     * @param vertices List of vertices in traversal order.
     */
    public DefaultSide(List<CubeVertex> vertices) {
        this.vertices = vertices;
    }
    
    public boolean isOccupied() {
        return getFace() != null;
    }

    protected void setFace(Face face) {
        this.face = face;
        // face is always connected to the same vertex
        // (defined by creator of DefaultEdge instance)
        this.junctionPoint = 0;
        this.flipped = false;
    }

    protected void removeFace() {
        this.face = null;
        this.junctionPoint = -1;
    }
    
    protected void rotate() {
        
        if (!isOccupied()) {
            throw new IllegalStateException("Failed to rotate: side is unoccupied");
        }
        junctionPoint = (++junctionPoint % 4);
    }

    protected void flip() {
        
        if (!isOccupied()) {
            throw new IllegalStateException("Failed to flip: side is unoccupied");
        }
        // in reality flip would also involve rotating the face by 90',
        // but we don't really care about this; by flipping the face,
        // we just indicate that it should be traversed in reverse order
        flipped = !flipped;
    }

    /**
     * @return Edge of the occupying face, that begins with the specified vertex.
     * @throws IllegalStateException if side is unoccupied.
     */
    @Override
    public Edge getEdge(CubeVertex vertex) {

        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Side does not have a vertex: " + vertex.name());
        }

        // Edge's index depends on the rotation of the face.
        // If the face is not rotated (junction point is 0), then edge's index in face's list of edges
        // is just the index of the vertex, passed as an argument.
        // If the face is rotated,
        //  then edge's index is (4 - [junction point index] + [vertex index]) % 4
        int edgeIndex = (4 - junctionPoint + vertices.indexOf(vertex)) % 4;
        if (flipped) {
            edgeIndex = Math.abs(edgeIndex - 3);
        }
        return face.getEdges().get(edgeIndex);
    }

    @Override
    public boolean hasVertex(CubeVertex vertex) {
        return vertices.contains(vertex);
    }
    
    @Override
    public List<CubeVertex> getVertices() {
        return vertices;
    }

    @Override
    public int getRotationFactor() {
        
        if (!isOccupied()) {
            throw new IllegalStateException("Failed to get rotation factor: side is unoccupied");
        }
        return junctionPoint;
    }

    @Override
    public boolean isFlipped() {
        
        if (!isOccupied()) {
            throw new IllegalStateException("Failed to get flip factor: side is unoccupied");
        }
        return flipped;
    }

    @Override
    public Face getFace() {
        return face;
    }
}
