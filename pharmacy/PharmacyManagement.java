import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class PharmacyManagement extends JFrame implements ActionListener {
    // Labels and fields for pharmacist and user interaction
    JLabel searchLabel, medicineLabel, totalPriceLabel;
    JTextField searchField;
    JComboBox<String> medicineChoice;
    JButton searchButton, addButton, removeButton, submitButton, clearButton, sortButton, exitButton;
    JTextArea cartTextArea, medicineInfoTextArea;
    JScrollPane cartScrollPane, medicineInfoScrollPane;
    double totalPrice = 0;

    // HashMap to store the medicines
    Map<String, Medicine> medicines = new HashMap<>();
    private boolean isFullScreen = false;

    public PharmacyManagement(String username) {
        // UI Setup for pharmacists and customers
        setTitle("Pharmacy Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Set background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0, 0, 900, 700);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(new Color(220, 240, 255)); // Light blue background

        // Title Label with custom font
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>Pharmacy Management System<br>Welcome, " + username + "</div></html>", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20)); // Adjusted font size
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setBounds(0, 20, 900, 50); // Adjusted bounds to accommodate both lines
        backgroundPanel.add(titleLabel);

        // Search bar for medicine lookup
        searchLabel = new JLabel("Search Medicine:");
        searchLabel.setBounds(50, 80, 150, 25);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backgroundPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(200, 80, 200, 25);
        backgroundPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(420, 80, 100, 25);
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createRaisedBevelBorder());
        searchButton.addActionListener(this);
        backgroundPanel.add(searchButton);

        // Dropdown for selecting medicine
        medicineLabel = new JLabel("Medicine:");
        medicineLabel.setBounds(50, 130, 150, 25);
        medicineLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backgroundPanel.add(medicineLabel);

        medicineChoice = new JComboBox<>();
        medicineChoice.setBounds(200, 130, 200, 25);
        backgroundPanel.add(medicineChoice);

        // Text area for cart details with scroll pane
        cartTextArea = new JTextArea();
        cartTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        cartTextArea.setEditable(false);
        cartScrollPane = new JScrollPane(cartTextArea);
        cartScrollPane.setBounds(450, 130, 400, 200);
        cartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        backgroundPanel.add(cartScrollPane);

        // Buttons for adding, removing medicines and sorting
        addButton = new JButton("Add Medicine");
        addButton.setBounds(50, 180, 150, 30);
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(this);
        backgroundPanel.add(addButton);

        removeButton = new JButton("Remove Medicine");
        removeButton.setBounds(220, 180, 150, 30);
        removeButton.setBackground(new Color(178, 34, 34));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(this);
        backgroundPanel.add(removeButton);

        sortButton = new JButton("Sort Medicines");
        sortButton.setBounds(50, 230, 150, 30);
        sortButton.setBackground(new Color(70, 130, 180));
        sortButton.setForeground(Color.WHITE);
        sortButton.setFocusPainted(false);
        sortButton.addActionListener(this);
        backgroundPanel.add(sortButton);

        // Total price label
        totalPriceLabel = new JLabel("Total Price: $0");
        totalPriceLabel.setBounds(50, 280, 150, 25);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backgroundPanel.add(totalPriceLabel);

        submitButton = new JButton("Submit");
        submitButton.setBounds(50, 320, 150, 30);
        submitButton.setBackground(new Color(0, 128, 128));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(this);
        backgroundPanel.add(submitButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(220, 320, 150, 30);
        clearButton.setBackground(new Color(255, 140, 0));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(this);
        backgroundPanel.add(clearButton);

        // Text area for showing searched medicine information with scroll pane
        medicineInfoTextArea = new JTextArea();
        medicineInfoTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        medicineInfoTextArea.setEditable(false);
        medicineInfoScrollPane = new JScrollPane(medicineInfoTextArea);
        medicineInfoScrollPane.setBounds(450, 350, 400, 250);
        medicineInfoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        medicineInfoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        backgroundPanel.add(medicineInfoScrollPane);

        // Footer Panel with an exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(400, 620, 100, 30);
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        backgroundPanel.add(exitButton);

        add(backgroundPanel);
        setVisible(true);

        // Load initial medicines
        preloadMedicines();

        // Add Key Listener for full-screen toggle
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullScreen();
                }
            }
        });
    }

    private void preloadMedicines() {
        String[][] data = {
            { "Paracetamol", "Pharma Inc.", "100", "2025-12-31", "10" },
            { "Aspirin", "HealthCorp", "50", "2024-11-01", "20" },
            { "Antacid", "Remedy Ltd.", "75", "2026-01-15", "15" },
            { "Crocin", "Medicorp", "60", "2025-09-01", "30" },
            { "Ibuprofen", "HealthCo", "80", "2025-12-31", "24" },
            { "Amoxicillin", "MediPharm", "120", "2026-03-15", "40" },
            { "Cetirizine", "PharmaXYZ", "90", "2025-08-30", "12" }
        };
        for (String[] medicineData : data) {
            Medicine medicine = new Medicine(medicineData[0], medicineData[1], Integer.parseInt(medicineData[2]),
                    medicineData[3], Double.parseDouble(medicineData[4]));
            medicines.put(medicine.getName(), medicine);
            medicineChoice.addItem(medicine.getName());
        }
    }

    private void toggleFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (!isFullScreen) {
            dispose(); // Dispose of the current window
            setUndecorated(true); // Remove window decorations
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
            setVisible(true);
            gd.setFullScreenWindow(this); // Set to full screen
            isFullScreen = true;
        } else {
            gd.setFullScreenWindow(null); // Exit full screen
            setUndecorated(false); // Add window decorations back
            setVisible(false);
            setSize(900, 700); // Set size back to original
            setLocationRelativeTo(null); // Center on screen
            setVisible(true);
            isFullScreen = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String searchQuery = searchField.getText();
            Medicine foundMedicine = medicines.get(searchQuery);
            if (foundMedicine != null) {
                medicineInfoTextArea.setText(foundMedicine.toString());
            } else {
                medicineInfoTextArea.setText("Medicine not found.");
            }
        } else if (e.getSource() == addButton) {
            String selectedMedicineName = (String) medicineChoice.getSelectedItem();
            Medicine selectedMedicine = medicines.get(selectedMedicineName);
            if (selectedMedicine != null) {
                cartTextArea.append(selectedMedicine.getName() + "\n");
                totalPrice += selectedMedicine.getPrice();
                totalPriceLabel.setText("Total Price: $" + totalPrice);
            }
        } else if (e.getSource() == removeButton) {
            String selectedMedicineName = (String) medicineChoice.getSelectedItem();
            Medicine selectedMedicine = medicines.get(selectedMedicineName);
            if (selectedMedicine != null) {
                cartTextArea.setText(cartTextArea.getText().replace(selectedMedicine.getName() + "\n", ""));
                totalPrice -= selectedMedicine.getPrice();
                totalPriceLabel.setText("Total Price: $" + totalPrice);
            }
        } else if (e.getSource() == sortButton) {
            String criteria = JOptionPane.showInputDialog("Sort by (expiry/stock):");
            if (criteria != null) {
                if (criteria.equalsIgnoreCase("expiry")) {
                    sortByExpiryDate();
                } else if (criteria.equalsIgnoreCase("stock")) {
                    sortByStockQuantity();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid criteria!");
                }
            }
        } else if (e.getSource() == submitButton) {
            JOptionPane.showMessageDialog(this, "Total Price: $" + totalPrice + "\nThank you for your purchase!");
            cartTextArea.setText("");
            totalPrice = 0;
            totalPriceLabel.setText("Total Price: $0");
        } else if (e.getSource() == clearButton) {
            cartTextArea.setText("");
            totalPrice = 0;
            totalPriceLabel.setText("Total Price: $0");
            medicineInfoTextArea.setText("");
        }
    }

    private void sortByExpiryDate() {
        List<Medicine> medicineList = new ArrayList<>(medicines.values());
        quickSort(medicineList, "expiry");
        updateMedicineChoice(medicineList);
    }

    private void sortByStockQuantity() {
        List<Medicine> medicineList = new ArrayList<>(medicines.values());
        quickSort(medicineList, "stock");
        updateMedicineChoice(medicineList);
    }

    private void quickSort(List<Medicine> list, String criteria) {
        quickSort(list, 0, list.size() - 1, criteria);
    }

    private void quickSort(List<Medicine> list, int low, int high, String criteria) {
        if (low < high) {
            int pi = partition(list, low, high, criteria);
            quickSort(list, low, pi - 1, criteria);
            quickSort(list, pi + 1, high, criteria);
        }
    }

    private int partition(List<Medicine> list, int low, int high, String criteria) {
        Medicine pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = criteria.equals("expiry") ? 
                list.get(j).getExpiryDate().compareTo(pivot.getExpiryDate()) < 0 : 
                list.get(j).getStockQuantity() < pivot.getStockQuantity();

            if (condition) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    private void updateMedicineChoice(List<Medicine> sortedMedicines) {
        medicineChoice.removeAllItems();
        for (Medicine medicine : sortedMedicines) {
            medicineChoice.addItem(medicine.getName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PharmacyManagement("User"));
    }

    static class Medicine {
        private String name;
        private String manufacturer;
        private int stockQuantity;
        private String expiryDate; // format: YYYY-MM-DD
        private double price;

        public Medicine(String name, String manufacturer, int stockQuantity, String expiryDate, double price) {
            this.name = name;
            this.manufacturer = manufacturer;
            this.stockQuantity = stockQuantity;
            this.expiryDate = expiryDate;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Name: " + name + "\nManufacturer: " + manufacturer +
                   "\nStock Quantity: " + stockQuantity +
                   "\nExpiry Date: " + expiryDate +
                   "\nPrice: $" + price;
        }
    }
}
