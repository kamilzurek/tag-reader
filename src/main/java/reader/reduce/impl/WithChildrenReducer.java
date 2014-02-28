package reader.reduce.impl;

import reader.predicate.Predicate;
import reader.predicate.impl.TagNameAlwaysTruePredicate;
import reader.reduce.Reducer;

import javax.xml.stream.XMLStreamReader;
import java.util.LinkedList;
import java.util.List;

public class WithChildrenReducer implements Reducer<XMLStreamReader, List<String>> {
    private Predicate<XMLStreamReader> tagPredicate;
    private String start = "";

    public WithChildrenReducer(Predicate<XMLStreamReader> predicate) {
        this.tagPredicate = predicate;
    }

    public WithChildrenReducer() {
        this(new TagNameAlwaysTruePredicate());
    }

    public List<String> reduce(XMLStreamReader reader) {
        List<String> content = new LinkedList<String>();

        if (reader.isStartElement()) {
            start = reader.getLocalName();
        }

        try {
            while (reader.hasNext()) {
                appendTextContentOf(reader, content);
                if (checkIfEnd(reader))
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("invoke getCause for more info", e);
        }

        return content;
    }

    private void appendTextContentOf(XMLStreamReader reader, List<String> content) throws Exception {
        if (reader.isStartElement() && tagPredicate.apply(reader)) {
            content.addAll(new AllChildrenContentReducer().reduce(reader));
        } else if (reader.hasNext()) {
            reader.next();
        }
    }

    private boolean checkIfEnd(XMLStreamReader reader) {
        return reader.isEndElement() && reader.getLocalName().equals(start);
    }
}
