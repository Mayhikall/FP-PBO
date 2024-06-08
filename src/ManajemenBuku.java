import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManajemenBuku extends JFrame {
    private JTextField txtJudul, txtPenulis, txtPenerbit, txtTahunTerbit, txtKategori, txtStok;
    private JButton btnTambah, btnEdit, btnHapus, btnCari;

    public ManajemenBuku() {
        setTitle("Manajemen Buku");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel("Judul:"), gbc);
        txtJudul = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtJudul, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Penulis:"), gbc);
        txtPenulis = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtPenulis, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Penerbit:"), gbc);
        txtPenerbit = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtPenerbit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Tahun Terbit:"), gbc);
        txtTahunTerbit = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtTahunTerbit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Kategori:"), gbc);
        txtKategori = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtKategori, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Stok:"), gbc);
        txtStok = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtStok, gbc);

        btnTambah = createStyledButton("Tambah");
        btnEdit = createStyledButton("Edit");
        btnHapus = createStyledButton("Hapus");
        btnCari = createStyledButton("Cari");

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(btnTambah, gbc);
        gbc.gridx = 1;
        panel.add(btnEdit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(btnHapus, gbc);
        gbc.gridx = 1;
        panel.add(btnCari, gbc);

        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahBuku();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editBuku();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusBuku();
            }
        });

        btnCari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cariBuku();
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

    private void tambahBuku() {
    String judul = txtJudul.getText().trim();
    String penulis = txtPenulis.getText().trim();
    String penerbit = txtPenerbit.getText().trim();
    String tahunTerbitStr = txtTahunTerbit.getText().trim();
    String kategori = txtKategori.getText().trim();
    String stokStr = txtStok.getText().trim();

    // Validate that none of the input fields are empty
    if (judul.isEmpty() || penulis.isEmpty() || penerbit.isEmpty() || tahunTerbitStr.isEmpty() || kategori.isEmpty() || stokStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int tahunTerbit;
    int stok;
    try {
        tahunTerbit = Integer.parseInt(tahunTerbitStr);
        stok = Integer.parseInt(stokStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Tahun Terbit dan Stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection conn = DatabaseConnection.getConnection()) {
        String query = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, kategori, stok) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, judul);
        stmt.setString(2, penulis);
        stmt.setString(3, penerbit);
        stmt.setInt(4, tahunTerbit);
        stmt.setString(5, kategori);
        stmt.setInt(6, stok);
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan.");
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    private void editBuku() {
        int idBuku = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Buku yang ingin diedit:"));
        String judul = txtJudul.getText();
        String penulis = txtPenulis.getText();
        String penerbit = txtPenerbit.getText();
        int tahunTerbit = Integer.parseInt(txtTahunTerbit.getText());
        String kategori = txtKategori.getText();
        int stok = Integer.parseInt(txtStok.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, tahun_terbit = ?, kategori = ?, stok = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, judul);
            stmt.setString(2, penulis);
            stmt.setString(3, penerbit);
            stmt.setInt(4, tahunTerbit);
            stmt.setString(5, kategori);
            stmt.setInt(6, stok);
            stmt.setInt(7, idBuku);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Buku berhasil diupdate.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hapusBuku() {
        int idBuku = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Buku yang ingin dihapus:"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM buku WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idBuku);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Buku berhasil dihapus.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cariBuku() {
        int idBuku = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Buku yang ingin dicari:"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM buku WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idBuku);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder hasil = new StringBuilder();
                hasil.append("ID: ").append(rs.getInt("id")).append("\n");
                hasil.append("Judul: ").append(rs.getString("judul")).append("\n");
                hasil.append("Penulis: ").append(rs.getString("penulis")).append("\n");
                hasil.append("Penerbit: ").append(rs.getString("penerbit")).append("\n");
                hasil.append("Tahun Terbit: ").append(rs.getInt("tahun_terbit")).append("\n");
                hasil.append("Kategori: ").append(rs.getString("kategori")).append("\n");
                hasil.append("Stok: ").append(rs.getInt("stok")).append("\n");

                JOptionPane.showMessageDialog(this, hasil.toString(), "Detail Buku", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Buku tidak ditemukan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManajemenBuku().setVisible(true);
            }
        });
    }
}
