package example;

import java.util.Iterator;

public interface Edge {

    int getSize();
    byte[] getPoints();
    byte[] getPointsReverse();
    Iterator<Byte> iterator();
    Iterator<Byte> iteratorReverse();
}
