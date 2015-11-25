package example;

import java.util.Iterator;

public interface Edge {

    byte[] asArray();
    Iterator<Byte> getPoints();
    Iterator<Byte> getPointsReverse();
}
