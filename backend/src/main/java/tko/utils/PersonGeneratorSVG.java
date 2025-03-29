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


    public List<String> GetPersonWithChangesByIdElements(Gender genderPerson, List<Muscle> muscleList) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document documentFront;
        Document documentBack;

        if(genderPerson == Gender.FEMALE) {
            documentFront = builder.parse(sourceWomanFront);
            documentBack = builder.parse(sourceWomanBack);
        }
        else if(genderPerson == Gender.MALE) {
            documentFront = builder.parse(sourceManFront);
            documentBack = builder.parse(sourceManBack);
        }
        else {
            throw new IllegalArgumentException("Invalid gender");
        }

        documentSvgHelper(documentBack);
        documentSvgHelper(documentFront);

        changeByElementId(documentBack,documentFront,muscleList);

        String stringSVG_front = documentToString(documentFront);
        String stringSVG_back = documentToString(documentBack);
        return List.of(stringSVG_front,stringSVG_back);
    }

    private static void documentSvgHelper(Document document) {
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
    }

    private static void changeByElementId(Document documentBack,Document documentFront,List<Muscle> muscleList){
        for(Muscle muscle : muscleList){
            Element elementFront = documentFront.getElementById(muscle.getId());
            Element elementBack = documentBack.getElementById(muscle.getId()+"_b");
            if(elementFront != null){
                elementFront.setAttribute("fill", color);
            }
            if(elementBack != null){
                elementBack.setAttribute("fill", color);

            }
        }
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
        List<Muscle> list  = List.of(Muscle.CRUS,Muscle.BICEPS_FEMORIS);
        List<String> result = pg.GetPersonWithChangesByIdElements(Gender.FEMALE, list);
    }
}
