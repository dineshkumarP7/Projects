import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Billing extends JFrame implements ActionListener {
    private JTextField quantityField, priceField;
    private JButton addButton, calculateButton, paymentButton, deleteButton, logoutButton;
    private JTextArea billTextArea;
    private double totalCost = 0;
    private JTable fruitTable;
    private DefaultTableModel fruitTableModel;
    private JLabel currentDateTimeLabel;
    private JComboBox<String> quantityUnitComboBox;
    private String[] fruitsFromDatabase;

    public Billing() {
        setTitle("Fruit Shop Billing");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize fruits data from database
        initializeFruitsFromDatabase();

        // Header Panel with Current DateTime and Logout Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.GRAY);

        JLabel headerLabel = new JLabel("Billing", JLabel.CENTER);
        headerLabel.setFont(new Font("CAMBRIA", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Time & Date
        currentDateTimeLabel = new JLabel();
        headerPanel.add(currentDateTimeLabel, BorderLayout.EAST);
        updateDateTime();
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();

        // Footer Panel with Logout Button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.GRAY);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        footerPanel.add(logoutButton, BorderLayout.EAST);

        JLabel copyrightLabel = new JLabel("2024 ©  DK Software Pvt. Ltd.TM", JLabel.CENTER);
        copyrightLabel.setForeground(Color.WHITE);
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(20, 180, 80, 20);
        quantityField = new JTextField();
        quantityField.setBounds(120, 180, 100, 20);

        JLabel quantityUnitLabel = new JLabel("Unit:");
        quantityUnitLabel.setBounds(240, 180, 80, 20);
        quantityUnitComboBox = new JComboBox<>(new String[]{"kilograms", "grams"});
        quantityUnitComboBox.setBounds(280, 180, 100, 20);

        JLabel priceLabel = new JLabel("Price per unit:");
        priceLabel.setBounds(20, 200, 100, 20);
        priceField = new JTextField();
        priceField.setBounds(120, 200, 100, 20);
        priceField.setEditable(false);

        addButton = new JButton("Add to Cart");
        addButton.setBounds(20, 250, 120, 25);
        addButton.addActionListener(this);

        calculateButton = new JButton("Calculate Total");
        calculateButton.setBounds(150, 250, 150, 25);
        calculateButton.addActionListener(this);

        paymentButton = new JButton("Payment");
        paymentButton.setBounds(320, 250, 100, 25);
        paymentButton.addActionListener(this);

        deleteButton = new JButton("Delete All");
        deleteButton.setBounds(20, 450, 120, 25);
        deleteButton.addActionListener(this);

        billTextArea = new JTextArea();
        JScrollPane billingScrollPane = new JScrollPane(billTextArea);
        billingScrollPane.setBounds(20, 300, 300, 150);

        // Fruit List
        JLabel fruitHeaderLabel = new JLabel("Fruits");
        fruitHeaderLabel.setBounds(20, 20, 100, 20);
        fruitHeaderLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Initialize fruitTableModel
        fruitTableModel = new DefaultTableModel();
        fruitTableModel.addColumn("Fruit Name");
        fruitTableModel.addColumn("Price");

        // Populate fruitTableModel with data from the database
        for (String fruit : fruitsFromDatabase) {
            String[] parts = fruit.split(",");
            fruitTableModel.addRow(parts);
        }

        // Create fruitTable using fruitTableModel
        fruitTable = new JTable(fruitTableModel);
        fruitTable.getSelectionModel().addListSelectionListener(e -> updatePriceField()); // Update priceField when a fruit is selected

        // Add fruitTable to a JScrollPane
        JScrollPane fruitScrollPane = new JScrollPane(fruitTable);
        fruitScrollPane.setBounds(20, 40, 200, 100);

        // Adding components to main panel
        mainPanel.add(quantityLabel);
        mainPanel.add(quantityField);
        mainPanel.add(quantityUnitLabel);
        mainPanel.add(quantityUnitComboBox);
        mainPanel.add(priceLabel);
        mainPanel.add(priceField);
        mainPanel.add(addButton);
        mainPanel.add(calculateButton);
        mainPanel.add(paymentButton);
        mainPanel.add(deleteButton);
        mainPanel.add(billingScrollPane);
        mainPanel.add(fruitHeaderLabel);
        mainPanel.add(fruitScrollPane);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initializeFruitsFromDatabase() {
        fruitsFromDatabase = new String[0]; // Initialize the array

        try {
            // Database connection URL
            String DATABASE_URL = "jdbc:ucanaccess://D://fruit//FruitDB.accdb";

            // Establishing the database connection
            Connection connection = DriverManager.getConnection(DATABASE_URL);

            // Creating a statement for executing SQL queries
            Statement statement = connection.createStatement();

            // Executing the SQL query to retrieve fruit names and selling prices
            ResultSet resultSet = statement.executeQuery("SELECT ItemName, SellingPrice FROM Product");

            // Creating a list to hold the fruit names
            List<String> fruitsList = new ArrayList<>();

            // Iterating through the result set and adding fruit names to the list
            while (resultSet.next()) {
                String fruitName = resultSet.getString("ItemName");
                double sellingPrice = resultSet.getDouble("SellingPrice");
                fruitsList.add(fruitName + "," + sellingPrice); // Changed the format to comma-separated
            }

            // Closing the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();

            // Convert the list to an array
            fruitsFromDatabase = fruitsList.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace(); // Printing the stack trace of the exception
            JOptionPane.showMessageDialog(this, "Error accessing database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePriceField() {
        int selectedRow = fruitTable.getSelectedRow();
        if (selectedRow >= 0) {
            String price = (String) fruitTableModel.getValueAt(selectedRow, 1);
            priceField.setText(price);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addFruit();
        } else if (e.getSource() == calculateButton) {
            calculateTotal();
        } else if (e.getSource() == paymentButton) {
            printBill();
        } else if (e.getSource() == deleteButton) {
            deleteAll();
        } else if (e.getSource() == logoutButton) {
            dispose(); // Close the current window
            new MainFrame().setVisible(true); // Assuming LoginFrame is your login window class
        }
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        String formattedDateTime = now.format(formatter);
        currentDateTimeLabel.setText(formattedDateTime);
        currentDateTimeLabel.setForeground(Color.WHITE);
    }

    private void addFruit() {
        int selectedRow = fruitTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fruit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fruitName = (String) fruitTableModel.getValueAt(selectedRow, 0);
        double quantity = Double.parseDouble(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());
        String quantityUnit = (String) quantityUnitComboBox.getSelectedItem();

        double cost = (quantityUnit.equals("kilograms")) ? quantity * price : (quantity / 1000.0) * price;
        double amountPerUnit = price;

        // Format the output in a table-like structure
        String tableRow = String.format("%-15s %-8.2f %-15s $%-10.2f $%-10.2f\n", fruitName, quantity, quantityUnit, amountPerUnit, cost);
        billTextArea.append(tableRow);

        // Update the total cost
        totalCost += cost;

        // Clear fields after adding fruit
        quantityField.setText("");
        priceField.setText("");
    }

    private void calculateTotal() {
        DecimalFormat df = new DecimalFormat("#.##");
        billTextArea.append("\n-----------------------------------------------------------------------\n");
        billTextArea.append("Total Cost: $" + df.format(totalCost) + "\n");
    }

    private void printBill() {
        JOptionPane.showMessageDialog(this, billTextArea.getText(), "Bill", JOptionPane.PLAIN_MESSAGE);
        double totalAmount = totalCost;
        PaymentPage paymentPage = new PaymentPage(totalAmount);
        paymentPage.setVisible(true);
    }

    private void deleteAll() {
        billTextArea.setText(""); // Clear the JTextArea
        totalCost = 0; // Reset the total cost
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Billing().setVisible(true));
    }
}
