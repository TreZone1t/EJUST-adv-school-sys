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
                // check role and show correct menu
                String role = loggedInUser.getRole();
                if (role.equals("MANAGER")) {
                    showManagerMenu();
                } else if (role.equals("TEACHER")) {
                    showTeacherMenu();
                } else if (role.equals("STUDENT")) {
                    showStudentMenu();
                }
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n<----Login---->\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
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

    private void showManagerMenu() {
        System.out.println("\n<---- Manager Menu ---->\n");
        System.out.println("1. Add User");
        System.out.println("2. View All Users");
        System.out.println("3. Edit Grade");
        System.out.println("4. Logout");
        System.out.print("Choice: ");

        // switch based on user choice
        String choice = scanner.nextLine();
        if (choice.equals("1"))
            addUser();
        else if (choice.equals("2"))
            viewAllUsers();
        else if (choice.equals("3"))
            editGrade();
        else if (choice.equals("4")) {
            // tell server we are leaving
            network.sendRequest(new Request("QUIT", null));
            loggedInUser = null;
        } else
            System.out.println("Invalid choice.");
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

        User newUser = new User(id, role, username, password, name);
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

    private void showTeacherMenu() {
        System.out.println("\n<---- Teacher Menu ---->\n");
        System.out.println("1. Create Exam");
        System.out.println("2. Upload Exam from File");
        System.out.println("3. View All Exams");
        System.out.println("4. Upload Grade");
        System.out.println("5. View All Grades");
        System.out.println("6. Logout");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        if (choice.equals("1"))
            createExam();
        else if (choice.equals("2"))
            uploadExamFromFile();
        else if (choice.equals("3"))
            viewAllExams();
        else if (choice.equals("4"))
            editGrade();
        else if (choice.equals("5"))
            viewAllGrades();
        else if (choice.equals("6")) {
            network.sendRequest(new Request("QUIT", null));
            loggedInUser = null;
        } else
            System.out.println("Invalid choice.");
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

    private void showStudentMenu() {
        System.out.println("\n<---- Student Menu ---->\n");
        System.out.println("1. View Available Exams");
        System.out.println("2. Take Exam");
        System.out.println("3. View My Grades");
        System.out.println("4. Logout");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        if (choice.equals("1"))
            viewAllExams();
        else if (choice.equals("2"))
            takeExam();
        else if (choice.equals("3"))
            viewMyGrades();
        else if (choice.equals("4")) {
            network.sendRequest(new Request("QUIT", null));
            loggedInUser = null;
        } else
            System.out.println("Invalid choice.");
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
