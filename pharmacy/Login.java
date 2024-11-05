import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Login extends Frame implements ActionListener {
    // GUI components
    Label userLabel, passLabel;
    TextField userTextField, passTextField;
    Button loginButton, clearButton;
    private Image backgroundImage;

    public Login() {
        // Load the background image (ensure the path is correct)
        backgroundImage = Toolkit.getDefaultToolkit().getImage("backlogin.jpg");

        // Create a panel for the background
        BackgroundPanel backgroundPanel = new BackgroundPanel();

        // Set layout and title
        backgroundPanel.setLayout(new GridBagLayout());
        setTitle("Pharmacy Management System - Login");
        setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Full screen
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximize window

        // Set custom font
        Font customFont = new Font("Arial", Font.BOLD, 16);

        // Username and password labels
        userLabel = new Label("Username:");
        userLabel.setFont(customFont);
        userTextField = new TextField(20); // Starts empty
        passLabel = new Label("Password:");
        passLabel.setFont(customFont);
        passTextField = new TextField(20); // Starts empty
        passTextField.setEchoChar('*');  // Hide password input

        // Login and Clear buttons
        loginButton = new Button("Login");
        clearButton = new Button("Clear");

        // Apply hover effects
        addHoverEffect(loginButton);
        addHoverEffect(clearButton);

        // Add components to background panel with GridBagLayout for better alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Padding around components

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(userLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(userTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(passTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(loginButton, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(clearButton, gbc);

        // Set button action listeners
        loginButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Set window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        // Add the background panel to the frame
        add(backgroundPanel);
        setVisible(true);
    }

    private void addHoverEffect(Button button) {
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); // Steel blue on hover
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237)); // Reset to original color
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        if (action.equals("Login")) {
            String username = userTextField.getText().trim();
            String password = passTextField.getText().trim();

            // Validate credentials against users.txt
            if (validateCredentials(username, password)) {
                // Show success message in a dialog
                JOptionPane.showMessageDialog(this, "Login successful! Welcome.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new PharmacyManagement(username); // Open PharmacyManagement page (implement as needed)
                this.dispose(); // Close login window
            } else {
                // Show error message in a dialog
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (action.equals("Clear")) {
            userTextField.setText("");
            passTextField.setText("");
        }
    }

    // Method to validate user credentials from users.txt
    private boolean validateCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split on comma
                if (parts.length >= 2) { // Ensure there are at least username and password
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    if (fileUser.equals(username) && filePass.equals(password)) {
                        return true; // Valid credentials
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception
        }
        return false; // Invalid credentials
    }

    // Custom panel to draw the background image
    private class BackgroundPanel extends Panel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
