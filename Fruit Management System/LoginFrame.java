import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel timeDateLabel;

    public LoginFrame() {
        setTitle("Fruit Shop Management");
        setSize(400, 250); // Increased the size of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY); // Set background color

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.GRAY);
        headerPanel.setLayout(new BorderLayout());
        JLabel headerLabel = new JLabel("Welcome to Login ");
        headerLabel.setForeground(Color.WHITE); // Set text color
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        timeDateLabel = new JLabel(getDateTime());
        timeDateLabel.setForeground(Color.WHITE);
        headerPanel.add(timeDateLabel, BorderLayout.EAST);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.GRAY);
        JLabel footerLabel = new JLabel("Copyright © 2024 DK Software Pvt. Ltd.TM");
        footerLabel.setForeground(Color.WHITE); // Set text color
        footerPanel.add(footerLabel);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE); // Set background color

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        loginButton.addActionListener(this);

        // Set bounds for each component
        usernameLabel.setBounds(20, 20, 80, 25);
        passwordLabel.setBounds(20, 50, 80, 25);
        usernameField.setBounds(100, 20, 160, 25);
        passwordField.setBounds(100, 50, 160, 25);
        loginButton.setBounds(100, 80, 80, 25);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);

        // Update time and date every second
        Timer timer = new Timer(1000, e -> timeDateLabel.setText(getDateTime()));
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // You can perform your authentication logic here
            if (username.equals("admin") && password.equals("admin")) {
                // Dispose the current login frame
                dispose();

                // Open the new frame (Replace NewFrame with the name of your next frame class)
                MainFrame newFrame = new MainFrame();
                newFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
