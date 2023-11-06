package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        //Settings of the frame
        super("Bejelentkezés");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(350, 150);
        setLocation(new Point(800, 450));
        setLayout(new GridLayout(3, 1));
        //Create Swing units
        JPanel upperPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        JTextField upperTextField = new JTextField(20);
        JPasswordField pwField = new JPasswordField(20);
        JLabel userLabel = new JLabel("Felhasználónév:");
        JLabel pwLabel =  new JLabel("Jelszó:");
        JButton button = new JButton("Belépés");

        //settings of Swing units
        button.addActionListener(new LoginButtonActionListener());


        //add units to panels
        upperPanel.add(userLabel);
        upperPanel.add(upperTextField);
        centerPanel.add(pwLabel);
        centerPanel.add(pwField);
        lowerPanel.add(button);

        //add panels to the frame
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.EAST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }

    static class LoginButtonActionListener implements ActionListener {
        //TODO login when button pressed at LoginFrame
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
