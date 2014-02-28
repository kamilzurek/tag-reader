package reader.predicate.impl;

import reader.predicate.Predicate;

import javax.xml.stream.XMLStreamReader;
import java.util.Collection;
import java.util.Collections;

public class TagNameSelectivePredicate implements Predicate<XMLStreamReader> {
    private final Collection<String> tags;

    public TagNameSelectivePredicate(Collection<String> tags) {
        this.tags = tags;
    }

    public TagNameSelectivePredicate() {
        this(Collections.EMPTY_LIST);
    }

    public boolean apply(XMLStreamReader reader) {
        return reader.isStartElement() && tags.contains(reader.getLocalName());
    }
}
