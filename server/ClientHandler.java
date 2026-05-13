package server;

import schema.*;
import schema.Network.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataManager dataManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User loggedInUser;

    public ClientHandler(Socket socket, DataManager dataManager) {
        this.socket = socket;
        this.dataManager = dataManager;
    }

    public void run() {
        try {
            // open input and output streams for communication
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // loop forever to read requests
            while (true) {
                // read request from client
                Request request = (Request) in.readObject();
                // if client wants to quit, break the loop
                if (request.getAction().equals("QUIT")) {
                    break;
                }
                // process the request
                Response response = handleRequest(request);
                // send response back to client
                out.writeObject(response);
                out.flush();
                out.reset();
            }
        } catch (Exception e) {
            System.out.println("excaeption happend in client handler! : " + e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        // get the action type from the request
        String action = request.getAction();

        if (action.equals("LOGIN")) {
            String[] credentials = (String[]) request.getPayload();
            // check credentials
            User user = dataManager.authenticate(credentials[0], credentials[1]);
            if (user != null) {
                // save logged in user
                loggedInUser = user;
                return new Response("SUCCESS", "Login successful", user);
            }
            return new Response("ERROR", "Wrong username or password", null);
        }

        if (action.equals("ADD_USER")) {
            // check if manager
            if (loggedInUser != null && loggedInUser.getRole().equals("MANAGER")) {
                if (dataManager.addUser((User) request.getPayload())) {
                    return new Response("SUCCESS", "User added", null);
                }
                return new Response("ERROR", "User exists", null);
            }
            return new Response("ERROR", "Unauthorized", null);
        }

        if (action.equals("GET_ALL_USERS")) {
            ArrayList<User> users = dataManager.getAllUsers();
            return new Response("SUCCESS", "Users list", users.toArray(new User[0]));
        }

        if (action.equals("CREATE_EXAM")) {
            dataManager.addExam((Exam) request.getPayload());
            return new Response("SUCCESS", "Exam created", null);
        }

        if (action.equals("GET_ALL_EXAMS")) {
            ArrayList<Exam> exams = dataManager.getAllExams();
            return new Response("SUCCESS", "Exams list", exams.toArray(new Exam[0]));
        }

        if (action.equals("SUBMIT_EXAM") || action.equals("UPLOAD_GRADE")) {
            dataManager.addGrade((Grade) request.getPayload());
            return new Response("SUCCESS", "Grade saved", null);
        }

        if (action.equals("GET_MY_GRADES")) {
            ArrayList<Grade> grades = dataManager.getStudentGrades(loggedInUser.getId());
            return new Response("SUCCESS", "My grades", grades.toArray(new Grade[0]));
        }

        if (action.equals("GET_ALL_GRADES")) {
            ArrayList<Grade> grades = dataManager.getAllGrades();
            return new Response("SUCCESS", "All grades", grades.toArray(new Grade[0]));
        }

        return new Response("ERROR", "Action not allowed", null);
    }
}
