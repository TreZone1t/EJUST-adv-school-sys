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
}
