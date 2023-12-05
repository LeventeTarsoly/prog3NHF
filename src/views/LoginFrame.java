package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A belépési Frame
 */
public class LoginFrame extends JFrame {
    /**
     * A jelszó mutatására alkalmas CheckboxS
     */
    static JCheckBox showPassword;
    /**
     * A jelszóhoz használt szövegbodoz, ami elrejti a bemeneti adatokat
     */
    static JPasswordField pwField;
    /**
     * A felhasználónévhez használt szövegdoboz
     */
    static JTextField usernameField;
    /**
     * Bejelentkezési üzenetet tartalmazó szövegdoboz
     */
    static JLabel msgLabel;

    /**
     * Beléptető Frame Létrehozása
     */
    public LoginFrame() {
        //Settings of the frame
        super("Bejelentkezés");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(350, 190);
        setLayout(new GridLayout(3, 1));

        //Létrehozza a Paneleket,és a kompononseket
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
        button.addActionListener(new LoginButtonActionListener());


        //A kompononenseket a panelekhez adja
        upperPanel.add(userLabel, BorderLayout.SOUTH);
        upperPanel.add(usernameField, BorderLayout.SOUTH);
        upperPanel.add(msgLabel);
        centerPanel.add(pwLabel, BorderLayout.NORTH);
        centerPanel.add(pwField, BorderLayout.NORTH);
        centerPanel.add(showPassword, BorderLayout.SOUTH);
        lowerPanel.add(button, BorderLayout.CENTER);
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }

    /**
     * A beléptető gombhoz tartozó ActionListener
     */
    class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (usernameField.getText().equalsIgnoreCase("tarsolyl") && pwField.getText().equalsIgnoreCase("proghf")) {
                dispose();

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
            else
                msgLabel.setText("Hibás felhasználónév vagy jelszó");
        }
    }

    /**
     * A jelszó mutatásához tartozó Checkboc Listenerje
     */
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
