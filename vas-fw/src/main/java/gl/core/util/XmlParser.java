package gl.core.util;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParser {
  public static Map parseSmsXML(String strXml) {
    Map<Object, Object> tagMap = null;
    
    try {
      tagMap = new HashMap<>();
      strXml = strXml.replaceAll("&", "&amp;");
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(strXml)));
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("ccg");
      
      for (int temp = 0; temp < nList.getLength(); temp++)
      {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == 1)
        {
          Element element = (Element)nNode;













        
        }













      
      }













    
    }
    catch (Exception exception) {}



    
    return tagMap;
  }
  
  public static String getTagValue(Element eElement, String tagName) {
    String temp = null;
    
    try {
      temp = eElement.getElementsByTagName(tagName).item(0).getTextContent();
    
    }
    catch (Exception exception) {}
    return temp;
  }
  
  public static int getIntTagValue(Element eElement, String tagName) {
    int value = 0;
    
    try {
      String temp = eElement.getElementsByTagName(tagName).item(0).getTextContent();
      if (temp != null)
      {
        temp = temp.trim();
        value = Integer.parseInt(temp);
      }
    
    }
    catch (Exception exception) {}
    return value;
  }
}
