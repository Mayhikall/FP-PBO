import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManajemenAnggota extends JFrame {
    private JTextField txtNama, txtAlamat, txtNoTelepon, txtEmail, txtIdAnggota;
    private JButton btnTambah, btnEdit, btnHapus, btnCari;

    public ManajemenAnggota() {
        setTitle("Manajemen Anggota");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel("Nama:"), gbc);
        txtNama = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Alamat:"), gbc);
        txtAlamat = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtAlamat, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("No. Telepon:"), gbc);
        txtNoTelepon = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNoTelepon, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("ID Anggota:"), gbc);
        txtIdAnggota = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtIdAnggota, gbc);

        btnTambah = createStyledButton("Tambah");
        btnEdit = createStyledButton("Edit");
        btnHapus = createStyledButton("Hapus");
        btnCari = createStyledButton("Cari");

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(btnTambah, gbc);
        gbc.gridx = 1;
        panel.add(btnEdit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(btnHapus, gbc);
        gbc.gridx = 1;
        panel.add(btnCari, gbc);

        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahAnggota();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editAnggota();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusAnggota();
            }
        });

        btnCari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cariAnggota();
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

    private void tambahAnggota() {
        String nama = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String noTelepon = txtNoTelepon.getText().trim();
        String email = txtEmail.getText().trim();
        String idAnggota = txtIdAnggota.getText().trim();

        // Validate that none of the input fields are empty
        if (nama.isEmpty() || alamat.isEmpty() || noTelepon.isEmpty() || email.isEmpty() || idAnggota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO anggota (nama, alamat, no_telepon, email, id_anggota) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, noTelepon);
            stmt.setString(4, email);
            stmt.setString(5, idAnggota);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Anggota berhasil ditambahkan.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void editAnggota() {
        int idAnggota = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Anggota yang ingin diedit:"));
        String nama = txtNama.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String noTelepon = txtNoTelepon.getText().trim();
        String email = txtEmail.getText().trim();
        String id_anggota = txtIdAnggota.getText().trim();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE anggota SET nama = ?, alamat = ?, no_telepon = ?, email = ?, id_anggota = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, noTelepon);
            stmt.setString(4, email);
            stmt.setString(5, id_anggota);
            stmt.setInt(6, idAnggota);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Anggota berhasil diupdate.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hapusAnggota() {
        int idAnggota = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Anggota yang ingin dihapus:"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM anggota WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idAnggota);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Anggota berhasil dihapus.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cariAnggota() {
        int idAnggota = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan ID Anggota yang ingin dicari:"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM anggota WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idAnggota);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder hasil = new StringBuilder();
                hasil.append("ID: ").append(rs.getInt("id")).append("\n");
                hasil.append("Nama: ").append(rs.getString("nama")).append("\n");
                hasil.append("Alamat: ").append(rs.getString("alamat")).append("\n");
                hasil.append("No. Telepon: ").append(rs.getString("no_telepon")).append("\n");
                hasil.append("Email: ").append(rs.getString("email")).append("\n");
                hasil.append("ID Anggota: ").append(rs.getString("id_anggota")).append("\n");

                JOptionPane.showMessageDialog(this, hasil.toString(), "Detail Anggota", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Anggota tidak ditemukan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManajemenAnggota().setVisible(true);
            }
        });
    }
}
