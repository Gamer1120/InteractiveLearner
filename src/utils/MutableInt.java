package utils;

public class MutableInt {
    int value;

    public MutableInt() {
        this(1);
    }

    public MutableInt(int val) {
        value = val;
    }

    public void increment() {
        ++value;
    }

    public int toInt() {
        return value;
    }
}
