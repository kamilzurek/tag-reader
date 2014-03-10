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
import reader.reduce.Reducer;
import reader.reduce.XmlReducer;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContentReducer implements XmlReducer<XMLStreamReader, Result<String, String>> {

    public Result<String, String> reduce(XMLStreamReader reader) {
        try {
            if (!reader.isStartElement()) {
                return XmlResult.EMPTY_RESULT;
            }

            String startTag = reader.getLocalName();
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement()) {
                    break;
                }
                if (!reader.isWhiteSpace()
                        && reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                    return getContent(startTag, reader.getText());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("invoke getCause for more info", e);
        }

        return XmlResult.EMPTY_RESULT;
    }

    public Reducer<XMLStreamReader, Result<String, String>> setToXml(boolean toXml) {
        return this;
    }

    private Result<String, String> getContent(final String startTag, final String text) {
        XmlResult result = new XmlResult(1);
        result.add(startTag, text);
        return result;
    }
}
