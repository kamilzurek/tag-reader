package reader.filter.tag;

import reader.filter.AbstractFilter;
import reader.predicate.Predicate;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;

public class TagFilter extends AbstractFilter<XMLStreamReader, Boolean> {

    public Boolean filter(XMLStreamReader reader) {
        boolean passAllPredicates = true;

        if (predicates.size() == 0)
            return Boolean.FALSE;

        for (Predicate<XMLStreamReader> predicate : predicates) {
            passAllPredicates = passAllPredicates && predicate.apply(reader);
            if (!passAllPredicates)
                return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
