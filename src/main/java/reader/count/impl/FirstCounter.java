package reader.count.impl;

import reader.count.Counter;

/**
 * Created by kamil on 25.02.14.
 */
public class FirstCounter implements Counter {

    public boolean check(int count) {
        return count < 1;
    }
}
