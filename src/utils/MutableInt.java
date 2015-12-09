package utils;

public class MutableInt extends Number {
    int value;

    public MutableInt(int val) {
        value = val;
    }

    public void add(int val) {
        value += val;
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
