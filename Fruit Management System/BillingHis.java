import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class BillingHis extends JFrame {

    private DefaultTableModel tableModel;

    public BillingHis() {
        setTitle("Fruit Shop Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Billing History", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(Color.GRAY);
        headerLabel.setForeground(Color.WHITE);
        add(headerLabel, BorderLayout.NORTH);

        // Table to display data
        JTable table = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Bill Number");
        tableModel.addColumn("Total Amount");
        tableModel.addColumn("Payment Date");
        tableModel.addColumn("Received Amount");
        tableModel.addColumn("Payment Method");
        tableModel.addColumn("Balance Amount");
        table.setModel(tableModel);

        // Scroll Pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        JButton clearButton = new JButton("Clear Data");
        clearButton.addActionListener(e -> clearTableData());
        buttonPanel.add(clearButton, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Footer Label
        JLabel footerLabel = new JLabel("© 2024 DK Software Pvt. Ltd.TM", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setOpaque(true);
        footerLabel.setBackground(Color.GRAY);
        footerLabel.setForeground(Color.WHITE);
        buttonPanel.add(footerLabel, BorderLayout.SOUTH);

        // Fetch data from the database and populate the table
        fetchData();
    }

    private void fetchData() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Database connection URL
            String DATABASE_URL = "jdbc:ucanaccess://D://fruit//FruitDB.accdb";

            // Establishing the database connection
            connection = DriverManager.getConnection(DATABASE_URL);

            // SQL query to fetch billing details
            String sql = "SELECT * FROM BillingHistory";

            // Creating a prepared statement
            statement = connection.prepareStatement(sql);

            // Executing the query
            resultSet = statement.executeQuery();

            // Adding data to the table model
            while (resultSet.next()) {
                int billNumber = resultSet.getInt("BillNumber");
                double totalAmount = resultSet.getDouble("TotalAmount");
                String paymentDate = resultSet.getString("PaymentDate");
                double receivedAmount = resultSet.getDouble("ReceivedAmount");
                String paymentMethod = resultSet.getString("PaymentMethod");
                double balanceAmount = resultSet.getDouble("BalanceAmount");

                Object[] row = {billNumber, totalAmount, paymentDate, receivedAmount, paymentMethod, balanceAmount};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            // Inform the user about any errors that occur during database operation
            JOptionPane.showMessageDialog(this, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Print the stack trace for debugging
        } finally {
            // Closing resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void clearTableData() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Database connection URL
            String DATABASE_URL = "jdbc:ucanaccess://D://fruit//FruitDB.accdb";

            // Establishing the database connection
            connection = DriverManager.getConnection(DATABASE_URL);

            // SQL query to delete all records from BillingHistory
            String sql = "DELETE FROM BillingHistory";

            // Creating a prepared statement
            statement = connection.prepareStatement(sql);

            // Executing the update
            statement.executeUpdate();

            // Clear the table model
            tableModel.setRowCount(0); // Clears all rows in the table

            JOptionPane.showMessageDialog(this, "All billing records have been cleared from the database.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            // Inform the user about any errors that occur during database operation
            JOptionPane.showMessageDialog(this, "Error clearing data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Print the stack trace for debugging
        } finally {
            // Closing resources
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingHis().setVisible(true));
    }
}
