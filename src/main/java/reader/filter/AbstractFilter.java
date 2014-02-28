package reader.filter;

import reader.predicate.Predicate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilter<T, E> {
    protected List<Predicate> predicates = new ArrayList<Predicate>();

    public abstract E filter(T obj);

    public void addPredicate(Predicate<T> predicate) {
        predicates.add(predicate);
    }
}
