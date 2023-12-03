package views;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Egy audioanyag létrehozásához vagy módosításához használható panel
 */
public class MemberPanel extends JPanel{
    /**
     * Az adatok megadásának panele.
     */
    private final JPanel mainPanel;

    /**
     * A tag nevének szövegmezője.
     */
    private final JTextField name;

    /**
     * A tag születési idejének szövegmezője.
     */
    private final JFormattedTextField dateOfBirth;

    /**
     * A tag telefonszámának szövegmezője.
     */
    private final JTextField phone;

    /**
     * Instantiates a new Member panel.
     */
    MemberPanel() {
        this.mainPanel = new JPanel(new BorderLayout(5, 5)); // a JOptionPane fő panele

        // Labelek létrehozása és panelhez adása
        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2)); // a JLabeleket tartalmazó panel
        label.add(new JLabel("Név", SwingConstants.RIGHT));
        label.add(new JLabel("Születési idő", SwingConstants.RIGHT));
        label.add(new JLabel("Telefonszám", SwingConstants.RIGHT));
        this.mainPanel.add(label, BorderLayout.WEST);

        // a felhasználó által szerkeszthető komponensek létrehozása és panelhez adása
        JPanel input = new JPanel(new GridLayout(0, 1, 2, 2));
        this.name = new JTextField(10);
        this.dateOfBirth = new JFormattedTextField();
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.install(dateOfBirth);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Hibás dátumformátum. Használja az éééé-hh-nn formátumot.", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
        }
        this.phone = new JTextField(10);

        input.add(name);
        input.add(dateOfBirth);
        input.add(phone);
        this.mainPanel.add(input, BorderLayout.CENTER);
    }

    /**
     * Gets main panel.
     *
     * @return the main panel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Visszaadja a név szövegmezőben megadott értéket.
     *
     * @return A név megadására szolgáló TextField tartalma
     */
    public String getNameValue() {
        return name.getText();
    }

    /**
     * Beállítja a név megadására való TextField értékét.
     *
     * @param name A név, amire állítani szeretnénk a TextField-et
     */
    public void setNameValue(String name) {
        this.name.setText(name);
    }

    /**
     * Visszaadja a születési idő mezőben megadott értéket.
     *
     * @return A születési év TextField értéke
     */
    public String getDateOfBirthValue() {
        return dateOfBirth.getText();
    }

    /**
     * Beállítja a születési év TextField értékét.
     *
     * @param dateOfBirth Az érték, amire állítani szeretnénk
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.setText(dateOfBirth);
    }

    /**
     * Visszaadja a telefonszám szövegmezőben megadott értéket.
     *
     * @return A telefonszám TextField értéke
     */
    public String getPhoneValue() {
        return phone.getText();
    }

    /**
     * A telefonszám megadásárára szolgáló TextField értékének beállítása.
     *
     * @param phone A telefonszám, amire állítani szeretnénk
     */
    public void setPhone(Integer phone) {
        this.phone.setText(String.valueOf(phone));
    }
}
