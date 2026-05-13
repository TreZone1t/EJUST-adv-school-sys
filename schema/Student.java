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
}
