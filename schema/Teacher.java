package schema;

public class Teacher extends User {
    public Teacher(String id, String username, String password, String name) {
        super(id, username, password, name);
    }

    @Override
    public String[] getAllowedActions() {
        return new String[] { "CREATE_EXAM", "GET_ALL_EXAMS", "UPLOAD_GRADE", "GET_ALL_GRADES", "LOGOUT", "QUIT" };
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }

    @Override
    public int showMenu() {
        System.out.println("\n<---- Teacher Menu ---->\n");
        System.out.println("1. Create Exam");
        System.out.println("2. View All Exams");
        System.out.println("3. Upload Grade");
        System.out.println("4. View All Grades");
        System.out.println("5. Logout");
        System.out.print("Choice: ");
        String choice = super.scanner.nextLine();
        if (choice.equals("1"))
            return 21;
        else if (choice.equals("2"))
            return 22;
        else if (choice.equals("3"))
            return 23;
        else if (choice.equals("4"))
            return 24;
        else if (choice.equals("5")) {
            return 0;
        } else
            return -1;
    }

}
