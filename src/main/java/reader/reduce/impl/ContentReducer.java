package reader.reduce.impl;

import reader.reduce.Reducer;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContentReducer implements Reducer<XMLStreamReader, List<String>> {
    public List<String> reduce(XMLStreamReader reader) {
        try {
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement()) {
                    break;
                }
                if (!reader.isWhiteSpace()
                        && reader.getEventType() == XMLStreamConstants.CHARACTERS) {
                    return Arrays.asList(reader.getText());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("invoke getCause for more info", e);
        }

        return Collections.emptyList();
    }
}
