package schema;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String role; // "MANAGER", "TEACHER", "STUDENT"
    private String username;
    private String password;
    private String name;

    public User(String id, String role, String username, String password, String name) {
        // initialize the user attributes
        this.id = id;
        this.role = role.toUpperCase();
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getId() { return id; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }

    public String toCsv() {
        // convert the user to a csv string
        return id + "," + role + "," + username + "," + password + "," + name;
    }
}
