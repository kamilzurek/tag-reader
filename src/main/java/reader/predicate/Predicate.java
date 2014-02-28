package reader.predicate;

/**
 * Created with IntelliJ IDEA.
 * User: pi11374
 * Date: 25.02.14
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public interface Predicate<T> {
    boolean apply(T obj);
}
