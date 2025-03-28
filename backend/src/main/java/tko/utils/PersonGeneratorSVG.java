package tko.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

public class PersonGeneratorSVG {

    private static final String color = "#FF0000";
    private static final String sourceWomanBack  = "src/main/resources/peopleSVG/woman_back.svg";
    private static final String sourceWomanFront  = "src/main/resources/peopleSVG/woman_front.svg";

    private static final String sourceManBack  = "src/main/resources/peopleSVG/man_back.svg";
    private static final String sourceManFront  = "src/main/resources/peopleSVG/man_front.svg";


    public String GetPersonWithChangesByIdElements(Gender genderPerson, List<Muscle> muscleList) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document documentFront;
        Document documentBack;

        if(genderPerson == Gender.FEMALE) {
            documentFront = builder.parse(sourceWomanFront);
            documentBack = builder.parse(sourceWomanBack);
        }
        if(genderPerson == Gender.MALE) {
            documentFront = builder.parse(sourceManFront);
            documentBack = builder.parse(sourceManBack);
        }








        String res = documentToString(document);



        return "";
    }

    public static Document readSvgFromInputStream(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.parse(inputStream);
    }

    private static Document documentSvgHelper(Document document) {
        NodeList nodes = document.getElementsByTagName("*");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.hasAttribute("id")) {
                    element.setIdAttribute("id", true);
                }
            }
        }
        return document;
    }

    public static String documentToString(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(document);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        transformer.transform(source,result);

        return writer.toString();
    }


    public static void main(String[] args) throws Exception {
        PersonGeneratorSVG pg = new PersonGeneratorSVG();
        pg.ChangePersonElements();
    }


}
