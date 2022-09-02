/*This class is the login screen class. It takes care of the initial login screen*/


import javax.swing.*;
import java.awt.*;


import static java.awt.Image.SCALE_SMOOTH;

class LoginScreen extends JFrame {

    private JFrame loginWindow;

    LoginScreen () {
        ImageIcon logo = new ImageIcon("TheComputerStore.png");
        Image image = logo.getImage();
        Image scaledImage = image.getScaledInstance(400, 250, SCALE_SMOOTH);
        logo = new ImageIcon(scaledImage);

        JButton loginButton = new JButton("Click to Login", logo);
        loginButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        loginButton.setFont(new Font("helvetica", Font.BOLD, 30));
        loginButton.setOpaque(true);
        loginButton.setBackground(new Color(178, 224, 247));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(178, 224, 247)));

        loginButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(loginWindow);
            loginDialog.setVisible(true);
        });

        loginWindow = new JFrame("Computer Products Management System");
        loginWindow.add(loginButton, BorderLayout.CENTER);
        loginWindow.setBackground(new Color(178, 224, 247));
        loginWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginWindow.setSize(700, 300);
        loginWindow.setResizable(false);
        loginWindow.setVisible(true);
    }
}