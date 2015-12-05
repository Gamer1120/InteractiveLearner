package utils;

public class MutableLogarithmicDouble {
    double value;

    public MutableLogarithmicDouble(double val) {
        value = Math.log(val);
    }

    public void add(double val) {
        value += Math.log(val);
    }

    public double toDouble() {
        return value;
    }
}
