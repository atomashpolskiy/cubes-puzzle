package example;

import java.util.Arrays;

// TODO: detect symm
class RotatedFace implements RotatableFace {

    private Face face;
    private int rotationFactor;
    private boolean flipped;
    private Object[] fields = new Object[3];

    RotatedFace(Face face, int rotationFactor, boolean flipped) {
        this.fields[0] = this.face = face;
        this.fields[1] = this.rotationFactor = rotationFactor;
        this.fields[2] = this.flipped = flipped;
    }

    public int getRotationFactor() {
        return rotationFactor;
    }

    @Override
    public boolean isFlipped() {
        return flipped;
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
        return (flipped? "*" : "") + getFace().toString() + (flipped? "* : " : " : ") + getRotationFactor();
    }
}
