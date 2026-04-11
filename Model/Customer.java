// Model/Customer.java
package Model;

public class Customer {
    private int id;
    private String ten;
    private String cmnd;
    private String sdt;

    public Customer(int id, String ten, String cmnd, String sdt) {
        this.id = id;
        this.ten = ten;
        this.cmnd = cmnd;
        this.sdt = sdt;
    }

    public int getId() { return id; }
    public String getTen() { return ten; }
    public String getCmnd() { return cmnd; }
    public String getSdt() { return sdt; }

    public void setTen(String ten) { this.ten = ten; }
    public void setCmnd(String cmnd) { this.cmnd = cmnd; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String toDataString() { return id + "," + ten + "," + cmnd + "," + sdt; }

    public static Customer fromDataString(String s) {
        String[] p = s.split(",");
        return new Customer(Integer.parseInt(p[0]), p[1], p[2], p[3]);
    }
}