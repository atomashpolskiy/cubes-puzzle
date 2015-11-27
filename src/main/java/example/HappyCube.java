package example;

public interface HappyCube {

    HappyCube cubeVisitor(CubeVisitor visitor);
    HappyCube uniqueSolutions();
    void solve();
}
