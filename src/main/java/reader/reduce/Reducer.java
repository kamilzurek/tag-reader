package reader.reduce;

import reader.filter.AbstractFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pi11374
 * Date: 25.02.14
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public interface Reducer <T, E> {

    public E reduce(T obj);

}
