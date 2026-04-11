package Model;

public class BDS {
    private int maBDS;
    private String loai;
    private double gia;
    private String khuVuc;
    private double dienTich;
    private int soTang;
    private boolean daCoc;

    public BDS(int maBDS, String loai, double gia, String khuVuc, double dienTich, int soTang) {
        this.maBDS = maBDS;
        this.loai = loai;
        this.gia = gia;
        this.khuVuc = khuVuc == null ? "" : khuVuc;
        this.dienTich = dienTich;
        this.soTang = soTang;
        this.daCoc = false;
    }

    /** Tương thích mã cũ: diện tích 0, số tầng 0 */
    public BDS(int maBDS, String loai, double gia, String khuVuc) {
        this(maBDS, loai, gia, khuVuc, 0, 0);
    }

    public int getMaBDS() { return maBDS; }
    public String getLoai() { return loai; }
    public double getGia() { return gia; }
    public String getKhuVuc() { return khuVuc; }
    public double getDienTich() { return dienTich; }
    public int getSoTang() { return soTang; }
    public boolean isDaCoc() { return daCoc; }

    public void setLoai(String loai) { this.loai = loai; }
    public void setGia(double gia) { this.gia = gia; }
    public void setKhuVuc(String khuVuc) { this.khuVuc = khuVuc == null ? "" : khuVuc; }
    public void setDienTich(double dienTich) { this.dienTich = dienTich; }
    public void setSoTang(int soTang) { this.soTang = soTang; }
    public void setDaCoc(boolean daCoc) { this.daCoc = daCoc; }

    @Override
    public String toString() {
        return "BDS " + maBDS + " | " + loai + " | " + khuVuc
                + " | " + dienTich + " m² | " + soTang + " tầng | Giá: " + gia
                + " | " + (daCoc ? "Đã cọc" : "Chưa cọc");
    }

    public String toDataString() {
        return maBDS + "," + loai + "," + gia + "," + daCoc + "," + khuVuc.replace(",", " ")
                + "," + dienTich + "," + soTang;
    }

    public static BDS fromDataString(String s) {
        String[] p = s.split(",", -1);
        if (p.length >= 7) {
            BDS b = new BDS(
                    Integer.parseInt(p[0].trim()),
                    p[1].trim(),
                    Double.parseDouble(p[2].trim()),
                    p[4].trim(),
                    Double.parseDouble(p[5].trim()),
                    Integer.parseInt(p[6].trim())
            );
            b.setDaCoc(Boolean.parseBoolean(p[3].trim()));
            return b;
        }
        if (p.length >= 5) {
            BDS b = new BDS(Integer.parseInt(p[0].trim()), p[1].trim(), Double.parseDouble(p[2].trim()), p[4].trim());
            b.setDaCoc(Boolean.parseBoolean(p[3].trim()));
            return b;
        }
        BDS b = new BDS(Integer.parseInt(p[0].trim()), p[1].trim(), Double.parseDouble(p[2].trim()), "");
        b.setDaCoc(Boolean.parseBoolean(p[3].trim()));
        return b;
    }
}
