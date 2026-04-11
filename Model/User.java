package Model;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public String toDataString() { return username + "," + password; }

    public static User fromDataString(String s) {
        String[] p = s.split(",");
        if (p.length < 2) return null;
    return new User(p[0].trim(), p[1].trim());
}
}