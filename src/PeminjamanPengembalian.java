import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PeminjamanPengembalian extends JFrame {
    private JTextField txtIdBuku, txtIdAnggota, txtTanggalPeminjaman, txtTanggalPengembalian;
    private JButton btnPinjam, btnKembalikan, btnRiwayat;

    public PeminjamanPengembalian() {
        setTitle("Peminjaman dan Pengembalian Buku");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel("ID Buku:"), gbc);
        txtIdBuku = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtIdBuku, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID Anggota:"), gbc);
        txtIdAnggota = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtIdAnggota, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tanggal Peminjaman (YYYY-MM-DD):"), gbc);
        txtTanggalPeminjaman = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20);
        gbc.gridx = 1;
        panel.add(txtTanggalPeminjaman, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Tanggal Pengembalian (YYYY-MM-DD):"), gbc);
        txtTanggalPengembalian = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtTanggalPengembalian, gbc);

        btnPinjam = createStyledButton("Pinjam");
        btnKembalikan = createStyledButton("Kembalikan");
        btnRiwayat = createStyledButton("Riwayat Peminjaman");

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(btnPinjam, gbc);
        gbc.gridx = 1;
        panel.add(btnKembalikan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(btnRiwayat, gbc);

        btnPinjam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pinjamBuku();
            }
        });

        btnKembalikan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kembalikanBuku();
            }
        });

        btnRiwayat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lihatRiwayat();
            }
        });

        add(panel, BorderLayout.CENTER);

        // Set background color for the panel
        panel.setBackground(Color.decode("#55C1FF"));

        // Set font for labels and buttons
        Font font = new Font("Arial", Font.PLAIN, 14);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel || component instanceof JButton) {
                component.setFont(font);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(Color.decode("#507FF3"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 90, 150)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return button;
    }

    private void pinjamBuku() {
        int idBuku = Integer.parseInt(txtIdBuku.getText());
        int idAnggota = Integer.parseInt(txtIdAnggota.getText());
        String tanggalPeminjaman = txtTanggalPeminjaman.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO peminjaman (id_buku, id_anggota, tanggal_peminjaman) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idBuku);
            stmt.setInt(2, idAnggota);
            stmt.setString(3, tanggalPeminjaman);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void kembalikanBuku() {
        int idBuku = Integer.parseInt(txtIdBuku.getText());
        int idAnggota = Integer.parseInt(txtIdAnggota.getText());
        String tanggalPengembalian = txtTanggalPengembalian.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE peminjaman SET tanggal_pengembalian = ? WHERE id_buku = ? AND id_anggota = ? AND tanggal_pengembalian IS NULL";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tanggalPengembalian);
            stmt.setInt(2, idBuku);
            stmt.setInt(3, idAnggota);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void lihatRiwayat() {
        int idAnggota = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Anggota:"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT p.*, b.judul FROM peminjaman p JOIN buku b ON p.id_buku = b.id WHERE p.id_anggota = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idAnggota);
            ResultSet rs = stmt.executeQuery();

            StringBuilder hasil = new StringBuilder();
            while (rs.next()) {
                hasil.append("Judul Buku: ").append(rs.getString("judul")).append("\n");
                hasil.append("Tanggal Peminjaman: ").append(rs.getString("tanggal_peminjaman")).append("\n");
                hasil.append("Tanggal Pengembalian: ").append(rs.getString("tanggal_pengembalian") != null ? rs.getString("tanggal_pengembalian") : "Belum dikembalikan").append("\n");
                hasil.append("=================================\n");
            }

            JOptionPane.showMessageDialog(this, hasil.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PeminjamanPengembalian().setVisible(true);
            }
        });
    }
}
