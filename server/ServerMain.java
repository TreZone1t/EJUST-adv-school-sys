package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Starting School Managementing system Server...");
        // initial the data manager to load the data from the files
        DataManager dataManager = new DataManager();

        try {
            // create a server socket to listen on port 9090
            ServerSocket serverSocket = new ServerSocket(9090);
            // create a scanner to read the input from the console from the IT
            Scanner sc = new Scanner(System.in);
            System.out.println("Server is listening on port 9090");

            while (true) {
                // accept the client connection
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");
                // create a client handler to handle the client connection
                ClientHandler handler = new ClientHandler(socket, dataManager);
                handler.start();
                // if the IT types "exit" the server will close
                if (sc.nextLine().equals("exit")) {
                    break;
                }
            }
            // close the server socket and the scanner
            serverSocket.close();
            sc.close();
        } catch (Exception e) {
            // print any exceptions that may happend
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
