package schema;

public class Student extends User {
    public Student(String id, String username, String password, String name) {
        super(id, username, password, name);
    }

    @Override
    public String[] getAllowedActions() {
        return new String[] { "GET_ALL_EXAMS", "SUBMIT_EXAM", "GET_MY_GRADES", "LOGOUT", "QUIT" };
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }
    @Override
    public int showMenu() {
        System.out.println("\n<---- Student Menu ---->\n");
        System.out.println("1. View All Exams");
        System.out.println("2. Submit Exam");
        System.out.println("3. View My Grades");
        System.out.println("4. Logout");
        System.out.print("Choice: ");
        String choice = super.scanner.nextLine();
        if (choice.equals("1"))
            return 11;
        else if (choice.equals("2"))
            return 12;
        else if (choice.equals("3"))
            return 13;
        else if (choice.equals("4")) {
            return 0;
        } else
        return -1;
}
}
