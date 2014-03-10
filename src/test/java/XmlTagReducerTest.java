import org.fest.assertions.api.Assertions;
import org.testng.annotations.Test;
import reader.XmlTagReader;
import reader.model.Result;
import reader.model.XmlResult;

import java.util.List;

public class XmlTagReducerTest {

    @Test
    public void givenTagAndTextContentShouldReturnEmptyListWhenTagIsNotPresentOrEmpty() throws Exception {
        String xml = "<output><result></result></output>";
        
        Result<String, String> content =  new XmlTagReader(xml)
                .tag("result")
                .textContent()
                .first();

        Assertions.assertThat(content.getAll()).isEmpty();
        Assertions.assertThat(content.get("result")).isEmpty();
    }

    @Test
    public void givenTagAndWithChildrenShouldReturnEmptyListWhenTagIsNotPresentOrEmpty() throws Exception {
        String xml = "<output></output>";
        List<Result<String, String>> content = new XmlTagReader(xml)
                .tag("result")
                .withChildren()
                .all();
        xml = "<output><result/></output>";
        List<Result<String, String>> content2 = new XmlTagReader(xml)
                .tag("result")
                .withChildren()
                .all();

        Assertions.assertThat(content).isEmpty();
        Assertions.assertThat(content2).isEmpty();
    }

    @Test
    public void givenTagAndTextContentShouldReturnOnlyOneContentFromElement() throws Exception {
        String xml = "<output><result><code>1</code><desc>Error during processing</desc></result></output>";

        List<Result<String, String>> content = new XmlTagReader(xml)
                .tag("code")
                .textContent()
                .all();

        Assertions.assertThat(content).hasSize(1);
        Assertions.assertThat(content.get(0).get("code")).isNotEmpty().isEqualTo("1");
    }

    @Test
    public void givenTagAndNamespaceAndTextContentShouldReturnOnlyOneContentFromElement() throws Exception {
        String xml = "<output><n1:result xmlns:n1=\"http://acme.com\"><n1:code>1</n1:code><n1:desc>Error during processing</n1:desc></n1:result>" +
                "<ns2:result xmlns:ns2=\"acme2.com\"><ns2:desc>Error</ns2:desc></ns2:result></output>";

        List<Result<String, String>> content = new XmlTagReader(xml)
                                .tag("desc")
                                .namespace("http://acme.com")
                                .textContent().all();

        Assertions.assertThat(content).hasSize(1);
        Assertions.assertThat(content.get(0).get("desc")).isNotEmpty().isEqualTo("Error during processing");
    }

    @Test
    public void givenTagOrTagAndNamespaceWithFirstShouldReturnExactlyOne() throws Exception {
        String xml = "<output><n1:result xmlns:n1=\"http://acme.com\"><n1:code>1</n1:code><n1:desc>Error during processing</n1:desc></n1:result></output>";

        Result<String,String> content = new XmlTagReader(xml)
                            .tag("desc")
                            .withChildren()
                            .first();
        Result<String,String> content2 = new XmlTagReader(xml)
                            .tag("code")
                            .namespace("http://acme.com")
                            .withChildren()
                            .first();

        Assertions.assertThat(content.get("desc")).isNotEmpty().isEqualTo("Error during processing");
        Assertions.assertThat(content2.get("code")).isNotEmpty().isEqualTo("1");
    }

    @Test
    public void givenTagAndTwoElementsTextContentShouldReturnEither() throws Exception {
        String xml = "<output><result><code>1</code><code>2</code><desc>Error</desc></result></output>";

        List<Result<String,String>> content = new XmlTagReader(xml)
                                    .tag("code")
                                    .textContent()
                                    .all();

        Assertions.assertThat(content).hasSize(2);
        Assertions.assertThat(content.get(0).get("code")).isNotEmpty().isEqualTo("1");
        Assertions.assertThat(content.get(1).get("code")).isNotEmpty().isEqualTo("2");
    }

    @Test
    public void givenTagAndNamespaceAndThreeElementsTextContentShouldReturnEither() throws Exception {
        String xml = "<output><result xmlns:ns1=\"acme.com\" xmlns:ns2=\"foo.com\">" +
                "<ns1:code>1</ns1:code><ns1:code>2</ns1:code><ns2:code>3</ns2:code><desc>Error</desc></result></output>";

        List<Result<String,String>> content = new XmlTagReader(xml)
                .tag("code")
                .namespace("acme.com")
                .textContent()
                .all();
        List<Result<String,String>> content2 = new XmlTagReader(xml)
                .tag("code")
                .namespace("foo.com")
                .textContent()
                .all();

        Assertions.assertThat(content).hasSize(2);
        Assertions.assertThat(content.get(0).get("code")).isNotEmpty().isEqualTo("1");
        Assertions.assertThat(content.get(1).get("code")).isNotEmpty().isEqualTo("2");
        Assertions.assertThat(content2).hasSize(1);
        Assertions.assertThat(content2.get(0).get("code")).isNotEmpty().isEqualTo("3");
    }

    @Test
     public void givenTagAndWithChildrenShouldReturnOnlyTwoContents() throws Exception {
        String xml = "<output><result><code>1</code><desc>Error during processing</desc></result>" +
                "<result><code>2</code><desc>Error during processing</desc></result></output>";

        List<Result<String, String>> content = new XmlTagReader(xml)
                                    .tag("result")
                                    .withChildren()
                                    .only(2);

        Assertions.assertThat(content).hasSize(2);
        Assertions.assertThat(content.get(0).get("code")).isEqualTo("1");
        Assertions.assertThat(content.get(0).get("desc")).isEqualTo("Error during processing");
        Assertions.assertThat(content.get(1).get("code")).isEqualTo("2");
        Assertions.assertThat(content.get(1).get("desc")).isEqualTo("Error during processing");
    }

    @Test
    public void givenTagAndWithChildrenAndOnlyThreeShouldReturnOnlyTwoContents() throws Exception {
        String xml = "<output><result><code>1</code><desc>Error during processing</desc></result>" +
                "<result><code>2</code><desc>Error during processing</desc></result>" +
                "<result><code>3</code><desc>Error during processing</desc></result></output>";

        List<Result<String, String>> content = new XmlTagReader(xml)
                                    .tag("result")
                                    .withChildren()
                                    .only(2);

        Assertions.assertThat(content).hasSize(2);
    }

    @Test
    public void givenTagAndNamespaceAndWithChildrenShouldReturnOnlyOneContents() throws Exception {
        String xml = "<output><n1:result xmlns:n1=\"http://acme.com\"><n1:code>1</n1:code><n1:desc>Error during processing</n1:desc></n1:result></output>";

        List<Result<String, String>> content = new XmlTagReader(xml)
                                    .tag("result")
                                    .namespace("http://acme.com")
                                    .withChildren()
                                    .all();

        Assertions.assertThat(content).hasSize(1);
    }
}
