package utils;

public class MutableInt extends Number {
    private int value;

    /**
     * An int value that may be updated.
     * The default initial value is zero.
     */
    public MutableInt() {
        this(0);
    }

    /**
     * An int value that may be updated.
     *
     * @param val - the initial value
     */
    public MutableInt(int val) {
        value = val;
    }

    /**
     * Adds the given value.
     *
     * @param val - the value to be added
     */
    public void add(int val) {
        value += val;
    }

    /**
     * @inheritDoc
     */
    @Override
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public short shortValue() {
        return (short) value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int intValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public double doubleValue() {
        return value;
    }
}
