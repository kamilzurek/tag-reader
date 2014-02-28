package reader;

import reader.count.Counter;
import reader.filter.AbstractFilter;
import reader.filter.tag.TagFilter;
import reader.reduce.Reducer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

public abstract class AbstractReader<E, T> {
    protected final String xml;
    protected AbstractFilter<E, Boolean> elementFilter = null;
    protected Reducer<E, List<T>> reducer = null;
    protected Counter counter = null;

    protected AbstractReader(final String xml) {
        this.xml = xml;
    }

    public abstract T firstText() throws XMLStreamException;
    public abstract List<T> only(int count) throws XMLStreamException;
    public abstract List<T> all() throws XMLStreamException;

    protected abstract List<T> apply() throws XMLStreamException;
    protected abstract void checkInternals();
}
