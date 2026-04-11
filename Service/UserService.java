package Service;

import Model.User;
import java.io.*;
import java.util.*;

public class UserService {
    private final String FILE = "users.txt";
    private final List<User> users = new ArrayList<>();

    public UserService() { load(); }

    public boolean login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return false; // đã tồn tại
        }
        users.add(new User(username, password));
        save();
        return true;
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (User u : users) pw.println(u.toDataString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void load() {
        users.clear();
        File f = new File(FILE);
        if (!f.exists()) {
            // Tạo file mặc định nếu chưa có
            try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
                pw.println("admin,12345");
            } catch (IOException e) { e.printStackTrace(); }
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                User u = User.fromDataString(line.trim());
                if (u != null) users.add(u);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}