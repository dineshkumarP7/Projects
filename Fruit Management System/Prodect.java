import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prodect extends JFrame {
    private JLabel itemNameLabel, supplierNameLabel, doReceivingLabel, doExpiryLabel, origPriceLabel, sellingPriceLabel, qtyLabel, totalAmountLabel;
    private JTextField itemNameField, supplierNameField, doReceivingField, doExpiryField, origPriceField, sellingPriceField, qtyField;
    private JLabel totalAmountField;
    private JButton saveButton, clearButton, deleteButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;

    public Prodect() {
        setTitle("Prodect Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(817, 620);
        setLocationRelativeTo(null);

        initComponents();
        connectToDatabase();
    }

    private void initComponents() {
        setLayout(null);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.GRAY);
        headerPanel.setBounds(0, 0, 800, 50);

        // Title Label
        JLabel titleLabel = new JLabel("Product Details", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("CAMBRIA", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        

        // Date-Time Label
        JLabel dateTimeLabel = new JLabel();
        updateDateTimeLabel(dateTimeLabel);
        headerPanel.add(dateTimeLabel, BorderLayout.EAST);
        dateTimeLabel.setForeground(Color.WHITE);
        add(headerPanel);

        // Footer Panel
        JPanel footerPanel = new JPanel(null);
        footerPanel.setBackground(Color.GRAY);
        footerPanel.setBounds(0, 540, 800, 50);

        JLabel titleLabel1 = new JLabel("Copyright   © 2024 DK Software Pvt. Ltd.TM", JLabel.CENTER);
        footerPanel.setForeground(Color.WHITE);
        footerPanel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel1.setBounds(200, 10, 400, 30);
        footerPanel.add(titleLabel1, BorderLayout.CENTER);
        add(footerPanel);

        itemNameLabel = new JLabel("Item Name:");
        itemNameLabel.setBounds(20, 60, 120, 25);
        itemNameField = new JTextField();
        itemNameField.setBounds(150, 60, 200, 25);

        supplierNameLabel = new JLabel("Supplier Name:");
        supplierNameLabel.setBounds(20, 90, 120, 25);
        supplierNameField = new JTextField();
        supplierNameField.setBounds(150, 90, 200, 25);

        doReceivingLabel = new JLabel("D.O. Receiving:");
        doReceivingLabel.setBounds(20, 120, 120, 25);
        doReceivingField = new JTextField();
        doReceivingField.setBounds(150, 120, 200, 25);

        doExpiryLabel = new JLabel("D.O. Expiry:");
        doExpiryLabel.setBounds(20, 150, 120, 25);
        doExpiryField = new JTextField();
        doExpiryField.setBounds(150, 150, 200, 25);

        origPriceLabel = new JLabel("Orig Price:");
        origPriceLabel.setBounds(20, 180, 120, 25);
        origPriceField = new JTextField();
        origPriceField.setBounds(150, 180, 200, 25);

        sellingPriceLabel = new JLabel("Selling Price per Kg/Gms:");
        sellingPriceLabel.setBounds(20, 210, 170, 25);
        sellingPriceField = new JTextField();
        sellingPriceField.setBounds(200, 210, 150, 25);
        sellingPriceField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTotalAmount();
            }
        });

        qtyLabel = new JLabel("Qty (Kg/Gms):");
        qtyLabel.setBounds(20, 240, 120, 25);
        qtyField = new JTextField();
        qtyField.setBounds(150, 240, 200, 25);
        qtyField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTotalAmount();
            }
        });

        totalAmountLabel = new JLabel("Total Amount:");
        totalAmountLabel.setBounds(20, 270, 120, 25);
        totalAmountField = new JLabel();
        totalAmountField.setBounds(150, 270, 200, 25);
        totalAmountField.setOpaque(true);
        totalAmountField.setBackground(Color.LIGHT_GRAY);

        saveButton = new JButton("Save");
        saveButton.setBounds(150, 300, 80, 25);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveInventoryDetails();
            }
        });

        clearButton = new JButton("Clear");
        clearButton.setBounds(250, 300, 80, 25);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(350, 300, 80, 25);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteInventoryDetails();
            }
        });

        add(itemNameLabel);
        add(itemNameField);
        add(supplierNameLabel);
        add(supplierNameField);
        add(doReceivingLabel);
        add(doReceivingField);
        add(doExpiryLabel);
        add(doExpiryField);
        add(origPriceLabel);
        add(origPriceField);
        add(sellingPriceLabel);
        add(sellingPriceField);
        add(qtyLabel);
        add(qtyField);
        add(totalAmountLabel);
        add(totalAmountField);
        add(saveButton);
        add(clearButton);
        add(deleteButton);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Item Name", "Supplier Name", "D.O. Receiving", "D.O. Expiry", "Orig Price", "Selling Price", "Qty", "Total Amount"});
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 340, 740, 200);

        add(scrollPane);
    }

    private void connectToDatabase() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String url = "jdbc:ucanaccess://D://fruit//FruitDB.accdb";
            conn = DriverManager.getConnection(url);
            fetchAndDisplayDetails();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            System.exit(1);
        }
    }

    private void fetchAndDisplayDetails() {
        tableModel.setRowCount(0); // Clear existing rows in the table model

        try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM Product")) {
            while (resultSet.next()) {
                String itemName = resultSet.getString("ItemName");
                String supplierName = resultSet.getString("SupplierName");
                Date doReceiving = resultSet.getDate("DOReceiving");
                Date doExpiry = resultSet.getDate("DOExpiry");
                double origPrice = resultSet.getDouble("OrigPrice");
                double sellingPrice = resultSet.getDouble("SellingPrice");
                int qty = resultSet.getInt("Qty");
                double totalAmount = resultSet.getDouble("TotalAmount");

                tableModel.addRow(new Object[]{itemName, supplierName, doReceiving, doExpiry, origPrice, sellingPrice, qty, totalAmount});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch Product details from the database.");
        }
    }

    private void saveInventoryDetails() {
        saveButton.setEnabled(false); // Disable the save button

        String itemName = itemNameField.getText();
        String supplierName = supplierNameField.getText();
        String doReceiving = doReceivingField.getText();
        String doExpiry = doExpiryField.getText();
        String origPrice = origPriceField.getText();
        String sellingPrice = sellingPriceField.getText();
        String qty = qtyField.getText();

        double totalAmount;
        try {
            double sellingPriceDouble = Double.parseDouble(sellingPrice);
            double qtyDouble = Double.parseDouble(qty);
            totalAmount = sellingPriceDouble * qtyDouble;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for selling price and quantity.");
            saveButton.setEnabled(true);
            return;
        }

        if (isDuplicateRecord(itemName, supplierName)) {
            JOptionPane.showMessageDialog(this, "Product details already exist.");
            saveButton.setEnabled(true); // Re-enable the save button
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Product (ItemName, SupplierName, DOReceiving, DOExpiry, OrigPrice, SellingPrice, Qty, TotalAmount, SellAmt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, itemName);
            ps.setString(2, supplierName);
            ps.setDate(3, parseDate(doReceiving));
            ps.setDate(4, parseDate(doExpiry));
            ps.setDouble(5, Double.parseDouble(origPrice));
            ps.setDouble(6, Double.parseDouble(sellingPrice));
            ps.setInt(7, Integer.parseInt(qty));
            ps.setDouble(8, totalAmount);
            ps.setDouble(9, totalAmount); // Assuming SellAmt is the same as Total Amount on insertion
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product details saved successfully.");
            clearFields();
            fetchAndDisplayDetails();
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save Product details: " + ex.getMessage());
        } finally {
            saveButton.setEnabled(true); // Re-enable the save button
        }
    }

    private void deleteInventoryDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        String itemName = (String) tableModel.getValueAt(selectedRow, 0); // Assuming item name is unique

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Product WHERE ItemName = ?");
            ps.setString(1, itemName);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Product details deleted successfully.");
                tableModel.removeRow(selectedRow); // Remove row from table model
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete Product details.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete Product details: " + ex.getMessage());
        }
    }

    private boolean isDuplicateRecord(String itemName, String supplierName) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Product WHERE ItemName = ? AND SupplierName = ?");
            ps.setString(1, itemName);
            ps.setString(2, supplierName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to check for duplicate records: " + ex.getMessage());
        }
        return false;
    }

    private void clearFields() {
        itemNameField.setText("");
        supplierNameField.setText("");
        doReceivingField.setText("");
        doExpiryField.setText("");
        origPriceField.setText("");
        sellingPriceField.setText("");
        qtyField.setText("");
        totalAmountField.setText("");
    }

    private java.sql.Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = sdf.parse(date);
        return new java.sql.Date(utilDate.getTime());
    }

    private void updateDateTimeLabel(JLabel label) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(sdf.format(new Date()));
            }
        });
        timer.start();
    }

    private void updateTotalAmount() {
        try {
            double sellingPrice = Double.parseDouble(sellingPriceField.getText());
            double qty = Double.parseDouble(qtyField.getText());
            double totalAmount = sellingPrice * qty;
            totalAmountField.setText(String.valueOf(totalAmount));
        } catch (NumberFormatException e) {
            totalAmountField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Prodect().setVisible(true);
        });
    }
}
