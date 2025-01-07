import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class indexpage extends JFrame {
    private JLabel footerLabel;
    private JLabel dateLabel;
    private JLabel timeLabel;
    private JButton welcomeButton;

    public indexpage() {

        setLayout(null);
        // Set background image
        ImageIcon backgroundImage = new ImageIcon("D:\\fruit\\image\\in.jpg"); 
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600); 
        add(backgroundLabel);

       
        ImageIcon buttonIcon = new ImageIcon("D:\\fruit\\image\\logo.png");

        // Create and add the welcome button
        welcomeButton = new JButton();
        welcomeButton.setBounds(330, 120, buttonIcon.getIconWidth(), buttonIcon.getIconHeight()); 
        welcomeButton.setIcon(buttonIcon);
        welcomeButton.setBorderPainted(false); // Remove border
        welcomeButton.setContentAreaFilled(false); // Make content area transparent
        welcomeButton.setFocusPainted(false); // Remove focus border
        welcomeButton.setOpaque(false); 
        backgroundLabel.add(welcomeButton);

        
        welcomeButton.addActionListener(e -> {
            LoginFrame nextPage = new LoginFrame();
            nextPage.setVisible(true);
            dispose(); 
        });

        // Header Panel with background color
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.GRAY);
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 800, 80); 
        backgroundLabel.add(headerPanel);

        // Header label inside header panel
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
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(dateLabel);

        timeLabel = new JLabel();
        timeLabel.setBounds(690, 10, 100, 30); 
        timeLabel.setForeground(Color.WHITE);  
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(timeLabel);

        // Footer Panel with background color
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.GRAY); 
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBounds(0, 550, 800, 50); 
        backgroundLabel.add(footerPanel);

        // Footer label inside footer panel
        footerLabel = new JLabel("Copyright  © 2024 DK Software Pvt. Ltd.TM");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        //  "Stay" label
        JLabel stayLabel = new JLabel("Stay");
        stayLabel.setFont(new Font("Cambria", Font.BOLD, 36));
        stayLabel.setForeground(Color.WHITE);
        stayLabel.setBounds(337, 280, 100, 50);
        backgroundLabel.add(stayLabel);

        //  "Fresh" label
        JLabel freshLabel = new JLabel("Fresh");
        freshLabel.setFont(new Font("Cambria", Font.BOLD, 36));
        freshLabel.setForeground(Color.WHITE);
        freshLabel.setBounds(365, 320, 100, 50); 
        backgroundLabel.add(freshLabel);

        //  "Fruit" label
        JLabel fruitLabel = new JLabel("Fruit");
        fruitLabel.setFont(new Font("Cambria", Font.BOLD, 36));
        fruitLabel.setForeground(Color.WHITE);
        fruitLabel.setBounds(420, 350, 100, 50); 
        backgroundLabel.add(fruitLabel);

        // Set frame properties
        setTitle("Fruit Shop Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(810, 638); 
        setLocationRelativeTo(null); 
        setVisible(true);
        updateTimeAndDate();

        
        Timer timer = new Timer(1000, e -> updateTimeAndDate());
        timer.start();
    }

    
    private void updateTimeAndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); // 12-hour format with AM/PM
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());
        dateLabel.setText(date);
        timeLabel.setText(time);
    }

    public static void main(String[] args) {
        new indexpage();
    }
}
