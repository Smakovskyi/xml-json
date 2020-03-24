package kpi.ua;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpi.ua.generated.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class DomRunner {
    public static void main(String[] args) throws Exception {
        InputStream in = DomRunner.class.getResourceAsStream("/session.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse( in );
        Element root = document.getDocumentElement();

        NodeList rootChilds =  root.getChildNodes();
        for( int i = 0 ; i<rootChilds.getLength() ; i++){
            Node node = rootChilds.item(i);
            //System.out.println(node);
            if( node.getNodeType() == Node.ELEMENT_NODE ){
                Element el = (Element) node;
                String name = el.getNodeName();
                if("exams".equals(name)){
                    parseExams(el);
                }
            }
        }
        System.out.println("===========JAXB=====================");
        JAXBContext context = JAXBContext.newInstance( Session.class );
        Unmarshaller unm = context.createUnmarshaller();
        Session session = (Session) unm.unmarshal(DomRunner.class.getResourceAsStream("/session.xml") );
        System.out.println( session );

        System.out.println("===========XPATH===================");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        XPathExpression expr = xpath.compile("/session/exams/exam[1]/date");
        String result = (String) expr.evaluate(document , XPathConstants.STRING);
        System.out.println(result);
        System.out.println("==============JSON===============");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println( objectMapper.writeValueAsString(session));




    }

    private static void parseExams(Element exams) {
        NodeList examsChilds = exams.getChildNodes();
        for(int i=0; i<examsChilds.getLength();i++){
            Node node = examsChilds.item(i);
            if( node.getNodeType() == Node.ELEMENT_NODE){
                Element exam = (Element) node;
                String examName = exam.getAttribute("name");
                System.out.println(examName);
            }
        }

    }
}
