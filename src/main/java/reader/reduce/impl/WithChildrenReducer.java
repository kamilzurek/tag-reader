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

package reader.reduce.impl;

import reader.model.Result;
import reader.model.XmlResult;
import reader.predicate.Predicate;
import reader.predicate.impl.TagNameAlwaysTruePredicate;
import reader.reduce.Reducer;
import reader.reduce.XmlReducer;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Class reduce XML elements from stream to {@link Result} object, with
 * all child nodes.
 */

public class WithChildrenReducer implements XmlReducer<XMLStreamReader, Result<String, String>> {
    private Predicate<XMLStreamReader> tagPredicate;
    private String start;
    private boolean toXml;

    public WithChildrenReducer(Predicate<XMLStreamReader> predicate) {
        this.tagPredicate = predicate;
        this.start = "";
    }

    public WithChildrenReducer() {
        this(new TagNameAlwaysTruePredicate());
    }

    public Result<String, String> reduce(XMLStreamReader reader) {
        Result<String, String> content = new XmlResult();

        if (reader.isStartElement()) {
            start = reader.getLocalName();
        }

        try {
            while (reader.hasNext()) {
                if (reader.isStartElement() && tagPredicate.apply(reader)) {
                    appendTextContentOf(reader, content);
                } else if (reader.hasNext()) {
                    reader.next();
                }
                if (checkIfEnd(reader))
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("invoke getCause for more info", e);
        }

        return content;
    }

    public XmlReducer<XMLStreamReader, Result<String, String>> setToXml(boolean toXml) {
        this.toXml = toXml;
        return this;
    }

    private void appendTextContentOf(XMLStreamReader reader, Result<String, String> results) throws Exception {
        String startTag = "";
        String text = "";
        boolean onlyText = true;

        if (reader.isStartElement()) {
            startTag = reader.getLocalName();
        }

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    appendTextContentOf(reader, results);
                    onlyText = false;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (checkIfSeekingElementIsEmpty(reader, text)) {
                        return;
                    }

                    if (reader.getLocalName().equals(startTag)) {
                        if (onlyText) {
                            if (toXml)
                                results.add(startTag, xml(startTag, text));
                            else
                                results.add(startTag, text);
                        }
                        return;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    text = reader.getText();
                    break;
            }
        }
    }

    private String xml(final String tag, final String content) {
        return new StringBuilder().append("<").append(tag).append(">")
                .append(content)
                .append("</").append(tag).append(">").toString();
    }

    private boolean checkIfEnd(XMLStreamReader reader) {
        return reader.isEndElement() && reader.getLocalName().equals(start);
    }

    private boolean checkIfSeekingElementIsEmpty(XMLStreamReader reader, String text) {
        return checkIfEnd(reader) && text.equals("");
    }
}
