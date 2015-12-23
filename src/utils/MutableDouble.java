package utils;

public class MutableDouble extends Number {
    private double value;

    /**
     * A double value that may be updated.
     *
     * @param val - the initial value
     */
    public MutableDouble(double val) {
        value = val;
    }

    /**
     * Adds the given value.
     *
     * @param val - the value to be added
     */
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
