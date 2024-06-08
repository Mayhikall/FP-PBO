import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Adding Username label and text field
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Adding Password label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Adding Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.decode("#507FF3"));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        panel.add(loginButton, gbc);

        // Adding Register button
        gbc.gridx = 1;
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.decode("#507FF3"));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Register().setVisible(true);
            }
        });
        panel.add(registerButton, gbc);

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

        add(panel, BorderLayout.CENTER);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "")) {
            String query = "SELECT * FROM pengguna WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                new MainMenu().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login Failed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
