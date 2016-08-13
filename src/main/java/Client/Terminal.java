package Client;

import Server.Deposit;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import exception.FileException;
import exception.UnknownHost;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mahsa on 07/08/2016.
 */
public class Terminal extends Thread {

    String fileName;

    public Terminal(String fileName) {
        this.fileName = fileName;
    }


    public void run() {
        try {
            parsXml(fileName);
        } catch (FileException e) {
            e.getMessage();
        }
    }

    public void parsXml(String fileName) throws FileException {
        String id;
        String type;
        BigDecimal amount;
        String deposit;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(fileName);
        } catch (ParserConfigurationException e) {
            throw new FileException("Xml File not found");
        } catch (SAXException e) {
            throw new FileException("Xml File not found");
        } catch (IOException e) {
            throw new FileException("Xml File not found");
        }
        NodeList nList = document.getElementsByTagName("transaction");
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            id = node.getAttributes().getNamedItem("id").getNodeValue();
            type = node.getAttributes().getNamedItem("type").getNodeValue();
            amount = new BigDecimal(node.getAttributes().getNamedItem("amount").getNodeValue());
            deposit = node.getAttributes().getNamedItem("deposit").getNodeValue();
            try {
                communicate(new Transaction(id, type, amount, deposit, fileName));
            } catch (UnknownHost unknownHost) {
                unknownHost.getMessage();
            }
        }
    }

    public void communicate(Transaction transaction) throws UnknownHost {
        Socket clientSocket;
        BufferedReader inFromServer;
        ObjectOutputStream outputStream;
        String serverSentence;
        try {
            clientSocket = new Socket("localhost", 2022);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(transaction);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverSentence = inFromServer.readLine();
            writeFile(serverSentence);
            clientSocket.close();
        } catch (UnknownHostException e) {
            throw new UnknownHost("Unknown Host");
        } catch (IOException e) {
            throw new UnknownHost("connection refuse");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public void writeFile(String sentence) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        File file = new File("response" + fileName.substring(8, 9) + ".xml");
        boolean exists = file.exists();
        if (exists) {
            Document document = documentBuilder.parse("response" + fileName.substring(8, 9) + ".xml");
            Element root = document.getDocumentElement();
            Collection<String> depositCollection = new ArrayList<String>();
            depositCollection.add(sentence);

            for (String sentenceString : depositCollection) {
                // server elements
                Element newTransaction = document.createElement("transaction");

                String[] splitNumber = sentenceString.split("#");
                Element depositNumber = document.createElement("depositNumber");
                depositNumber.appendChild(document.createTextNode(splitNumber[0]));
                newTransaction.appendChild(depositNumber);

                Element requestType = document.createElement("type");
                requestType.appendChild(document.createTextNode(splitNumber[1].split(",")[0]));
                newTransaction.appendChild(requestType);

                Element amount = document.createElement("amount");
                amount.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[0]));
                newTransaction.appendChild(amount);

                Element initialBalance = document.createElement("initialBalance");
                initialBalance.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[1].split("-")[0]));
                newTransaction.appendChild(initialBalance);

                Element result = document.createElement("result");
                result.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[1].split("-")[1]));
                newTransaction.appendChild(result);
                root.appendChild(newTransaction);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;

            try

            {
                transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult("response" + fileName.substring(8, 9) + ".xml");
                transformer.transform(source, result);
            } catch (
                    TransformerConfigurationException e)

            {
                e.printStackTrace();
            } catch (
                    TransformerException e)

            {
                e.printStackTrace();
            }


            System.out.println("File saved!");

        } else {
            // root elements
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("transaction");
            document.appendChild(rootElement);

            String[] splitNumber = sentence.split("#");
            Element depositNumber = document.createElement("depositNumber");
            depositNumber.appendChild(document.createTextNode(splitNumber[0]));
            rootElement.appendChild(depositNumber);

            Element requestType = document.createElement("type");
            requestType.appendChild(document.createTextNode(splitNumber[1].split(",")[0]));
            rootElement.appendChild(requestType);

            Element amount = document.createElement("amount");
            amount.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[0]));
            rootElement.appendChild(amount);

            Element initialBalance = document.createElement("initialBalance");
            initialBalance.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[1].split("-")[0]));
            rootElement.appendChild(initialBalance);

            Element result = document.createElement("result");
            result.appendChild(document.createTextNode(splitNumber[1].split(",")[1].split(":")[1].split("-")[1]));
            rootElement.appendChild(result);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            StreamResult resul = new StreamResult(new File("response" + fileName.substring(8, 9) + ".xml"));
            transformer.transform(source, resul);
            System.out.println("File saved!");

        }
    }
}




