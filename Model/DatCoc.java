package Model;

public class DatCoc {
    private int id;
    private int maBDS;
    private int idKhach;
    private double tienCoc;
    private String trangThai;

    public DatCoc(int id, int maBDS, int idKhach, double tienCoc, String trangThai) {
        this.id = id;
        this.maBDS = maBDS;
        this.idKhach = idKhach;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public int getMaBDS() { return maBDS; }
    public int getIdKhach() { return idKhach; }
    public double getTienCoc() { return tienCoc; }
    public String getTrangThai() { return trangThai; }

    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String toDataString() {
        return id + "," + maBDS + "," + idKhach + "," + tienCoc + "," + trangThai;
    }

    /** Định dạng mới: id,maBDS,idKhach,tien,trạng_thái — hoặc cũ (đặt phòng): có thêm ngayNhan,ngayTra */
    public static DatCoc fromDataString(String s) {
        String[] p = s.split(",", -1);
        if (p.length >= 7) {
            return new DatCoc(
                    Integer.parseInt(p[0].trim()),
                    Integer.parseInt(p[1].trim()),
                    Integer.parseInt(p[2].trim()),
                    Double.parseDouble(p[5].trim()),
                    p[6].trim()
            );
        }
        return new DatCoc(
                Integer.parseInt(p[0].trim()),
                Integer.parseInt(p[1].trim()),
                Integer.parseInt(p[2].trim()),
                Double.parseDouble(p[3].trim()),
                p[4].trim()
        );
    }
}
