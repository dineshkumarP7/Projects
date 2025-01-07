import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class supplierPage extends JFrame {
    private JLabel nameLabel, importItemLabel, mobileLabel, emailLabel, placeLabel, dateLabel, timeLabel;
    private JTextField nameField, importItemField, mobileField, emailField, placeField;
    private JButton saveButton, clearButton, deleteButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;

    public supplierPage() {
        setTitle("Suppliers Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        initComponents();
        connectToDatabase();
    }

    private void initComponents() {
        setLayout(null);

        nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(20, 20, 120, 25);
        nameField = new JTextField();
        nameField.setBounds(150, 20, 200, 25);

        importItemLabel = new JLabel("Import Item:");
        importItemLabel.setBounds(20, 50, 120, 25);
        importItemField = new JTextField();
        importItemField.setBounds(150, 50, 200, 25);

        mobileLabel = new JLabel("Mobile:");
        mobileLabel.setBounds(20, 80, 120, 25);
        mobileField = new JTextField();
        mobileField.setBounds(150, 80, 200, 25);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 110, 120, 25);
        emailField = new JTextField();
        emailField.setBounds(150, 110, 200, 25);

        placeLabel = new JLabel("Place:");
        placeLabel.setBounds(20, 140, 120, 25);
        placeField = new JTextField();
        placeField.setBounds(150, 140, 200, 25);

        saveButton = new JButton("Save");
        saveButton.setBounds(150, 170, 80, 25);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCustomerDetails();
            }
        });

        clearButton = new JButton("Clear");
        clearButton.setBounds(250, 170, 80, 25);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(350, 170, 80, 25);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCustomerDetails();
            }
        });

        add(nameLabel);
        add(nameField);
        add(importItemLabel);
        add(importItemField);
        add(mobileLabel);
        add(mobileField);
        add(emailLabel);
        add(emailField);
        add(placeLabel);
        add(placeField);
        add(saveButton);
        add(clearButton);
        add(deleteButton);

        dateLabel = new JLabel();
        dateLabel.setBounds(600, 20, 150, 25);
        dateLabel.setForeground(Color.WHITE);
        add(dateLabel);

        timeLabel = new JLabel();
        timeLabel.setBounds(600, 50, 150, 25);
        timeLabel.setForeground(Color.WHITE);
        add(timeLabel);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Name", "Import Item", "Mobile", "Email", "Place"});
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 200, 740, 150);

        add(scrollPane);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.GRAY);
        footerPanel.setBounds(0, getHeight() - 88, 800, 50);
        add(footerPanel);

        JLabel copyrightLabel = new JLabel("Copyright © 2024 DK Software Pvt. Ltd.TM", SwingConstants.CENTER);
        copyrightLabel.setForeground(Color.WHITE);
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);
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

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Dealer");

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String importItem = resultSet.getString("ImportItem");
                String mobile = resultSet.getString("Mobile");
                String email = resultSet.getString("Email");
                String place = resultSet.getString("Place");

                tableModel.addRow(new Object[]{name, importItem, mobile, email, place});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch customer details from the database.");
        }
    }

    private void saveCustomerDetails() {
        saveButton.setEnabled(false); // Disable the save button

        String name = nameField.getText();
        String importItem = importItemField.getText();
        String mobile = mobileField.getText();
        String email = emailField.getText();
        String place = placeField.getText();

        if (isDuplicateRecord(mobile, email)) {
            JOptionPane.showMessageDialog(this, "Customer details already exist.");
            saveButton.setEnabled(true); // Re-enable the save button
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Dealer (Name, ImportItem, Mobile, Email, Place) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, importItem);
            ps.setString(3, mobile);
            ps.setString(4, email);
            ps.setString(5, place);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer details saved successfully.");
            clearFields();
            fetchAndDisplayDetails();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save customer details: " + ex.getMessage());
        } finally {
            saveButton.setEnabled(true); // Re-enable the save button
        }
    }

    private void deleteCustomerDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }

        String mobile = (String) tableModel.getValueAt(selectedRow, 2); // Assuming mobile is unique

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Dealer WHERE Mobile = ?");
            ps.setString(1, mobile);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Customer details deleted successfully.");
                tableModel.removeRow(selectedRow); // Remove row from table model
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer details.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete customer details: " + ex.getMessage());
        }
    }

    private boolean isDuplicateRecord(String mobile, String email) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Dealer WHERE Mobile = ? OR Email = ?");
            ps.setString(1, mobile);
            ps.setString(2, email);
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
        nameField.setText("");
        importItemField.setText("");
        mobileField.setText("");
        emailField.setText("");
        placeField.setText("");
    }

    

    public static void main(String[] args) {
        supplierPage frame = new supplierPage();
        frame.setVisible(true);
    }
}

