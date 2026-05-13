package server;

import schema.*;
import java.io.*; // for File and PrintWriter
import java.util.*; // for ArrayList and Scanner 

public class DataManager {
    private ArrayList<User> users;
    private ArrayList<Exam> exams;
    private ArrayList<Grade> grades;

    public DataManager() {
        // initialise the data structures
        users = new ArrayList<>();
        exams = new ArrayList<>();
        grades = new ArrayList<>();
        // create the data directory if it doesn't exist
        new File("data").mkdirs();
        // load the existing data from the files from each .csv file
        loadUsers();
        loadExams();
        loadGrades();
        // if there is no data, add the default data for the manager
        if (users.isEmpty()) {
            users.add(new Manager("m1", "admin", "admin", "System Admin"));
        }
    }

    private void loadUsers() {
        try {
            // create a file to store the users data
            File f = new File("data/users.csv");
            // if the file doesn't exist, return
            if (!f.exists())
                return;
            // create a scanner to read the users from
            Scanner sc = new java.util.Scanner(f);
            // read the users from the file
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",");
                if (p.length == 5) {
                    if (p[1].equals("MANAGER")) {
                        users.add(new Manager(p[0], p[2], p[3], p[4]));
                    } else if (p[1].equals("TEACHER")) {
                        users.add(new Teacher(p[0], p[2], p[3], p[4]));
                    } else {
                        users.add(new Student(p[0], p[2], p[3], p[4]));
                    }
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try {
            // open a file writer to save users
            PrintWriter pw = new PrintWriter(new File("data/users.csv"));
            // loop over users and save them
            for (User u : users)
                pw.println(u.toCsv());
            // close the file
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        // loop over all users to check login
        for (User u : users) {
            // if username and password match, return the user
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        // if no match, return null
        return null;
    }

    public boolean addUser(User user) {
        // check if username already exists
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()))
                return false;
        }
        // add user and save to file
        users.add(user);
        saveUsers();
        return true;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    private void loadExams() {
        try {
            // create a file object for exams
            File f = new File("data/exams.csv");
            if (!f.exists())
                return;
            // use scanner to read the file
            java.util.Scanner sc = new java.util.Scanner(f);
            while (sc.hasNextLine()) {
                // parse exam from string and add to list
                Exam exam = Exam.fromCsv(sc.nextLine());
                if (exam != null)
                    exams.add(exam);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading exams: " + e.getMessage());
        }
    }

    private void saveExams() {
        try {
            // open file writer for exams
            PrintWriter pw = new PrintWriter(new File("data/exams.csv"));
            for (Exam e : exams)
                pw.println(e.toCsv());
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving exams: " + e.getMessage());
        }
    }

    public void addExam(Exam exam) {
        exams.add(exam);
        saveExams();
    }

    public ArrayList<Exam> getAllExams() {
        return exams;
    }

    private void loadGrades() {
        try {
            // open grades file
            File f = new File("data/grades.csv");
            if (!f.exists())
                return;
            java.util.Scanner sc = new java.util.Scanner(f);
            // read grades line by line
            while (sc.hasNextLine()) {
                Grade grade = Grade.fromCsv(sc.nextLine());
                if (grade != null)
                    grades.add(grade);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading grades: " + e.getMessage());
        }
    }

    private void saveGrades() {
        try {
            // save all grades to file
            PrintWriter pw = new PrintWriter(new File("data/grades.csv"));
            for (Grade g : grades)
                pw.println(g.toCsv());
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving grades: " + e.getMessage());
        }
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
        saveGrades();
    }

    public ArrayList<Grade> getStudentGrades(String studentId) {
        // find all grades for a specific student
        ArrayList<Grade> res = new ArrayList<>();
        for (Grade g : grades) {
            // check if student id matches
            if (g.getStudentId().equals(studentId)) {
                res.add(g);
            }
        }
        return res;
    }

    public ArrayList<Grade> getAllGrades() {
        return grades;
    }
}
