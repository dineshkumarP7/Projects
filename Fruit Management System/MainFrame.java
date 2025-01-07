import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JButton billingButton;
    private JButton salesReportButton;
    private JButton productSalesButton;
    private JButton customerButton;
    private JButton logoutButtonFooter;
    private JLabel copyrightLabel;
    private Image backgroundImage;

    public MainFrame() {
        setTitle("Fruit Shop Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 620);

        // Load the background image
        backgroundImage = new ImageIcon("D:\\fruit\\image\\main10.jpg").getImage();

        // Create and configure background label
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, 880, 600);
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(null);

        // Header Panel
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(Color.GRAY);
        headerPanel.setBounds(0, 0, 800, 50);
        backgroundLabel.add(headerPanel);

        JLabel headerLabel = new JLabel("THE JUICY BASKET");
        headerLabel.setFont(new Font("CAMBRIA", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBounds(200, 10, 400, 30);
        headerPanel.add(headerLabel);

        // Date and Time Components
        dateLabel = new JLabel();
        dateLabel.setBounds(600, 10, 100, 30);
        dateLabel.setForeground(Color.WHITE);  
        headerPanel.add(dateLabel);

        timeLabel = new JLabel();
        timeLabel.setBounds(700, 10, 100, 30); 
        timeLabel.setForeground(Color.WHITE);  
        headerPanel.add(timeLabel);

        // Button dimensions and margins
        int buttonWidth = 180;
        int buttonHeight = 80;
        int buttonMargin = 20;

        // Total width and height occupied by the buttons including margins
        int totalWidth = (buttonWidth + buttonMargin) * 2 - buttonMargin;
        int totalHeight = (buttonHeight + buttonMargin) * 2 - buttonMargin;

        // Calculate the starting x and y positions to center the buttons
        int xPos = (getWidth() - totalWidth) / 2;
        int yPos = (getHeight() - totalHeight) / 2;

        billingButton = createButtonWithIcon("Billing", "D://fruit//image//billing.png");
        billingButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        billingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openBilling();
            }
        });
        backgroundLabel.add(billingButton);

        xPos += buttonWidth + buttonMargin;
        salesReportButton = createButtonWithIcon("Billing History", "D:\\fruit\\image\\sales.png");
        salesReportButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        salesReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    
                openBillingHis();
            }
        });
        backgroundLabel.add(salesReportButton);

        xPos -= buttonWidth + buttonMargin;
        yPos += buttonHeight + buttonMargin;
        productSalesButton = createButtonWithIcon("Suppliers Details", "D:\\fruit\\image\\supplier.png");
        productSalesButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        productSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opensupplierPage();
            }
        });
        backgroundLabel.add(productSalesButton);

        xPos += buttonWidth + buttonMargin;
        customerButton = createButtonWithIcon("Products", "D:\\fruit\\image\\prodect.png");
        customerButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProduct();
            }
        });
        backgroundLabel.add(customerButton);

        // Footer Panel
        JPanel footerPanel = new JPanel(null);
        footerPanel.setBackground(Color.GRAY);
        footerPanel.setBounds(0, getHeight() - 88, 800, 50);
        backgroundLabel.add(footerPanel);

        logoutButtonFooter = new JButton("Logout");
        logoutButtonFooter.setBounds(10, 10, 80, 30);
        footerPanel.add(logoutButtonFooter);

        
        logoutButtonFooter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openindexPage();
            }
        });

        copyrightLabel = new JLabel("Copyright © 2024 DK Software Pvt. Ltd.TM", SwingConstants.CENTER);
        copyrightLabel.setBounds(200, 10, 400, 30);
        copyrightLabel.setForeground(Color.WHITE);
        footerPanel.add(copyrightLabel);

        // Set current time and date
        updateTimeAndDate();

        // Update time and date every second
        Timer timer = new Timer(1000, e -> updateTimeAndDate());
        timer.start();
    }

    private JButton createButtonWithIcon(String text, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Resize the image to fit the button
        icon = new ImageIcon(resizedImg);

        JButton button = new JButton(text, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        return button;
    }

    private void openBilling() {
        try {
            Process compileProcess = Runtime.getRuntime().exec("javac Billing.java");
            int compileResult = compileProcess.waitFor();
            if (compileResult != 0) {
                throw new RuntimeException("Compilation failed");
            }

            // Run CustomerPage
            Process runProcess = Runtime.getRuntime().exec("java -cp \"D:\\fruit\\*;\" Billing");
            int runResult = runProcess.waitFor();
            if (runResult != 0) {
                throw new RuntimeException("Execution failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open Customer Page: " + ex.getMessage());
        }
    }

    private void openBillingHis() {
        try {
            Process compileProcess = Runtime.getRuntime().exec("javac BillingHis.java");
            int compileResult = compileProcess.waitFor();
            if (compileResult != 0) {
                throw new RuntimeException("Compilation failed");
            }

            // Run CustomerPage
            Process runProcess = Runtime.getRuntime().exec("java -cp \"D:\\fruit\\*;\" BillingHis");
            int runResult = runProcess.waitFor();
            if (runResult != 0) {
                throw new RuntimeException("Execution failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open Product Page: " + ex.getMessage());
        }
    }

    private void openProduct() {
        try {
            // Compile CustomerPage.java
            Process compileProcess = Runtime.getRuntime().exec("javac Prodect.java");
            int compileResult = compileProcess.waitFor();
            if (compileResult != 0) {
                throw new RuntimeException("compile failed");
            }

            // Run CustomerPage
            Process runProcess = Runtime.getRuntime().exec("java -cp \"D:\\fruit\\*;\" Prodect");
            int runResult = runProcess.waitFor();
            if (runResult != 0) {
                throw new RuntimeException("Execution failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open Product Page: " + ex.getMessage());
        }
    }

    private void opensupplierPage() {
        try {
            // Compile CustomerPage.java
            Process compileProcess = Runtime.getRuntime().exec("javac supplierPage.java");
            int compileResult = compileProcess.waitFor();
            if (compileResult != 0) {
                throw new RuntimeException("Compilation failed");
            }

            // Run CustomerPage
            Process runProcess = Runtime.getRuntime().exec("java -cp \"D:\\fruit\\*;\" supplierPage");
            int runResult = runProcess.waitFor();
            if (runResult != 0) {
                throw new RuntimeException("Execution failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open Customer Page: " + ex.getMessage());
        }
    }

    private void openindexPage() {
        this.dispose(); 
        indexpage indexPage = new indexpage();
        indexPage.setVisible(true);
    }

    private void updateTimeAndDate() {
        timeLabel.setText(new java.text.SimpleDateFormat("hh:mm:ss a").format(new java.util.Date()));
        dateLabel.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
