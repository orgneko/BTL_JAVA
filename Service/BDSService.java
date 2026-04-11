package Service;

import Model.BDS;
import java.io.*;
import java.util.*;

public class BDSService {
    private final String FILE = "rooms.txt";
    private final List<BDS> danhSach = new ArrayList<>();

    public BDSService() { taiDuLieu(); }

    public List<BDS> layTatCa() { return danhSach; }

    public BDS timTheoMaBDS(int maBDS) {
        for (BDS b : danhSach) if (b.getMaBDS() == maBDS) return b;
        return null;
    }

    public boolean them(BDS b) {
        if (timTheoMaBDS(b.getMaBDS()) != null) return false;
        danhSach.add(b);
        luuDuLieu();
        return true;
    }

    public boolean capNhat(int maBDS, String loai, double gia, String khuVuc, double dienTich, int soTang) {
        BDS b = timTheoMaBDS(maBDS);
        if (b == null) return false;
        b.setLoai(loai);
        b.setGia(gia);
        b.setKhuVuc(khuVuc);
        b.setDienTich(dienTich);
        b.setSoTang(soTang);
        luuDuLieu();
        return true;
    }

    public boolean xoa(int maBDS) {
        boolean ok = danhSach.removeIf(b -> b.getMaBDS() == maBDS);
        if (ok) luuDuLieu();
        return ok;
    }

    public void danhDauCoc(int maBDS, boolean daCoc) {
        BDS b = timTheoMaBDS(maBDS);
        if (b != null) { b.setDaCoc(daCoc); luuDuLieu(); }
    }

    public List<BDS> timKiemKhaDung() {
        List<BDS> out = new ArrayList<>();
        for (BDS b : danhSach) if (!b.isDaCoc()) out.add(b);
        return out;
    }

    public List<BDS> timKiemTheoLoai(String loai) {
        List<BDS> out = new ArrayList<>();
        if (loai == null) return out;
        String keyword = loai.trim().toLowerCase();
        for (BDS b : danhSach) {
            String type = b.getLoai() == null ? "" : b.getLoai().trim().toLowerCase();
            if (type.contains(keyword)) out.add(b);
        }
        return out;
    }

    private void luuDuLieu() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (BDS b : danhSach) pw.println(b.toDataString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void taiDuLieu() {
        danhSach.clear();
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                danhSach.add(BDS.fromDataString(line));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
