import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PencarianBuku extends JFrame {
    private JTextField txtCari;
    private JButton btnCari;
    private JTextArea txtHasil;

    public PencarianBuku() {
        setTitle("Pencarian Buku");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel("Kata Kunci:"), gbc);
        txtCari = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtCari, gbc);

        btnCari = createStyledButton("Cari");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnCari, gbc);

        txtHasil = new JTextArea(10, 40);
        txtHasil.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtHasil);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        btnCari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cariBuku();
            }
        });

        add(panel, BorderLayout.CENTER);

        // Set background color for the panel
        panel.setBackground(Color.decode("#55C1FF"));

        // Set font for labels, buttons, and text area
        Font font = new Font("Arial", Font.PLAIN, 14);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel || component instanceof JButton || component instanceof JTextArea) {
                component.setFont(font);
            }
        }
        txtHasil.setFont(font);
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

    private void cariBuku() {
        String keyword = txtCari.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM buku WHERE judul LIKE ? OR penulis LIKE ? OR kategori LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            StringBuilder hasil = new StringBuilder();
            boolean adaHasil = false;
            while (rs.next()) {
                adaHasil = true;
                hasil.append("Judul: ").append(rs.getString("judul")).append("\n");
                hasil.append("Penulis: ").append(rs.getString("penulis")).append("\n");
                hasil.append("Penerbit: ").append(rs.getString("penerbit")).append("\n");
                hasil.append("Tahun Terbit: ").append(rs.getInt("tahun_terbit")).append("\n");
                hasil.append("Kategori: ").append(rs.getString("kategori")).append("\n");
                hasil.append("Stok: ").append(rs.getInt("stok")).append("\n");
                hasil.append("=================================\n");
            }

            if (adaHasil) {
                txtHasil.setText(hasil.toString());
            } else {
                txtHasil.setText("Buku Tidak Ditemukan");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PencarianBuku().setVisible(true);
            }
        });
    }
}
