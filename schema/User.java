package schema;

import java.io.Serializable;

public abstract class User implements Serializable, CsvExportable {
    private String id;
    private String username;
    private String password;
    private String name;

    public User(String id, String username, String password, String name) {
        // initialize the user attributes
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public abstract String getRole();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toCsv() {
        // convert the user to a csv string
        return id + "," + getRole() + "," + username + "," + password + "," + name;
    }

    public abstract String[] getAllowedActions();

    public boolean canPerform(String action) {
        String[] allowedActions = getAllowedActions();
        if (allowedActions == null)
            return false;
        for (String a : allowedActions) {
            if (a.equals(action))
                return true;
        }
        return false;
    }
}
