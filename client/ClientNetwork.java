package client;

import schema.Network.Request;
import schema.Network.Response;
import java.io.*;
import java.net.Socket;

public class ClientNetwork {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connect(String host, int port) {
        try {
            // create socket connecting to host(ip-address) and port(9090)
            socket = new Socket(host, port);
            // setup streams
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Error connecting: " + e.getMessage());
        }
    }

    public Response sendRequest(Request request) {
        try {
            // send object
            out.writeObject(request);
            out.flush();
            out.reset();
            // receive response
            return (Response) in.readObject();
        } catch (Exception e) {
            System.out.println("Error sending request: " + e.getMessage());
            if (e.getMessage().equals("Connection refused: connect")) {
                System.out.println("Server is not running. Please start the server first.");
                System.out.println("Exiting...");
                System.exit(1);
            }
            return null;
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error disconnecting: " + e.getMessage());
        }
    }
}
