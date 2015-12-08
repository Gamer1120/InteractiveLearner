package utils;

public class MutableInt {
    int value;

    public MutableInt(int val) {
        value = val;
    }

    public void add(int val) {
        value += val;
    }

    public int toInt() {
        return value;
    }
}
