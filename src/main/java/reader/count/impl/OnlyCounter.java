package reader.count.impl;

import reader.count.Counter;

/**
 * Created by kamil on 25.02.14.
 */
public class OnlyCounter implements Counter {
    private final int size;

    public OnlyCounter(final int size) {
        this.size = size;
    }

    public boolean check(int count) {
        return count < size;
    }
}
