package schema;

public class Manager extends User {
    public Manager(String id, String username, String password, String name) {
        super(id, username, password, name);
    }

    @Override
    public String getRole() {
        return "MANAGER";
    }

    @Override
    public String[] getAllowedActions() {
        return new String[] { "ADD_USER", "GET_ALL_USERS", "UPLOAD_GRADE", "LOGOUT", "GET_ALL_GRADES", "QUIT" };
    }
}
