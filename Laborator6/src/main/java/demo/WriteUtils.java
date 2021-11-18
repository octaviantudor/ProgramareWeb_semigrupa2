package demo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Map;

public class WriteUtils {

    private static final String CSV_SEPARATOR = ";";


    public static void writeToCSV(List<Map<String, Object>> taskMaps, String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/csv/" + fileName + ".csv"), "UTF-8"));

            for (Map<String, Object> map : taskMaps) {
                StringBuffer oneLine = new StringBuffer();
                for (Map.Entry<String, Object> e : map.entrySet()) {

                    oneLine.append(e.getValue());
                    oneLine.append(CSV_SEPARATOR);

                }
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();

        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public static void writeToXml(List<Map<String, Object>> taskMaps, String fileName) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        var doc = builder.newDocument();

        Element root = doc.createElementNS("unibuc.ro", "tasks");
        doc.appendChild(root);

        for (Map<String, Object> map : taskMaps) {
            Element task = doc.createElement("task");
            for (Map.Entry<String, Object> e : map.entrySet()) {

                if (e.getKey().equals("id"))
                    task.setAttribute(e.getKey(), e.getValue().toString());
                else {
                    task.appendChild(createUserElement(doc, e.getKey(), e.getValue().toString()));
                }
            }
            root.appendChild(task);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);

        File myFile = new File("src/main/resources/xml/" + fileName + ".xml");

        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);
    }


    private static Node createUserElement(Document doc, String name,
                                          String value) {

        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }


}
