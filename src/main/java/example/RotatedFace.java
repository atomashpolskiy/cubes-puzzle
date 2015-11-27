package example;

import java.util.Arrays;

class RotatedFace implements RotatableFace {

    private Face face;
    private int rotationFactor;
    private Object[] fields = new Object[2];

    RotatedFace(Face face, int rotationFactor) {
        this.fields[0] = this.face = face;
        this.fields[1] = this.rotationFactor = rotationFactor;
    }

    public int getRotationFactor() {
        return rotationFactor;
    }

    public Face getFace() {
        return face;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fields);
    }

    @Override
    public boolean equals(Object object) {

        if (object == null || !RotatedFace.class.equals(object.getClass())) {
            return false;
        }

        RotatedFace that = (RotatedFace) object;
        return that.hasEqualFields(fields);
    }

    public boolean hasEqualFields(Object[] fields) {
        return Arrays.equals(fields, this.fields);
    }

    @Override
    public String toString() {
        return getFace().toString() + " : " + getRotationFactor();
    }
}
