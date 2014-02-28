package reader;

import reader.count.Counter;
import reader.count.impl.AllCounter;
import reader.count.impl.FirstCounter;
import reader.count.impl.OnlyCounter;
import reader.filter.AbstractFilter;
import reader.filter.tag.TagFilter;
import reader.predicate.impl.NamespacePredicate;
import reader.predicate.impl.TagNamePredicate;
import reader.predicate.impl.TagNameSelectivePredicate;
import reader.reduce.Reducer;
import reader.reduce.impl.ContentReducer;
import reader.reduce.impl.WithChildrenReducer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class XmlTagReader extends AbstractReader<XMLStreamReader, String> {
    private static final XMLInputFactory FACTORY;
    private static final Reducer<XMLStreamReader, List<String>> DEFAULT_REDUCER =
            new ContentReducer();

    static {
        FACTORY = XMLInputFactory.newInstance();
        FACTORY.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
    }

    public XmlTagReader(String xml) {
        this(xml, new TagFilter());
    }

    public XmlTagReader(final String xml, final AbstractFilter<XMLStreamReader, Boolean> filter) {
        super(xml);
        this.elementFilter = filter;
    }

    public XmlTagReader tag(final String tagname) {
        elementFilter.addPredicate(new TagNamePredicate(tagname));
        return this;
    }

    public XmlTagReader namespace(final String namespace) {
        elementFilter.addPredicate(new NamespacePredicate(namespace));
        return this;
    }

    public XmlTagReader textContent() {
        reducer = new ContentReducer();
        return this;
    }

    public XmlTagReader withChildren() {
        reducer = new WithChildrenReducer();
        return this;
    }

    public XmlTagReader withChildren(String... tags) {
        withChildren(Arrays.asList(tags));
        return this;
    }

    public XmlTagReader withChildren(Collection<String> tags) {
        reducer = new WithChildrenReducer(new TagNameSelectivePredicate(tags));
        return this;
    }

    public String firstText() throws XMLStreamException {
        counter = new FirstCounter();
        return apply().get(0);
    }

    public List<String> all() throws XMLStreamException {
        counter = new AllCounter();
        return apply();
    }

    public List<String> only(final int count) throws XMLStreamException {
        counter = new OnlyCounter(count);
        return apply();
    }

    @Override
    protected List<String> apply() throws XMLStreamException {
        checkInternals();

        StringReader xmlReader = new StringReader(xml);
        XMLStreamReader reader = FACTORY.createXMLStreamReader(xmlReader);
        List<String> content = new LinkedList<String>();
        int elementCounter = 0;

        while (reader.hasNext() && counter.check(elementCounter)) {
            reader.next();
            if (elementFilter.filter(reader)) {
                content.addAll(reducer.reduce(reader));
                elementCounter++;
            }
        }

        xmlReader.close();

        return content;
    }

    @Override
    protected void checkInternals() {
        if (reducer == null)
            reducer = DEFAULT_REDUCER;
    }
}
