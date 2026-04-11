package UI;

import Model.BDS;
import Model.Customer;
import Model.DatCoc;
import Service.BDSService;
import Service.CustomerService;
import Service.DatCocService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class HotelManagerApp extends JFrame {

    private final BDSService dichVuBDS = new BDSService();
    private final CustomerService dichVuKhachHang = new CustomerService();
    private final DatCocService dichVuDatCoc = new DatCocService();

    private DefaultTableModel bdsModel, customerModel, datCocModel;
    private JTable bdsTable, customerTable, datCocTable;
    private JTabbedPane tabs;

    public HotelManagerApp() {

        thietLapGiaoDien();

        setTitle("Bán bất động sản");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.add("BDS", taoPanelBDS());
        tabs.add("Khách hàng", taoPanelKhachHang());
        tabs.add("Đặt cọc", taoPanelDatCoc());
        tabs.add("Tìm kiếm & Báo cáo", taoPanelBaoCao());

        add(tabs);
        lamMoiTatCaBang();
    }

    private void thietLapGiaoDien() {
        Color primary = new Color(52, 152, 219);
        Color background = new Color(245, 247, 250);

        UIManager.put("Panel.background", background);
        UIManager.put("TabbedPane.contentAreaColor", background);
        UIManager.put("TabbedPane.selected", Color.WHITE);

        UIManager.put("TableHeader.background", primary);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));

        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 13));
    }

    private JButton nutTuyChinh(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });

        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(46, 204, 113));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
    }

    /** Hiển thị số tiền đầy đủ, có nhóm hàng nghìn (không dùng dạng 2.0E10). */
    private static String dinhDangTienHienThi(double v) {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance(Locale.forLanguageTag("vi-VN"));
        DecimalFormat df = new DecimalFormat("#,##0.##########", sym);
        df.setGroupingUsed(true);
        df.setMaximumFractionDigits(12);
        return df.format(v);
    }

    private static void ganRendererTienChoCot(JTable table, int chiSoCot) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(dinhDangTienHienThi(((Number) value).doubleValue()));
                } else {
                    super.setValue(value);
                }
            }
        };
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(chiSoCot).setCellRenderer(r);
    }

    private JPanel taoPanelBDS() {
        JPanel panel = new JPanel(new BorderLayout());
        bdsModel = new DefaultTableModel(new String[]{"BDS", "Loại", "Giá", "Khu vực", "Diện tích", "Số tầng", "Đã cọc"}, 0);
        bdsTable = new JTable(bdsModel);
        styleTable(bdsTable);
        ganRendererTienChoCot(bdsTable, 2);

        JScrollPane sp = new JScrollPane(bdsTable);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(sp, BorderLayout.CENTER);

        JButton them = nutTuyChinh("Thêm");
        JButton sua = nutTuyChinh("Sửa");
        JButton xoa = nutTuyChinh("Xóa");
        JButton doiTrangThai = nutTuyChinh("Đổi trạng thái");
        JButton timKiemLoai = nutTuyChinh("Tìm theo loại");

        JButton taiLaiBDS = nutTuyChinh("Tải lại dữ liệu");
        taiLaiBDS.setBackground(new Color(46, 204, 113));
        taiLaiBDS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { taiLaiBDS.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { taiLaiBDS.setBackground(new Color(46, 204, 113)); }
        });

        JPanel actions = new JPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        actions.add(them);
        actions.add(sua);
        actions.add(xoa);
        actions.add(doiTrangThai);
        actions.add(timKiemLoai);
        actions.add(taiLaiBDS);
        panel.add(actions, BorderLayout.SOUTH);

        taiLaiBDS.addActionListener(e -> lamMoiBDS());

        them.addActionListener(e -> {
            JTextField fBDS = new JTextField();
            JTextField fLoai = new JTextField();
            JTextField fGia = new JTextField();
            JTextField fKhuVuc = new JTextField();
            JTextField fDienTich = new JTextField();
            JTextField fSoTang = new JTextField();
            Object[] msg = {
                    "BDS (mã):", fBDS, "Loại:", fLoai, "Giá:", fGia, "Khu vực:", fKhuVuc,
                    "Diện tích (m²):", fDienTich, "Số tầng:", fSoTang
            };
            if (JOptionPane.showConfirmDialog(this, msg, "Thêm BDS", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int ma = Integer.parseInt(fBDS.getText());
                    String loai = fLoai.getText();
                    double gia = Double.parseDouble(fGia.getText());
                    String kv = fKhuVuc.getText();
                    double dt = Double.parseDouble(fDienTich.getText().trim().isEmpty() ? "0" : fDienTich.getText().trim());
                    int tang = Integer.parseInt(fSoTang.getText().trim().isEmpty() ? "0" : fSoTang.getText().trim());
                    boolean ok = dichVuBDS.them(new BDS(ma, loai, gia, kv, dt, tang));
                    JOptionPane.showMessageDialog(this, ok ? "Thêm thành công" : "Mã BDS đã tồn tại");
                    lamMoiBDS();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ");
                }
            }
        });

        sua.addActionListener(e -> {
            int row = bdsTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn BDS"); return; }

            int maBDS = (int) bdsModel.getValueAt(row, 0);
            JTextField fLoai = new JTextField(bdsModel.getValueAt(row, 1).toString());
            JTextField fGia = new JTextField(bdsModel.getValueAt(row, 2).toString());
            JTextField fKhuVuc = new JTextField(bdsModel.getValueAt(row, 3).toString());
            JTextField fDienTich = new JTextField(bdsModel.getValueAt(row, 4).toString());
            JTextField fSoTang = new JTextField(bdsModel.getValueAt(row, 5).toString());
            Object[] msg = {
                    "Loại:", fLoai, "Giá:", fGia, "Khu vực:", fKhuVuc,
                    "Diện tích (m²):", fDienTich, "Số tầng:", fSoTang
            };

            if (JOptionPane.showConfirmDialog(this, msg, "Sửa BDS", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    double dt = Double.parseDouble(fDienTich.getText().trim().isEmpty() ? "0" : fDienTich.getText().trim());
                    int tang = Integer.parseInt(fSoTang.getText().trim().isEmpty() ? "0" : fSoTang.getText().trim());
                    dichVuBDS.capNhat(maBDS, fLoai.getText(), Double.parseDouble(fGia.getText()), fKhuVuc.getText(), dt, tang);
                    lamMoiBDS();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ");
                }
            }
        });

        xoa.addActionListener(e -> {
            int row = bdsTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn BDS"); return; }

            int maBDS = (int) bdsModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa BDS mã " + maBDS + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dichVuBDS.xoa(maBDS);
                lamMoiBDS();
            }
        });

        doiTrangThai.addActionListener(e -> {
            int row = bdsTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn BDS"); return; }

            int maBDS = (int) bdsModel.getValueAt(row, 0);
            boolean current = (boolean) bdsModel.getValueAt(row, 6);
            dichVuBDS.danhDauCoc(maBDS, !current);
            lamMoiBDS();
        });

        timKiemLoai.addActionListener(e -> {
            String loai = JOptionPane.showInputDialog(
                    this,
                    "Nhập loại BDS cần tìm (từ khóa trong trường Loại):",
                    "Tìm kiếm BDS theo loại",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (loai != null) {
                List<BDS> rs = dichVuBDS.timKiemTheoLoai(loai);

                if (rs.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Không tìm thấy BDS phù hợp với: \"" + loai + "\"",
                            "Kết quả tìm kiếm",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    lamMoiBDS();
                    return;
                }

                bdsModel.setRowCount(0);
                for (BDS b : rs) {
                    bdsModel.addRow(new Object[]{
                            b.getMaBDS(),
                            b.getLoai(),
                            b.getGia(),
                            b.getKhuVuc(),
                            b.getDienTich(),
                            b.getSoTang(),
                            b.isDaCoc()
                    });
                }
            }
        });

        return panel;
    }

    private JPanel taoPanelKhachHang() {
        JPanel panel = new JPanel(new BorderLayout());

        customerModel = new DefaultTableModel(new String[]{"ID", "Tên", "CMND/CCCD", "SĐT"}, 0);
        customerTable = new JTable(customerModel);
        styleTable(customerTable);

        JScrollPane sp = new JScrollPane(customerTable);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(sp, BorderLayout.CENTER);

        JButton them = nutTuyChinh("Thêm");
        JButton sua = nutTuyChinh("Sửa");
        JButton xoa = nutTuyChinh("Xóa");
        JButton timKiem = nutTuyChinh("Tìm kiếm");

        JButton taiLaiKhachHang = nutTuyChinh("Tải lại dữ liệu");
        taiLaiKhachHang.setBackground(new Color(46, 204, 113));
        taiLaiKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { taiLaiKhachHang.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { taiLaiKhachHang.setBackground(new Color(46, 204, 113)); }
        });

        JPanel actions = new JPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        actions.add(them);
        actions.add(sua);
        actions.add(xoa);
        actions.add(timKiem);
        actions.add(taiLaiKhachHang);
        panel.add(actions, BorderLayout.SOUTH);

        taiLaiKhachHang.addActionListener(e -> lamMoiKhachHang());

        them.addActionListener(e -> {
            JTextField f1 = new JTextField();
            JTextField f2 = new JTextField();
            JTextField f3 = new JTextField();
            Object[] msg = {"Tên:", f1, "CMND/CCCD:", f2, "SĐT:", f3};

            if (JOptionPane.showConfirmDialog(this, msg, "Thêm khách", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                dichVuKhachHang.them(f1.getText(), f2.getText(), f3.getText());
                lamMoiKhachHang();
            }
        });

        sua.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn khách"); return; }

            int id = (int) customerModel.getValueAt(row, 0);
            JTextField f1 = new JTextField(customerModel.getValueAt(row, 1).toString());
            JTextField f2 = new JTextField(customerModel.getValueAt(row, 2).toString());
            JTextField f3 = new JTextField(customerModel.getValueAt(row, 3).toString());
            Object[] msg = {"Tên:", f1, "CMND/CCCD:", f2, "SĐT:", f3};

            if (JOptionPane.showConfirmDialog(this, msg, "Sửa khách", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                dichVuKhachHang.capNhat(id, f1.getText(), f2.getText(), f3.getText());
                lamMoiKhachHang();
            }
        });

        xoa.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn khách"); return; }

            int id = (int) customerModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa khách hàng ID " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dichVuKhachHang.xoa(id);
                lamMoiKhachHang();
            }
        });

        timKiem.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(this, "Nhập từ khóa (Tên, CMND, SĐT):");
            if (keyword != null) {
                List<Customer> rs = dichVuKhachHang.timKiem(keyword);
                customerModel.setRowCount(0);
                for (Customer c : rs)
                    customerModel.addRow(new Object[]{c.getId(), c.getTen(), c.getCmnd(), c.getSdt()});
            }
        });

        return panel;
    }

    private JPanel taoPanelDatCoc() {
        JPanel panel = new JPanel(new BorderLayout());

        datCocModel = new DefaultTableModel(new String[]{"ID", "BDS", "Khách", "Tiền cọc", "Trạng thái"}, 0);
        datCocTable = new JTable(datCocModel);
        styleTable(datCocTable);
        ganRendererTienChoCot(datCocTable, 3);

        JScrollPane sp = new JScrollPane(datCocTable);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(sp, BorderLayout.CENTER);

        JButton them = nutTuyChinh("Tạo đặt cọc");
        JButton hoanCoc = nutTuyChinh("Hoàn cọc");

        JButton taiLai = nutTuyChinh("Tải lại dữ liệu");
        taiLai.setBackground(new Color(46, 204, 113));
        taiLai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { taiLai.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { taiLai.setBackground(new Color(46, 204, 113)); }
        });

        JPanel actions = new JPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        actions.add(them);
        actions.add(hoanCoc);
        actions.add(taiLai);
        panel.add(actions, BorderLayout.SOUTH);

        them.addActionListener(e -> {
            JTextField maBDSF = new JTextField();
            JTextField idKhachF = new JTextField();
            JTextField tienF = new JTextField();

            Object[] msg = {
                "BDS (mã):", maBDSF,
                "ID Khách:", idKhachF,
                "Tiền cọc:", tienF
            };

            if (JOptionPane.showConfirmDialog(this, msg, "Đặt cọc", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int maBDS = Integer.parseInt(maBDSF.getText());
                    int idKhach = Integer.parseInt(idKhachF.getText());
                    double tien = Double.parseDouble(tienF.getText());

                    BDS b = dichVuBDS.timTheoMaBDS(maBDS);
                    Customer c = dichVuKhachHang.timTheoId(idKhach);

                    if (b == null) { JOptionPane.showMessageDialog(this, "BDS không tồn tại"); return; }
                    if (c == null) {
                        int choice = JOptionPane.showConfirmDialog(this,
                                "ID Khách hàng không tồn tại. Chuyển sang tab Khách hàng để thêm mới?",
                                "Khách hàng không tồn tại",
                                JOptionPane.YES_NO_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            tabs.setSelectedIndex(1);
                        }
                        return;
                    }

                    if (!dichVuDatCoc.coTheDatCoc(maBDS)) {
                        JOptionPane.showMessageDialog(this, "BDS này đã có đặt cọc đang hiệu lực.");
                        return;
                    }

                    DatCoc d = dichVuDatCoc.them(maBDS, idKhach, tien);

                    if (d == null) {
                        JOptionPane.showMessageDialog(this, "Không thể tạo đặt cọc (kiểm tra số tiền).");
                    } else {
                        dichVuBDS.danhDauCoc(maBDS, true);
                        lamMoiTatCaBang();
                        JOptionPane.showMessageDialog(this, "Đặt cọc thành công. ID: " + d.getId());
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage());
                }
            }
        });

        hoanCoc.addActionListener(e -> {
            int row = datCocTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn bản ghi đặt cọc"); return; }

            int id = (int) datCocModel.getValueAt(row, 0);
            int maBDS = (int) datCocModel.getValueAt(row, 1);
            String trangThai = datCocModel.getValueAt(row, 4).toString();

            if ("RETURNED".equalsIgnoreCase(trangThai.trim())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đặt cọc ID " + id + " đã được hoàn trước đó.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận hoàn cọc ID " + id + "?", "Hoàn cọc", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = dichVuDatCoc.hoanCoc(id);
                if (ok) {
                    dichVuBDS.danhDauCoc(maBDS, false);
                    lamMoiTatCaBang();
                    JOptionPane.showMessageDialog(this, "Hoàn cọc thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể hoàn cọc (đã hoàn hoặc ID không tồn tại).");
                }
            }
        });

        taiLai.addActionListener(e -> lamMoiDatCoc());
        return panel;
    }

    private JPanel taoPanelBaoCao() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(sp, BorderLayout.CENTER);

        JButton taoBaoCao = nutTuyChinh("Thống kê nhanh");
        JButton lietKeBDSChuaCoc = nutTuyChinh("BDS chưa cọc");

        JButton taiLaiToanBo = nutTuyChinh("Tải lại toàn bộ bảng");
        taiLaiToanBo.setBackground(new Color(46, 204, 113));
        taiLaiToanBo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { taiLaiToanBo.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { taiLaiToanBo.setBackground(new Color(46, 204, 113)); }
        });

        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(taoBaoCao);
        top.add(lietKeBDSChuaCoc);
        top.add(taiLaiToanBo);

        panel.add(top, BorderLayout.NORTH);

        taiLaiToanBo.addActionListener(e -> {
            lamMoiTatCaBang();
            area.setText("Đã làm mới dữ liệu của tất cả các bảng.");
        });

        taoBaoCao.addActionListener(e -> {
            long active = dichVuDatCoc.layTatCa().stream().filter(b -> b.getTrangThai().equals("ACTIVE")).count();
            long returned = dichVuDatCoc.layTatCa().stream().filter(b -> b.getTrangThai().equals("RETURNED")).count();
            long daCoc = dichVuBDS.layTatCa().stream().filter(BDS::isDaCoc).count();
            long chuaCoc = dichVuBDS.layTatCa().size() - daCoc;
            double tongActive = dichVuDatCoc.tongTienTheoTrangThai("ACTIVE");
            double tongReturned = dichVuDatCoc.tongTienTheoTrangThai("RETURNED");

            area.setText(
                    "BÁO CÁO THỐNG KÊ (BẤT ĐỘNG SẢN)\n" +
                    "- Tổng tiền cọc đang giữ (ACTIVE): " + String.format("%,.2f VND", tongActive) + "\n" +
                    "- Tổng tiền đã hoàn cọc (RETURNED): " + String.format("%,.2f VND", tongReturned) + "\n" +
                    "- Số đặt cọc đang hiệu lực: " + active + "\n" +
                    "- Số đặt cọc đã hoàn: " + returned + "\n" +
                    "- BDS đã cọc (theo danh mục): " + daCoc + "\n" +
                    "- BDS chưa cọc: " + chuaCoc + "\n"
            );
        });

        lietKeBDSChuaCoc.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("BDS CHƯA CỌC:\n");
            for (BDS b : dichVuBDS.timKiemKhaDung()) {
                sb.append("- BDS ").append(b.getMaBDS())
                        .append(" | ").append(b.getLoai())
                        .append(" | ").append(b.getKhuVuc())
                        .append(" | ").append(b.getDienTich()).append(" m² | ").append(b.getSoTang()).append(" tầng")
                        .append(" | Giá: ").append(b.getGia()).append("\n");
            }
            area.setText(sb.toString());
        });

        return panel;
    }

    private void lamMoiBDS() {
        bdsModel.setRowCount(0);
        for (BDS b : dichVuBDS.layTatCa()) {
            bdsModel.addRow(new Object[]{
                    b.getMaBDS(),
                    b.getLoai(),
                    b.getGia(),
                    b.getKhuVuc(),
                    b.getDienTich(),
                    b.getSoTang(),
                    b.isDaCoc()
            });
        }
    }

    private void lamMoiKhachHang() {
        customerModel.setRowCount(0);
        for (Customer c : dichVuKhachHang.layTatCa()) {
            customerModel.addRow(new Object[]{
                    c.getId(), c.getTen(), c.getCmnd(), c.getSdt()
            });
        }
    }

    private void lamMoiDatCoc() {
        datCocModel.setRowCount(0);
        for (DatCoc d : dichVuDatCoc.layTatCa()) {
            datCocModel.addRow(new Object[]{
                    d.getId(), d.getMaBDS(), d.getIdKhach(),
                    d.getTienCoc(), d.getTrangThai()
            });
        }
    }

    private void lamMoiTatCaBang() {
        lamMoiBDS();
        lamMoiKhachHang();
        lamMoiDatCoc();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelManagerApp().setVisible(true));
    }
}
