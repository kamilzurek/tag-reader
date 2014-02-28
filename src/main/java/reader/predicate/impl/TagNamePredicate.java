package reader.predicate.impl;

import reader.predicate.Predicate;

import javax.xml.stream.XMLStreamReader;

public class TagNamePredicate implements Predicate<XMLStreamReader> {
    private final String tagname;

    public TagNamePredicate() {
        this("");
    }

    public TagNamePredicate(final String tagname) {
        this.tagname = tagname;
    }

    public boolean apply(XMLStreamReader obj) {
        return obj.isStartElement() && tagname.equals(obj.getLocalName());
    }
}
