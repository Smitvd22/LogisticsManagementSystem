package LogisticsManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel() {
        initComponents();
    }

    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create title label
        JLabel titleLabel = new JLabel("Logistics Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        // Add main panel to center
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterDialog());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT user_id, username, user_type, email, phone FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String retrievedUsername = rs.getString("username");
                    String userType = rs.getString("user_type");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");

                    User user = new User(userId, retrievedUsername, userType, email, phone);

                    JOptionPane.showMessageDialog(this, "Login successful!");

                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.getContentPane().removeAll();

                    switch (userType.toLowerCase()) { // Ensure case-insensitive match
                    case "admin":
                        frame.add(new AdminPanel(user));
                        break;
                    case "client":
                        frame.add(new ClientPanel(user));
                        break;
                    case "warehouse":
                        frame.add(new WManagerPanel(user));
                        break;
                    case "driver":
                        frame.add(new DriverPanel(user));
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown user type: " + userType);
                }


                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage());
        }
    }

    private void showRegisterDialog() {
        // Check for the parent window and create a dialog
        JDialog dialog;
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (parentWindow instanceof JFrame) {
            dialog = new JDialog((JFrame) parentWindow, "Register", true);
        } else if (parentWindow instanceof JDialog) {
            dialog = new JDialog((JDialog) parentWindow, "Register", true);
        } else {
            // If no parent window is found, create a new JFrame as the parent
            dialog = new JDialog(new JFrame(), "Register", true);
        }

        // Set up the dialog
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        // Create registration panel
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

     // Add registration fields
        String[] labels = {"Username:", "Password:", "Confirm Password:", "Email:", "Phone:", "User Type:"};
        JTextField[] fields = new JTextField[5];
        
        Map<String, String> userTypeMap = new LinkedHashMap<>();
        userTypeMap.put("Client", "client");
        userTypeMap.put("Warehouse Manager", "warehouse");
        userTypeMap.put("Driver", "driver");

        JComboBox<String> userTypeDropdown = new JComboBox<>(userTypeMap.keySet().toArray(new String[0]));


        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            registerPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i == 1 || i == 2) { // Password fields
                fields[i] = new JPasswordField(20);
                registerPanel.add(fields[i], gbc);
            } else if (i == 5) { // User Type Dropdown
                registerPanel.add(userTypeDropdown, gbc);
            } else {
                fields[i] = new JTextField(20);
                registerPanel.add(fields[i], gbc);
            }
        }

        // Add submit button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        registerPanel.add(submitButton, gbc);

        // Action listener for submit button
        submitButton.addActionListener(e -> {
            String username = fields[0].getText();
            String password = new String(((JPasswordField) fields[1]).getPassword());
            String confirmPassword = new String(((JPasswordField) fields[2]).getPassword());
            String email = fields[3].getText();
            String phone = fields[4].getText();
            String userTypeFriendly = (String) userTypeDropdown.getSelectedItem();
            String userType = userTypeMap.get(userTypeFriendly); // Get the enum value

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || userType.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO users (username, password, email, phone, user_type) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.setString(3, email);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, userType); // Correctly mapped enum value

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Registration successful!");
                    dialog.dispose();
                }
            }

            catch (SQLException ex) {
                if (ex.getMessage().contains("Duplicate entry")) {
                    JOptionPane.showMessageDialog(dialog, "Username or email already exists!");
                } else {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Registration failed: " + ex.getMessage());
                }
            }
        });
;

        dialog.add(registerPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new LoginPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
