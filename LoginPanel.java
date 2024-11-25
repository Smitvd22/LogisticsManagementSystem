package LogisticsManagementSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class LoginPanel extends JPanel {
    // UI Components
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;
    private GradientPanel mainPanel;
    private JLabel logoLabel;
    
    // UI Colors
    private static final Color DARK_THEME = new Color(18, 18, 18);
    private static final Color ACCENT_COLOR = new Color(255, 255, 255);
    private static final Color BLACK_FONT = new Color(0,0,0);
    private static final Color PURPLE_ACCENT = new Color(138, 43, 226);
    private static final Color BACKGROUND_COLOR = new Color(13, 17, 23);
    private static final Color CARD_BACKGROUND = new Color(22, 27, 34);
    private static final Color TEXT_COLOR = new Color(201, 209, 217);
    private static final Color ACCENT_COLOR2 = new Color(136, 46, 224);
    private static final Color HOVER_COLOR = new Color(48, 54, 61);
    
    // Email configuration
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String EMAIL_USERNAME = "randomidiot075@gmail.com";
    private static final String EMAIL_PASSWORD = "howcanthispasswordbesostrong";

    // Logo configuration
    private ImageIcon logo;
    private static final int LOGO_WIDTH = 150;
    private static final int LOGO_HEIGHT = 150;

    public LoginPanel() {
        initComponents();
        loadLogo("C:/Users/rudra/OneDrive/Desktop/Stuff/WebD Stuff/Logo/codeconqueror-high-resolution-logo-transparent.png"); // Set default logo path
    }

    public void loadLogo(String path) {
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
            logo = new ImageIcon(scaledImage);
            logoLabel.setIcon(logo);
        } catch (Exception e) {
            // If logo loading fails, use text as fallback
            logoLabel.setText("gratafy");
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(DARK_THEME);

        mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        createModernComponents();
        layoutComponents();
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createModernComponents() {
        // Logo
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(ACCENT_COLOR);
        
        // Email field with placeholder
        emailField = new JRoundTextField(20, "Enter your email");
        emailField.setBackground(new Color(30, 30, 30));
        emailField.setForeground(ACCENT_COLOR);
        emailField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        emailField.setCaretColor(ACCENT_COLOR);
        
        // Password field with placeholder
        passwordField = new JRoundPasswordField(20, "Enter your password");
        passwordField.setBackground(new Color(30, 30, 30));
        passwordField.setForeground(ACCENT_COLOR);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        passwordField.setCaretColor(ACCENT_COLOR);
        
        // Login button
        loginButton = new JRoundButton("LOG IN");
        loginButton.setBackground(PURPLE_ACCENT);
        loginButton.setForeground(ACCENT_COLOR);
        loginButton.setFocusPainted(false);
        
        // Register button
        registerButton = new JRoundButton("REGISTER");
        registerButton.setBackground(new Color(30, 30, 30));
        registerButton.setForeground(ACCENT_COLOR);
        registerButton.setFocusPainted(false);
        
        // Forgot password button
        forgotPasswordButton = new JButton("FORGOT YOUR PASSWORD?");
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(new Color(138, 43, 226));
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterDialog());
        forgotPasswordButton.addActionListener(e -> showForgotPasswordDialog());
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 30, 5, 30);
        
        // Logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(logoLabel, gbc);
        
        // Email field
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 30, 5, 30);
        mainPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 30, 5, 30);
        mainPanel.add(loginButton, gbc);
        
        // Register button
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 30, 5, 30);
        mainPanel.add(registerButton, gbc);
        
        // Forgot password button
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 30, 5, 30);
        mainPanel.add(forgotPasswordButton, gbc);
    }

    // Enhanced custom components with placeholder support
    private static class JRoundTextField extends JTextField {
        private String placeholder;
        private boolean showingPlaceholder;

        public JRoundTextField(int columns, String placeholder) {
            super(columns);
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            setOpaque(false);
            setText(placeholder);
            setForeground(Color.GRAY);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(ACCENT_COLOR);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class JRoundPasswordField extends JPasswordField {
        private String placeholder;
        private boolean showingPlaceholder;

        public JRoundPasswordField(int columns, String placeholder) {
            super(columns);
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            setOpaque(false);
            setEchoChar((char) 0);
            setText(placeholder);
            setForeground(Color.GRAY);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (showingPlaceholder) {
                        setText("");
                        setEchoChar('•');
                        setForeground(ACCENT_COLOR);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (String.valueOf(getPassword()).isEmpty()) {
                        setEchoChar((char) 0);
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Implement the showRegisterDialog method
    private void showRegisterDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Register New User", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(CARD_BACKGROUND);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Field labels and text fields
        String[] labels = {"Username:", "Password:", "Confirm Password:", "Email:", "Phone:", "User Type:"};
        JTextField[] fields = new JTextField[5];
        
        Map<String, String> userTypeMap = new LinkedHashMap<>();
        userTypeMap.put("Client", "client");
        userTypeMap.put("Warehouse Manager", "warehouse");
        userTypeMap.put("Driver", "driver");

        JComboBox<String> userTypeDropdown = new JComboBox<>(userTypeMap.keySet().toArray(new String[0]));

        // Add fields to panel
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            
            JLabel label = new JLabel(labels[i]);
            label.setForeground(TEXT_COLOR);
            registerPanel.add(label, gbc);

            gbc.gridx = 1;
            if (i == 1 || i == 2) { // Password fields
                fields[i] = new JPasswordField(20);
            } else if (i == 5) { // User Type Dropdown
                registerPanel.add(userTypeDropdown, gbc);
                continue;
            } else {
                fields[i] = new JTextField(20);
            }
            fields[i].setBackground(BACKGROUND_COLOR);
            fields[i].setForeground(TEXT_COLOR);
            fields[i].setBorder(BorderFactory.createLineBorder(ACCENT_COLOR2));
            registerPanel.add(fields[i], gbc);
        }

        // Submit button
        JButton submitButton = new JButton("Register");
        submitButton.setBackground(ACCENT_COLOR2);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        registerPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String username = fields[0].getText();
            String password = new String(((JPasswordField) fields[1]).getPassword());
            String confirmPassword = new String(((JPasswordField) fields[2]).getPassword());
            String email = fields[3].getText();
            String phone = fields[4].getText();
            String userTypeFriendly = (String) userTypeDropdown.getSelectedItem();
            String userType = userTypeMap.get(userTypeFriendly);

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
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
                    pstmt.setString(5, userType);

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Registration successful!");
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                if (ex.getMessage().contains("Duplicate entry")) {
                    JOptionPane.showMessageDialog(dialog, "Username or email already exists!");
                } else {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Registration failed: " + ex.getMessage());
                }
            }
        });

        dialog.add(registerPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    // Rest of the existing methods remain the same...
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_type"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    handleSuccessfulLogin(user);
                } else {
                    showError("Invalid email or password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Login failed: " + e.getMessage());
        }
    }

    private void showForgotPasswordDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Reset Password", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField emailInput = new JRoundTextField(20 , "Enter Email");
        JButton submitButton = new JRoundButton("Send Reset Link");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Enter your email:"), gbc);
        
        gbc.gridy = 1;
        panel.add(emailInput, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(submitButton, gbc);
        
        submitButton.addActionListener(e -> {
            String email = emailInput.getText();
            if (isValidEmail(email)) {
                sendPasswordResetEmail(email);
                dialog.dispose();
            } else {
                showError("Please enter a valid email address");
            }
        });
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void sendPasswordResetEmail(String email) {
        // Generate reset token
        String resetToken = generateResetToken();
        
        // Store token in database
        storeResetToken(email, resetToken);
        
        // Send email
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EMAIL_HOST);
        props.put("mail.smtp.port", EMAIL_PORT);

        jakarta.mail.Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset Request");
            message.setText("Your password reset token is: " + resetToken);

            Transport.send(message);
            
            JOptionPane.showMessageDialog(this, 
                "Password reset instructions have been sent to your email.",
                "Reset Email Sent",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (MessagingException e) {
            e.printStackTrace();
            showError("Failed to send reset email: " + e.getMessage());
        }
    }
    
    private static class JRoundButton extends JButton {
        public JRoundButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int w = getWidth();
            int h = getHeight();
            
            Color color1 = DARK_THEME;
            Color color2 = new Color(40, 40, 48);
            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    // Utility methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private String generateResetToken() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private void storeResetToken(String email, String token) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE users SET reset_token = ?, reset_token_expiry = ? WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, token);
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 3600000)); // 1 hour expiry
                pstmt.setString(3, email);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to process reset request: " + e.getMessage());
        }
    }

    private void handleSuccessfulLogin(User user) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        
        switch (user.getUserType().toLowerCase()) {
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
                showError("Unknown user type: " + user.getUserType());
                return;
        }
        
        frame.revalidate();
        frame.repaint();
    }

}