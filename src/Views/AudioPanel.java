package Views;

import Classes.Enums;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Egy audioanyag létrehozásához vagy módosításához használható panel
 */
public class AudioPanel extends JPanel{
    /**
     * Az adatok megadásának panele.
     */
    private final JPanel mainPanel;

    /**
     * Az anyag nevének szövegmezője.
     */
    private final JTextField name;

    /**
     * Az előadó nevének szövegmezője.
     */
    private final JTextField artist;

    /**
     *
     */
    private final JTextField releaseyear;

    /**
     *
     */
    private final JTextField style;

    /**
     *
     */
    private final JComboBox type;
    /**
     *
     */
    private final JCheckBox borrowable;

    AudioPanel() {
        this.mainPanel = new JPanel(new BorderLayout(5, 5));

        // Labelek létrehozása és panelhez adása
        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Cím", SwingConstants.RIGHT));
        label.add(new JLabel("Előadó", SwingConstants.RIGHT));
        label.add(new JLabel("Kiadás éve", SwingConstants.RIGHT));
        label.add(new JLabel("Stílus", SwingConstants.RIGHT));
        label.add(new JLabel("Típus", SwingConstants.RIGHT));
        label.add(new JLabel("Kölcsönözhető", SwingConstants.RIGHT));
        this.mainPanel.add(label, BorderLayout.WEST);

        // a felhasználó által szerkeszthető komponensek létrehozása és panelhez adása
        JPanel input = new JPanel(new GridLayout(0, 1, 2, 2));
        this.name = new JTextField(10);
        artist = new JTextField(10);
        releaseyear = new JTextField(4);
        style = new JTextField(10);
        type = new JComboBox(Enums.Audiotype.values());
        borrowable = new JCheckBox();

        this.mainPanel.add(input, BorderLayout.CENTER);
        input.add(name);
        input.add(artist);
        input.add(releaseyear);
        input.add(style);
        input.add(type);
        input.add(borrowable);
    }
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

    public String getArtist(){ return artist.getText();}
    public void setArtist(String artist) {
        this.artist.setText(artist);
    }
    public Integer getReleaseYear(){return Integer.parseInt(releaseyear.getText());}

    public void setReleaseyear(Integer yr){
        releaseyear.setText(String.valueOf(yr));
    }

    public String getStyle(){return style.getText();}

    public void setStyle(String style){
        this.style.setText(style);
    }

    public Enums.Audiotype getType(){return (Enums.Audiotype) type.getSelectedItem();}

    public void setType(Enums.Audiotype type){
        this.type.setSelectedItem(type);
    }

    public Boolean isBorrowable(){return borrowable.isSelected();}

    public void setBorrowable(Boolean isSelected){
        borrowable.setSelected(isSelected);
    }
}
