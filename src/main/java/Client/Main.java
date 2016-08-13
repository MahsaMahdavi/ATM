package Client;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by mahsa on 07/08/2016.
 */
public class Main {
    public static void main(String arg[]){
        int counter = 0;
        while (true) {
            File file = new File("terminal" + counter + ".xml");
            boolean exists = file.exists();
            if (exists) {
                Terminal terminal = new Terminal("terminal" + counter + ".xml");
                terminal.start();
                counter++;
            } else {
                break;
            }
        }
    }
}
