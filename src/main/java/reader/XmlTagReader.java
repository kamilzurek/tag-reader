/*
* Copyright 2014 - Kamil Zurek
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package reader;

import reader.count.impl.AllCounter;
import reader.count.impl.FirstCounter;
import reader.count.impl.OnlyCounter;
import reader.filter.AbstractFilter;
import reader.filter.tag.TagFilter;
import reader.model.Result;
import reader.model.XmlResult;
import reader.predicate.impl.NamespacePredicate;
import reader.predicate.impl.TagNamePredicate;
import reader.predicate.impl.TagNameSelectivePredicate;
import reader.reduce.XmlReducer;
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

/**
 * Extending {@link AbstractReader} with fluent interface.
 * <p/>
 * Below is example for usage:<br/>
 * <pre>
 *     new XmlTagReader().tag("xmlTagForSearch").namespace("withOptionalNamespace")
 *                          .witchChildren("onlyThischildNode", "andThisNode")
 *                          .first()
 * </pre>
 */

public class XmlTagReader extends AbstractReader<XMLStreamReader, Result<String, String>> {
    private static final XMLInputFactory FACTORY;
    private static final XmlReducer<XMLStreamReader, Result<String, String>> DEFAULT_REDUCER =
            new ContentReducer();

    private boolean toXml;

    static {
        FACTORY = XMLInputFactory.newInstance();
        FACTORY.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
    }

    public XmlTagReader(final String xml) {
        this(xml, new TagFilter());
    }

    public XmlTagReader(final String xml, final AbstractFilter<XMLStreamReader, Boolean> filter) {
        super(xml);
        this.elementFilter = filter;
        this.toXml = false;
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

    public XmlTagReader addXmlTag() {
        toXml = true;
        return this;
    }

    public Result<String, String> first() throws XMLStreamException {
        counter = new FirstCounter();
        List<Result<String, String>> content = apply();
        return (content.isEmpty()) ? XmlResult.EMPTY_RESULT : content.get(0);
    }

    public List<Result<String, String>> all() throws XMLStreamException {
        counter = new AllCounter();
        return apply();
    }

    public List<Result<String, String>> only(final int count) throws XMLStreamException {
        counter = new OnlyCounter(count);
        List<Result<String, String>> content = apply();
        return (content.size() <= count) ? content : content.subList(0, count);
    }

    @Override
    protected List<Result<String, String>> apply() throws XMLStreamException {
        checkInternals();
        reducer.setToXml(toXml);

        StringReader xmlReader = new StringReader(xml);
        XMLStreamReader reader = FACTORY.createXMLStreamReader(xmlReader);
        List<Result<String, String>> content = new LinkedList<Result<String, String>>();
        int elementCounter = 0;

        while (reader.hasNext() && counter.check(elementCounter)) {
            reader.next();
            if (elementFilter.filter(reader)) {
                Result<String, String> result = reducer.reduce(reader);
                if (!result.getAll().isEmpty()) {
                    content.add(result);
                    elementCounter++;
                }
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
