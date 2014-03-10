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

package reader.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstraction of result objects, returned from transformed stream.
 */
public class XmlResult implements Result<String, String>{
    public static final EmptyResult EMPTY_RESULT = new EmptyResult();
    protected static final String EMPTY_VALUE = "";
    protected Map<String, String> xmlContent;

    public XmlResult() {
        this(16);
    }

    public XmlResult(final int amountOfElements) {
        xmlContent = new HashMap<String, String>(amountOfElements);
    }

    /**
     * Get node element.
     * @param key    tag name
     * @return  text content from tag; empty value if tag not found
     */
    public String get(String key) {
        String value = xmlContent.get(key);
        return (value == null) ? EMPTY_VALUE : value;
    }

    /**
     * Add tag with given name and value
     * @param key      name of tag
     * @param value    text content
     */
    public void add(String key, String value) {
        if (key != null)
            xmlContent.put(key, value);
    }

    /**
     * Add all tags witch content from another Result object.
     * @param result    Result object from which tags will be copied
     * @return  current Result object
     */
    public Result<String, String> addAll(Result<String, String> result) {
        Collection<String> keys = result.getAll();
        for (String key : keys)
            xmlContent.put(key, result.get(key));

        return this;
    }

    /**
     * Get all tags from current Result
     * @return  collection of tags
     */
    public Collection<String> getAll() {
        return xmlContent.keySet();
    }

    /* Empty Result */
    protected static class EmptyResult implements Result<String, String> {
        public String get(String key) {
            return EMPTY_VALUE;
        }

        public void add(String key, String value) {
            // do nothing
        }

        public Result<String, String> addAll(Result<String, String> result) {
            // do nothing
            return this;
        }

        public Collection<String> getAll() {
            return Collections.EMPTY_LIST;
        }
    }
}
