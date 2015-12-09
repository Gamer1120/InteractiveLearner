package utils;

public class MutableDouble extends Number {
    double value;

    public MutableDouble(double val) {
        value = val;
    }

    public void add(double val) {
        value += val;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public float floatValue() {
        return (float) value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public double doubleValue() {
        return value;
    }
}
