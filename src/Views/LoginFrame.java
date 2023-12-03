package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    static JCheckBox showPassword;
    static JPasswordField pwField;
    static JTextField usernameField;
    static JLabel msgLabel;
    public LoginFrame() {
        //Settings of the frame
        super("Bejelentkezés");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(350, 190);
        setLocation(new Point(800, 450));
        setLayout(new GridLayout(3, 1));
        //Create Swing units
        JPanel upperPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        usernameField = new JTextField(20);
        pwField = new JPasswordField(20);
        JLabel userLabel = new JLabel("Felhasználónév:");
        msgLabel = new JLabel("");
        JLabel pwLabel =  new JLabel("Jelszó:");
        JButton button = new JButton("Belépés");
        showPassword = new JCheckBox("Jelszó mutatása");
        showPassword.addActionListener(new ShowPasswordActionListener());

        //settings of Swing units
        button.addActionListener(new LoginButtonActionListener());


        //add units to panels
        upperPanel.add(userLabel, BorderLayout.SOUTH);
        upperPanel.add(usernameField, BorderLayout.SOUTH);
        upperPanel.add(msgLabel);
        centerPanel.add(pwLabel, BorderLayout.NORTH);
        centerPanel.add(pwField, BorderLayout.NORTH);
        centerPanel.add(showPassword, BorderLayout.SOUTH);
        lowerPanel.add(button, BorderLayout.CENTER);

        //add panels to the frame
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }

    class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (usernameField.getText().equalsIgnoreCase("mehtab") && pwField.getText().equalsIgnoreCase("12345")) {
                dispose();

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
            else
                msgLabel.setText("Hibás felhasználónév vagy jelszó");
        }
    }

    static class ShowPasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (showPassword.isSelected())
                pwField.setEchoChar((char) 0);
            else
                pwField.setEchoChar('*');
        }
    }

}
