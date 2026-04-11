package Service;

import Model.DatCoc;
import java.io.*;
import java.util.*;

public class DatCocService {
    private final String FILE = "bookings.txt";
    private final List<DatCoc> danhSach = new ArrayList<>();
    private int nextId = 1;

    public DatCocService() { taiDuLieu(); }

    public List<DatCoc> layTatCa() { return danhSach; }

    public DatCoc timTheoId(int id) {
        for (DatCoc d : danhSach) if (d.getId() == id) return d;
        return null;
    }

    /** BDS chưa có bản ghi đặt cọc đang ACTIVE */
    public boolean coTheDatCoc(int maBDS) {
        for (DatCoc d : danhSach) {
            if (d.getMaBDS() == maBDS && "ACTIVE".equals(d.getTrangThai())) return false;
        }
        return true;
    }

    public DatCoc them(int maBDS, int idKhach, double tienCoc) {
        if (!coTheDatCoc(maBDS)) return null;
        if (tienCoc < 0) return null;
        DatCoc d = new DatCoc(nextId++, maBDS, idKhach, tienCoc, "ACTIVE");
        danhSach.add(d);
        luuDuLieu();
        return d;
    }

    public boolean hoanCoc(int id) {
        DatCoc d = timTheoId(id);
        if (d == null || !"ACTIVE".equals(d.getTrangThai())) return false;
        d.setTrangThai("RETURNED");
        luuDuLieu();
        return true;
    }

    public double tongTienTheoTrangThai(String trangThai) {
        double sum = 0;
        for (DatCoc d : danhSach) {
            if (trangThai.equals(d.getTrangThai())) sum += d.getTienCoc();
        }
        return sum;
    }

    private void luuDuLieu() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (DatCoc d : danhSach) pw.println(d.toDataString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void taiDuLieu() {
        danhSach.clear();
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int maxId = 0;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                DatCoc d = DatCoc.fromDataString(line);
                danhSach.add(d);
                if (d.getId() > maxId) maxId = d.getId();
            }
            nextId = maxId + 1;
        } catch (IOException e) { e.printStackTrace(); }
    }
}
