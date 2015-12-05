package utils;

public class MutableDouble {
    double value;

    public MutableDouble(double val) {
        value = val;
    }

    public void add(double val) {
        value += val;
    }

    public double toDouble() {
        return value;
    }
}
