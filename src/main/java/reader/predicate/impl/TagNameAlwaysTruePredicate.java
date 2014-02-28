package reader.predicate.impl;

import reader.predicate.Predicate;

import javax.xml.stream.XMLStreamReader;

public class TagNameAlwaysTruePredicate implements Predicate<XMLStreamReader> {
    public boolean apply(XMLStreamReader obj) {
        return true;
    }
}
