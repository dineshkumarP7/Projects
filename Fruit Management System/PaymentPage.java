import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentPage extends JFrame {

    private JTextField amountField, receivedAmountField, balanceField;
    private JComboBox<String> paymentMethodComboBox;
    private JButton payButton, cancelButton;
    private JLabel headerLabel, footerLabel;
    private int billNumber;

    public PaymentPage(double totalAmount) {
        setTitle("Payment Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // Using null layout to use setBounds

        // Generate random bill number
        billNumber = (int) (Math.random() * 1000000);

        // Header Panel
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(0, 0, getWidth(), 50);
        headerPanel.setBackground(Color.GRAY); // Set header background color
        JButton previousPageButton = new JButton("Previous Page");
        previousPageButton.setBounds(10, 10, 120, 30);
        headerPanel.add(previousPageButton);

        previousPageButton.addActionListener(e -> {
            Billing billingPage = new Billing();
            billingPage.setVisible(true);
            dispose(); // Close current frame
        });

        headerLabel = new JLabel("Payment Page", SwingConstants.CENTER);
        headerLabel.setBounds(0, 0, getWidth(), 50);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        JLabel dateTimeLabel = new JLabel(dateTime);
        dateTimeLabel.setBounds(-30, 0, getWidth(), 50);
        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(dateTimeLabel);
        dateTimeLabel.setForeground(Color.WHITE);

        // Input Panel
        JPanel inputPanel = new JPanel(null);
        inputPanel.setBounds(50, 80, getWidth() - 100, 160);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

        JLabel totalLabel = new JLabel("Total Amount:");
        totalLabel.setBounds(20, 30, 100, 20);
        inputPanel.add(totalLabel);
        amountField = new JTextField();
        amountField.setEditable(false);
        amountField.setText(Double.toString(totalAmount));
        amountField.setBounds(120, 30, 120, 20);
        inputPanel.add(amountField);

        JLabel receivedAmountLabel = new JLabel("Received Amount:");
        receivedAmountLabel.setBounds(20, 60, 120, 20);
        inputPanel.add(receivedAmountLabel);
        receivedAmountField = new JTextField();
        receivedAmountField.setBounds(150, 60, 120, 20);
        receivedAmountField.addActionListener(e -> calculateBalance());
        inputPanel.add(receivedAmountField);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setBounds(20, 90, 120, 20);
        inputPanel.add(paymentMethodLabel);
        String[] paymentMethods = {"Cash", "Card", "QR"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        paymentMethodComboBox.setBounds(150, 90, 120, 20);
        paymentMethodComboBox.addActionListener(e -> calculateBalance());
        inputPanel.add(paymentMethodComboBox);

        JLabel balanceTextLabel = new JLabel("Balance Amount:");
        balanceTextLabel.setBounds(20, 120, 120, 20);
        inputPanel.add(balanceTextLabel);
        balanceField = new JTextField();
        balanceField.setEditable(false);
        balanceField.setBounds(150, 120, 120, 20);
        inputPanel.add(balanceField);

        // Button Panel
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(200, 250, getWidth() - 400, 50);

        payButton = new JButton("Pay");
        payButton.setBounds(0, 0, 100, 30);
        payButton.addActionListener(e -> {
            if (pay()) {
                JOptionPane.showMessageDialog(PaymentPage.this, "Payment successful. Please visit again.", "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
                Billing billingPage = new Billing();
                billingPage.setVisible(true);
                dispose(); // Close the payment page
            }
        });
        buttonPanel.add(payButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(100, 0, 100, 30);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        // Footer Panel
        JPanel footerPanel = new JPanel(null);
        footerPanel.setBounds(0, getHeight() - 88, getWidth(), 50);
        footerPanel.setBackground(Color.GRAY); // Set footer background color

        footerLabel = new JLabel(" 2024 © DK Software Pvt. Ltd.TM", SwingConstants.CENTER);
        footerLabel.setBounds(0, 0, getWidth(), 50);
        footerPanel.add(footerLabel);
        footerLabel.setForeground(Color.WHITE);

        // Add components to the frame
        add(headerPanel);
        add(inputPanel);
        add(buttonPanel);
        add(footerPanel);
    }

    private void calculateBalance() {
        try {
            double receivedAmount = Double.parseDouble(receivedAmountField.getText());
            double totalAmount = Double.parseDouble(amountField.getText());
            double balance = receivedAmount - totalAmount;
            balanceField.setText(Double.toString(balance));
        } catch (NumberFormatException e) {
            balanceField.setText("0.0");
        }
    }

    private boolean pay() {
        try {
            double receivedAmount = Double.parseDouble(receivedAmountField.getText());
            double totalAmount = Double.parseDouble(amountField.getText());
            String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
            double balance = Double.parseDouble(balanceField.getText());

            if (receivedAmount < totalAmount) {
                JOptionPane.showMessageDialog(this, "Received amount is less than total amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (receivedAmount == 0) {
                JOptionPane.showMessageDialog(this, "Received amount cannot be zero.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (selectedMethod == null || selectedMethod.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a payment method.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                storeBillingDetails(totalAmount, receivedAmount, selectedMethod, balance);
                return true;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for received amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void storeBillingDetails(double totalAmount, double receivedAmount, String paymentMethod, double balance) {
        try {
            String DATABASE_URL = "jdbc:ucanaccess://D://fruit//FruitDB.accdb";
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            String sql = "INSERT INTO BillingHistory (BillNumber, TotalAmount, PaymentDate, ReceivedAmount, PaymentMethod, BalanceAmount) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, billNumber);
            preparedStatement.setDouble(2, totalAmount);
            preparedStatement.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            preparedStatement.setDouble(4, receivedAmount);
            preparedStatement.setString(5, paymentMethod);
            preparedStatement.setDouble(6, balance);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            JOptionPane.showMessageDialog(this, "Billing details stored successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error storing billing details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentPage(100).setVisible(true));
    }
}
