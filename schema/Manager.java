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
    @Override
    public int showMenu() {
        System.out.println("\n<---- Manager Menu ---->\n");
        System.out.println("1. Add User");
        System.out.println("2. View All Users");
        System.out.println("3. Edit Grade");
        System.out.println("4. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine();
        if (choice.equals("1"))
            return 11;
        else if (choice.equals("2"))
            return 2;
        else if (choice.equals("3"))
            return 13;
        else if (choice.equals("4")) {
            return 0;
        } else
        return -1;
    }
}
