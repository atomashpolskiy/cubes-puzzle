package example;

import java.util.Iterator;

public interface Edge {

    int getSize();
    byte[] asArray();
    Iterator<Byte> getPoints();
    Iterator<Byte> getPointsReverse();
}
