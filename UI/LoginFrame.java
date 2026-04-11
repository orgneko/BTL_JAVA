package UI;

import Service.UserService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UserService userService = new UserService();

    public LoginFrame() {
        setTitle("Đăng nhập / Đăng ký");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ====================
        // "CSS" cho form
        // ====================
        Color primary = new Color(52, 152, 219);
        Color primaryDark = new Color(41, 128, 185);
        Color background = new Color(236, 240, 241);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        JButton loginBtn = new JButton("Đăng nhập");
        JButton registerBtn = new JButton("Đăng ký");

        // Style cho nút
        JButton[] buttons = {loginBtn, registerBtn};
        for (JButton b : buttons) {
            b.setBackground(primary);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(buttonFont);
            b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(primaryDark);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(primary);
                }
            });
        }

        // Label
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        JLabel passLabel = new JLabel("Mật khẩu:");
        userLabel.setFont(labelFont);
        passLabel.setFont(labelFont);

        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setOpaque(false);
        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(loginBtn);
        formPanel.add(registerBtn);

        // Tiêu đề
        JLabel title = new JLabel("BÁN BẤT ĐỘNG SẢN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(primaryDark);

        JLabel subTitle = new JLabel("Đăng nhập / Đăng ký", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(new Color(127, 140, 141));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(title);
        header.add(subTitle);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(background);
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        getRootPane().setDefaultButton(loginBtn); // Enter = Đăng nhập

        // Sự kiện đăng nhập
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (userService.login(user, pass)) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                dispose();
                new HotelManagerApp().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        });

        // Sự kiện đăng ký
        registerBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống!");
                return;
            }
            if (userService.register(user, pass)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể đăng nhập ngay.");
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}