package Server;

import Client.Transaction;
import exception.ClassException;
import exception.FileException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mahsa on 07/08/2016.
 */
public class Main {

    static String portNumber;
    static JSONArray deposits;
    static String customer;
    static String id;
    static String initialBalance;
    static String upperBound;
    static HashMap<String, Deposit> depositHashMap;

    public static void main(String arg[]) throws IOException, FileException {
        readJsonFile();
        int serverPort = 2022;
        ServerSocket serverSocket = new ServerSocket(serverPort);
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            Server server = new Server(socket, depositHashMap);
            server.start();
        }
    }

    public static void readJsonFile() throws FileException {
        try {
            FileReader reader = new FileReader("core.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            portNumber = (String) jsonObject.get("port");
            deposits = (JSONArray) jsonObject.get("deposits");
            addToMap();
        } catch (ParseException e1) {
            throw new FileException("JsonFile not found!");
        } catch (FileNotFoundException e1) {
            throw new FileException("JsonFile not found!");
        } catch (IOException e1) {
            throw new FileException("JsonFile not found!");
        }
    }

    public static void addToMap() {
        Iterator iterator = deposits.iterator();
        depositHashMap = new HashMap<String, Deposit>();
        while (iterator.hasNext()) {
            JSONObject innerObject = (JSONObject) iterator.next();
            customer = (String) innerObject.get("customer");
            id = (String) innerObject.get("id");
            initialBalance = (String) innerObject.get("initialBalance");
            upperBound = (String) innerObject.get("upperBound");
            depositHashMap.put(id, new Deposit(customer, id, new BigDecimal(initialBalance), new BigDecimal(upperBound)));
        }
    }
}
