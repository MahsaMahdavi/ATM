package Server;

import Client.Terminal;
import Client.Transaction;
import com.sun.javafx.runtime.SystemProperties;
import exception.ClassException;
import exception.FileException;
import jdk.nashorn.internal.ir.IdentNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.Exception;

import static java.io.FileDescriptor.out;

/**
 * Created by mahsa on 06/08/2016.
 */
public class Server extends Thread {

    Socket socket;
    HashMap<String, Deposit> depositHashMap;

    public Server(Socket socket, HashMap<String, Deposit> depositHashMap) {
        this.socket = socket;
        this.depositHashMap = depositHashMap;
    }

    public void run() {
        try {
            communicate();
        } catch (ClassException e) {
            e.printStackTrace();
        }
    }


    public void communicate() throws ClassException {

        //ServerSocket serverSocket = null;
        //Socket socket;
        ObjectInputStream inputStream;
        DataOutputStream outputStream;
        //int serverPort = 2022;
        try {
            // serverSocket = new ServerSocket(serverPort);
            //while (true) {
            //   socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
            Transaction transaction = (Transaction) inputStream.readObject();
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeBytes(depositOperation(transaction));
            socket.close();
            //}
        } catch (IOException e) {
            throw new ClassException("Class not found.");
        } catch (ClassNotFoundException e) {
            throw new ClassException("Class not found.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String depositOperation(Transaction transaction) throws IOException, InterruptedException {
        String clientResponse;
        BigDecimal operationResult;
        int compare;

        Deposit depositFound = depositHashMap.get(transaction.getDepositNumber());
        RandomAccessFile file = new RandomAccessFile("outLog.txt", "rw");
        long fileLength = file.length();
        if (transaction.getType().equals("deposit")) {
            compare = (transaction.getAmount().add(depositFound.initialBalance)).compareTo(depositFound.upperBound);
            if (compare == 0 || compare == -1) {
                Thread.sleep(100);
                operationResult = depositFound.depositOpetation(transaction.getAmount());
                System.out.print(operationResult);
                Thread.sleep(100);
                clientResponse = transaction.getDepositNumber() + "#" + transaction.getType() + "," + transaction.getAmount() + ":" + operationResult + "-success";
                file.seek(fileLength);
                file.writeBytes(transaction.getTerminalId() + " " + transaction.getDepositNumber() + " " + transaction.getType() + "$" + transaction.getAmount() + "\n");

                return clientResponse;
            }
        } else if (transaction.getType().equals("withdraw")) {
            compare = transaction.getAmount().compareTo(depositFound.initialBalance);
            if (compare == 0 || compare == -1) {
                Thread.sleep(100);
                operationResult = depositFound.withdrawOperation(transaction.getAmount());

                System.out.print(operationResult);
                Thread.sleep(100);
                clientResponse = transaction.getDepositNumber() + "#" + transaction.getType() + "," + transaction.getAmount() + ":" + operationResult + "-success";
                file.seek(fileLength);
                file.writeBytes(transaction.getTerminalId() + " " + transaction.getDepositNumber() + " " + transaction.getType() + "$" + transaction.getAmount() + "\n");

                return clientResponse;
            }
        }
        System.out.print("khabid" + transaction.getDepositNumber());
        Thread.sleep(100);
        System.out.print("bidar shod" + transaction.getDepositNumber());
        file.writeBytes(transaction.getTerminalId() + " " + transaction.getDepositNumber() + " " + transaction.getType() + "$" + transaction.getAmount() + "Failed" + "\n");
        file.seek(fileLength);
        return transaction.getDepositNumber() + "#" + transaction.getType() + "," + transaction.getAmount() + ":" + "NoOperation" + "-failed";
    }

}
