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

import reader.count.Counter;
import reader.filter.AbstractFilter;
import reader.filter.tag.TagFilter;
import reader.model.Result;
import reader.reduce.Reducer;
import reader.reduce.XmlReducer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;


/**
 * Class representing abstract reader.
 * <p/>
 * @param <E>    represents class of stream
 * @param <T>    type which reader will work on
 */
public abstract class AbstractReader<E, T> {
    protected final String xml;
    protected AbstractFilter<E, Boolean> elementFilter = null;
    protected XmlReducer<E, T> reducer = null;
    protected Counter counter = null;

    protected AbstractReader(final String xml) {
        this.xml = xml;
    }

    /**
     * Get first element from stream.
     * @return  first element
     * @throws XMLStreamException
     */
    public abstract T first() throws XMLStreamException;

    /**
     * Get only specified amount of elements.
     * @param count    sets number of returned elements
     * @return  list of results
     * @throws XMLStreamException
     */
    public abstract List<T> only(int count) throws XMLStreamException;

    /**
     * Get all appropriate elements from stream
     * @return  list of all elements
     * @throws XMLStreamException
     */
    public abstract List<T> all() throws XMLStreamException;

    protected abstract List<T> apply() throws XMLStreamException;
    protected abstract void checkInternals();
}
