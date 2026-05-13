package client;

import schema.*;
import schema.Network.*;
import java.io.File;
import java.util.Scanner;
public class MainUI {
    private ClientNetwork network;
    private Scanner scanner;
    private User loggedInUser;

    public MainUI() {
        network = new ClientNetwork();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Connecting to server...");
        // connect to the local server at port 9090
        network.connect("localhost", 9090);
        System.out.println("Connected!");

        // infinite loop to show menus
        while (true) {
            if (loggedInUser == null) {
                // if not logged in, show login menu
                showLoginMenu();
            } else {
              int choice = loggedInUser.showMenu();
              switch (choice) {
                case 0: 
                    network.sendRequest(new Request("LOGOUT", null));
                    loggedInUser = null;
                    break;
                case 11: addUser(); break;
                case 12: viewAllUsers(); break;
                case 13: editGrade(); break;
                case 21: createExam(); break;
                case 22: viewAllExams(); break;
                case 23: uploadExamFromFile(); break;
                case 24: viewAllGrades(); break;
                case 31: viewAllExams(); break;
                case 32: takeExam(); break;
                case 33: viewMyGrades(); break;
                default: System.out.println("Invalid choice."); break;
              }
            }
        
    }
    }
    private void showLoginMenu() {
        System.out.println("\n<----Login---->\n");
        System.out.print("Username (type 'exit' to quit): ");
        String username = scanner.nextLine();
        // if the user typed exit, quit the program
        if (username.equalsIgnoreCase("exit")) {
            network.sendRequest(new Request("QUIT", null));
            System.out.println("Goodbye!");
            System.exit(0);
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // send login request
        try {
            String[] credentials = { username, password };
            Request req = new Request("LOGIN", credentials);
            Response res = network.sendRequest(req);

            if (res != null && res.getStatus().equals("SUCCESS")) {
                loggedInUser = (User) res.getPayload();
                System.out.println("Login successful. Welcome, " + loggedInUser.getName());
            } else {
                throw new Exception("Login failed: " + res.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void editGrade() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Exam/Task ID: ");
        String examId = scanner.nextLine();
        System.out.print("New Score: ");
        double score = Double.parseDouble(scanner.nextLine());
        System.out.print("New Max Score: ");
        double maxScore = Double.parseDouble(scanner.nextLine());
        System.out.print("Type (EXAM/CLASSWORK): ");
        String type = scanner.nextLine().toUpperCase();
        // create new grade object but we need to check the data first if valid or not
        // [to-do].
        Grade grade = new Grade(studentId, examId, score, maxScore, type);
        Request req = new Request("UPLOAD_GRADE", grade);
        Response res = network.sendRequest(req);
        System.out.println(res.getMessage());
    }

    private void addUser() {
        System.out.print("Role (MANAGER/TEACHER/STUDENT): ");
        String role = scanner.nextLine().toUpperCase();

        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();

        User newUser;
        if (role.equals("MANAGER")) {
            newUser = new Manager(id, username, password, name);
        } else if (role.equals("TEACHER")) {
            newUser = new Teacher(id, username, password, name);
        } else {
            newUser = new Student(id, username, password, name);
        }
        
        Request req = new Request("ADD_USER", newUser);
        Response res = network.sendRequest(req);
        System.out.println(res.getMessage());
    }

    private void viewAllUsers() {
        Request req = new Request("GET_ALL_USERS", null);
        Response res = network.sendRequest(req);
        if (res.getStatus().equals("SUCCESS")) {
            User[] users = (User[]) res.getPayload();
            for (User u : users) {
                System.out.println(u.toCsv());
            }
        }
    }

    private void uploadExamFromFile() {
        System.out.print("Exam ID: ");
        String id = scanner.nextLine();
        System.out.print("Exam Title: ");
        String title = scanner.nextLine();
        System.out.print("File Name (e.g., quiz_sample.txt): ");
        String fileName = scanner.nextLine();

        try {
            // open file
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);
            Exam exam = new Exam(id, loggedInUser.getId(), title);

            // read lines
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // split by pipe character
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    exam.addQuestion(parts[0], parts[1]);
                }
            }
            // send create request
            Request req = new Request("CREATE_EXAM", exam);
            Response res = network.sendRequest(req);
            System.out.println(res.getMessage());
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }

    private void createExam() {
        System.out.print("Exam ID: ");
        String id = scanner.nextLine();
        System.out.print("Exam Title: ");
        String title = scanner.nextLine();

        Exam exam = new Exam(id, loggedInUser.getId(), title);

        System.out.println("Enter questions (type 'done' to finish):");
        while (true) {
            System.out.print("Question: ");
            String q = scanner.nextLine();
            if (q.equalsIgnoreCase("done"))
                break;
            System.out.print("Answer: ");
            String a = scanner.nextLine();
            exam.addQuestion(q, a);
        }

        Request req = new Request("CREATE_EXAM", exam);
        Response res = network.sendRequest(req);
        System.out.println(res.getMessage());
    }

    private void viewAllExams() {
        Request req = new Request("GET_ALL_EXAMS", null);
        Response res = network.sendRequest(req);
        if (res.getStatus().equals("SUCCESS")) {
            Exam[] exams = (Exam[]) res.getPayload();
            for (Exam e : exams) {
                System.out.println("ID: " + e.getId() + " | Title: " + e.getTitle());
            }
        }
    }

    private void viewAllGrades() {
        Request req = new Request("GET_ALL_GRADES", null);
        Response res = network.sendRequest(req);
        if (res.getStatus().equals("SUCCESS")) {
            Grade[] grades = (Grade[]) res.getPayload();
            for (Grade g : grades) {
                System.out.println(g.toCsv());
            }
        }
    }

    private void takeExam() {
        System.out.print("Enter Exam ID to take: ");
        String examId = scanner.nextLine();

        Request req = new Request("GET_ALL_EXAMS", null);
        Response res = network.sendRequest(req);

        if (res.getStatus().equals("SUCCESS")) {
            Exam[] exams = (Exam[]) res.getPayload();
            Exam targetExam = null;
            for (Exam e : exams) {
                if (e.getId().equals(examId)) {
                    targetExam = e;
                    break;
                }
            }

            if (targetExam == null) {
                System.out.println("Exam not found.");
                return;
            }

            int score = 0;
            java.util.ArrayList<String> questions = targetExam.getQuestions();
            java.util.ArrayList<String> answers = targetExam.getAnswers();

            System.out.println("\n--- Taking Exam: " + targetExam.getTitle() + " ---");
            // show questions and read answers
            for (int i = 0; i < questions.size(); i++) {
                System.out.println("Q" + (i + 1) + ": " + questions.get(i));
                System.out.print("Your answer: ");
                String userAnswer = scanner.nextLine();
                // user can answer with lowercase or uppercase
                if (userAnswer.equalsIgnoreCase(answers.get(i))) {
                    score++;
                }
            }

            System.out.println("Exam finished! You scored " + score + "/" + questions.size());

            Grade grade = new Grade(loggedInUser.getId(), targetExam.getId(), score, questions.size(), "EXAM");
            Request submitReq = new Request("SUBMIT_EXAM", grade);
            Response submitRes = network.sendRequest(submitReq);
            System.out.println(submitRes.getMessage());
        }
    }

    private void viewMyGrades() {
        Request req = new Request("GET_MY_GRADES", null);
        Response res = network.sendRequest(req);
        if (res.getStatus().equals("SUCCESS")) {
            Grade[] grades = (Grade[]) res.getPayload();
            for (Grade g : grades) {
                System.out.println(g.toCsv());
            }
        }
    }

    public static void main(String[] args) {
        MainUI ui = new MainUI();
        ui.start();
    }
}
