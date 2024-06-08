import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Sistem Manajemen Perpustakaan");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Sistem Manajemen Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnManajemenBuku = createStyledButton("Manajemen Buku");
        JButton btnManajemenAnggota = createStyledButton("Manajemen Anggota");
        JButton btnPeminjamanPengembalian = createStyledButton("Sistem Peminjaman dan Pengembalian Buku");
        JButton btnPencarianBuku = createStyledButton("Pencarian Buku");

        btnManajemenBuku.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManajemenBuku().setVisible(true);
            }
        });

        btnManajemenAnggota.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManajemenAnggota().setVisible(true);
            }
        });

        btnPeminjamanPengembalian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PeminjamanPengembalian().setVisible(true);
            }
        });

        btnPencarianBuku.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PencarianBuku().setVisible(true);
            }
        });

        buttonPanel.add(btnManajemenBuku);
        buttonPanel.add(btnManajemenAnggota);
        buttonPanel.add(btnPeminjamanPengembalian);
        buttonPanel.add(btnPencarianBuku);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.setBackground(Color.decode("#55C1FF")); // Set background color for main panel
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(Color.decode("#507FF3")); // Set background color for buttons
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 90, 150)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}
