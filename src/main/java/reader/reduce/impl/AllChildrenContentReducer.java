package reader.reduce.impl;

import reader.reduce.Reducer;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamil on 26.02.14.
 */
public class AllChildrenContentReducer implements Reducer<XMLStreamReader, List<String>>{
    private List<String> texts = new LinkedList<String>();

    public List<String> reduce(XMLStreamReader reader) {

        try {
            appendTextContentOf(reader);
        } catch (Exception e) {
            throw new RuntimeException("invoke getCause for more info", e);
        }

        return texts;
    }

    private void appendTextContentOf(XMLStreamReader reader) throws Exception {
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
                    appendTextContentOf(reader);
                    onlyText = false;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals(startTag)) {
                        if (onlyText)
                            texts.add(text);
                        return;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    text = reader.getText();
                    break;
            }
        }
    }
}
