package Service;

import Model.Customer;
import java.io.*;
import java.util.*;

public class CustomerService {
    private final String FILE = "customers.txt";
    private final List<Customer> customers = new ArrayList<>();
    private int nextId = 1;

    public CustomerService() { taiDuLieu(); } // Constructor gọi hàm taiDuLieu()

    public List<Customer> layTatCa() { return customers; } // Đổi từ getAll()
    public Customer timTheoId(int id) { // Đổi từ findById(int id)
        for (Customer c : customers) if (c.getId() == id) return c;
        return null;
    }
    public List<Customer> timKiem(String keyword) { // Đổi từ search(String keyword)
        keyword = keyword.toLowerCase();
        List<Customer> out = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getTen().toLowerCase().contains(keyword) ||
                c.getCmnd().toLowerCase().contains(keyword) ||
                c.getSdt().toLowerCase().contains(keyword)) {
                out.add(c);
            }
        }
        return out;
    }
    
    // PHƯƠNG THỨC MỚI ĐƯỢC THÊM: Tìm kiếm theo Tên hoặc CMND/CCCD
    public List<Customer> timKiemTheoTenHoacCmnd(String keyword) { // Đổi từ searchByNameOrId(String keyword)
        keyword = keyword.toLowerCase();
        List<Customer> out = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getTen().toLowerCase().contains(keyword) ||
                c.getCmnd().toLowerCase().contains(keyword)) {
                out.add(c);
            }
        }
        return out;
    }

    public Customer them(String ten, String cmnd, String sdt) { // Đổi từ add(...)
        Customer c = new Customer(nextId++, ten, cmnd, sdt);
        customers.add(c);
        luuDuLieu(); // Đổi từ save()
        return c;
    }

    public boolean capNhat(int id, String ten, String cmnd, String sdt) { // Đổi từ update(...)
        Customer c = timTheoId(id);
        if (c == null) return false;
        c.setTen(ten);
        c.setCmnd(cmnd);
        c.setSdt(sdt);
        luuDuLieu(); // Đổi từ save()
        return true;
    }

    public boolean xoa(int id) { // Đổi từ remove(int id)
        boolean ok = customers.removeIf(c -> c.getId() == id);
        if (ok) luuDuLieu(); // Đổi từ save()
        return ok;
    }

    private void luuDuLieu() { // Đổi từ save()
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Customer c : customers) pw.println(c.toDataString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void taiDuLieu() { // Đổi từ load()
        customers.clear();
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; int maxId = 0;
            while ((line = br.readLine()) != null) {
                Customer c = Customer.fromDataString(line);
                customers.add(c);
                if (c.getId() > maxId) maxId = c.getId();
            }
            nextId = maxId + 1;
        } catch (IOException e) { e.printStackTrace(); }
    }
}