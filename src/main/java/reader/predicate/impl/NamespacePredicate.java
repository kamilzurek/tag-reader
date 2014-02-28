package reader.predicate.impl;

import reader.predicate.Predicate;

import javax.xml.stream.XMLStreamReader;

public class NamespacePredicate implements Predicate<XMLStreamReader> {
    private final String namespace;

    public NamespacePredicate() {
        this("");
    }

    public NamespacePredicate(final String namespace) {
        this.namespace = namespace;
    }

    public boolean apply(XMLStreamReader obj) {
        if (!obj.isStartElement())
            return false;

        if (obj.getNamespaceURI() == null && "".equals(namespace))
            return true;

        return namespace.equals(obj.getNamespaceURI());
    }
}
