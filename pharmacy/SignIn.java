import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SignIn extends JFrame implements ActionListener {
    private Image backgroundImage;
    private JLabel userLabel, passLabel, emailLabel;
    private JTextField userTextField, emailTextField;
    private JPasswordField passTextField;
    private JButton signInButton, clearButton;
    private UserBST userBST;

    public SignIn() {
        // Load the background image (Ensure 'back.jpeg' is in the same directory or provide the correct path)
        backgroundImage = Toolkit.getDefaultToolkit().getImage("back.jpeg");

        // Create a panel to hold the components
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        setTitle("Pharmacy Management System - Sign In");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialize the UserBST and load users from file
        userBST = new UserBST();
        userBST.loadFromFile("users.txt");

        Font customFont = new Font("Arial", Font.BOLD, 16);

        // Initialize labels and text fields
        userLabel = new JLabel("Username:");
        userLabel.setFont(customFont);
        userTextField = new JTextField(30);
        
        passLabel = new JLabel("Password:");
        passLabel.setFont(customFont);
        passTextField = new JPasswordField(30);
        
        emailLabel = new JLabel("Email ID:");
        emailLabel.setFont(customFont);
        emailTextField = new JTextField(30);

        // Buttons
        signInButton = new JButton("Sign In");
        clearButton = new JButton("Clear");

        // Add components to the background panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(userLabel, gbc);
        gbc.gridx = 1; backgroundPanel.add(userTextField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        backgroundPanel.add(passLabel, gbc);
        gbc.gridx = 1; backgroundPanel.add(passTextField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        backgroundPanel.add(emailLabel, gbc);
        gbc.gridx = 1; backgroundPanel.add(emailTextField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(signInButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; backgroundPanel.add(clearButton, gbc);

        // Set button action listeners
        signInButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Close the window on exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the background panel to the frame
        add(backgroundPanel);
        setVisible(true);
    }

    // Custom panel to draw the background image
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signInButton) {
            String username = userTextField.getText().trim();
            String password = new String(passTextField.getPassword());
            String email = emailTextField.getText().trim();

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showMessage("Invalid email format.");
                return;
            }

            // Create a new user
            User newUser = new User(username, password, email);
            if (userBST.insert(newUser)) {
                userBST.saveToFile("users.txt");
                showMessage("Successfully registered!");

                // Navigate to Login screen after successful sign-in
                new Login();  // Open the Login screen
                dispose();    // Close the SignIn screen

            } else {
                showMessage("Email already registered.");
            }

        } else if (ae.getSource() == clearButton) {
            userTextField.setText("");
            passTextField.setText("");
            emailTextField.setText("");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignIn::new);
    }
}
