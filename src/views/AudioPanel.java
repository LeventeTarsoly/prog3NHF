package views;

import classes.Enums;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
     * Az anyag megjelenési évének szövegmezője
     */
    private final JTextField releaseyear;

    /**
     * Az anyag stílusának szövegmezője
     */
    private final JTextField style;

    /**
     * Az anyag típusának kombinált doboza
     */
    private final JComboBox type;
    /**
     * Az anyag kölcsönözhetőségének jelölőnégyzete
     */
    private final JCheckBox borrowable;

    /**
     * Az input panele
     */
    final JPanel input;

    /**
     * Audiopanel létrehozása
     */
    AudioPanel() {
        this.mainPanel = new JPanel(new BorderLayout(5, 5));

        //labelek létrehozása és panelhez adása
        JPanel label = new JPanel(new GridLayout(0, 1));
        label.add(new JLabel("Cím", SwingConstants.RIGHT));
        label.add(new JLabel("Előadó", SwingConstants.RIGHT));
        label.add(new JLabel("Kiadás éve", SwingConstants.RIGHT));
        label.add(new JLabel("Stílus", SwingConstants.RIGHT));
        label.add(new JLabel("Típus", SwingConstants.RIGHT));
        label.add(new JLabel("Kölcsönözhető", SwingConstants.RIGHT));
        label.add(new JLabel("Borítókép", SwingConstants.RIGHT));
        label.add(new JLabel("Zeneszám", SwingConstants.RIGHT));
        this.mainPanel.add(label, BorderLayout.WEST);

        //szerkeszthető komponensek létrehozása
        name = new JTextField(10);
        artist = new JTextField(10);
        releaseyear = new JTextField(4);
        style = new JTextField(10);
        type = new JComboBox(Enums.Audiotype.values());
        borrowable = new JCheckBox();
        JButton addCover = new JButton("Tallózás");
        addCover.addActionListener(new addButtonActionListener("png", this));
        JButton addAudio = new JButton("Tallózás");
        addAudio.addActionListener(new addButtonActionListener("wav", this));
        //szerkeszthető komponensek inputhoz adása
        input = new JPanel(new GridLayout(0, 1));
        input.add(name);
        input.add(artist);
        input.add(releaseyear);
        input.add(style);
        input.add(type);
        input.add(borrowable);
        input.add(addCover);
        input.add(addAudio);

        mainPanel.add(input, BorderLayout.CENTER);
    }

    /**
     * Tallózáshoz használt gomb ActionListenerje
     */
    static class addButtonActionListener implements ActionListener {
        /**
         * Kiterjesztés
         */
        final String Extension;
        /**
         * A hívó panel
         */
        final JPanel ParentPanel;

        /**
         * ActionListener létrehozása
         *
         * @param ext         a kiterjesztés
         * @param parentPanel a hívó panel
         */
        addButtonActionListener(String ext, JPanel parentPanel) {
            Extension = ext;
            ParentPanel = parentPanel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            //Fájl választó osztály
            JFileChooser fileChooser = new JFileChooser();
            //Beállítja a kiterjesztést
            fileChooser.setFileFilter(new FileNameExtensionFilter(Extension+" fájlok", Extension));
            //Ha hozzáadott újat a felhasználó
            if (fileChooser.showOpenDialog(ParentPanel) == JFileChooser.APPROVE_OPTION) {
                //ha a kiterjesztés .png
                if(Extension.equals("png")){
                    File copied = new File("src/Data/Picture/new.png");
                    try {
                        //Google Guava segítsével átmásolja a kiválasztott filet a program Data/Picture mappájába
                        com.google.common.io.Files.copy(fileChooser.getSelectedFile(), copied);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                //ha a kiterjesztés .wav
                else if(Extension.equals("wav")){
                    File copied = new File("src/Data/Audio/new.wav");
                    try {
                        //Google Guava segítsével átmásolja a kiválasztott filet a program Data/Audio mappájába
                        com.google.common.io.Files.copy(fileChooser.getSelectedFile(), copied);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        }

    }

    /**
     * Visszaadja a fő panelt
     *
     * @return a main panel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Visszaadja a cím szövegmezőben megadott értéket.
     *
     * @return A cím megadására szolgáló TextField tartalma
     */
    public String getNameValue() {
        return name.getText();
    }

    /**
     * Beállítja a cím megadására való TextField értékét.
     *
     * @param name A cím, amire állítani szeretnénk a TextField-et
     */
    public void setNameValue(String name) {
        this.name.setText(name);
    }

    /**
     * Lekéri a szövegmezőből az előadó nevét
     *
     * @return az előadó neve
     */
    public String getArtist() {
        return artist.getText();
    }

    /**
     * Beállítja alpaértelmezett értékként a jelenleg megadott előadó nevet
     *
     * @param artist az előadó neve
     */
    public void setArtist(String artist) {
        this.artist.setText(artist);
    }

    /**
     * Visszaadja a megjelenési év szövegmezőben megadott értéket.
     *
     * @return a megjelenési év
     */
    public Integer getReleaseYear() {
        return Integer.parseInt(releaseyear.getText());
    }

    /**
     * Beállítja a megjelenési év megadására való TextField értékét.
     *
     * @param year a megjelenési év
     */
    public void setReleaseyear(Integer year) {
        releaseyear.setText(String.valueOf(year));
    }

    /**
     * Visszaadja a stílus szövegmezőben megadott értéket.
     *
     * @return a stílus
     */
    public String getStyle() {
        return style.getText();
    }

    /**
     * Beállítja a stílus megadására való TextField értékét.
     *
     * @param style a stílus
     */
    public void setStyle(String style){
        this.style.setText(style);
    }

    /**
     * Visszaadja a típus kombinált dobozban megadott értéket.
     *
     * @return a típus audiotype
     */
    public Enums.Audiotype getType() {
        return (Enums.Audiotype) type.getSelectedItem();
    }

    /**
     * Beállítja a típus megadására való ComboBox értékét.
     *
     * @param type a típus
     */
    public void setType(Enums.Audiotype type){
        this.type.setSelectedItem(type);
    }

    /**
     * Lekéri, hogy kölcsönözhető-e az anyag
     *
     * @return igaz - kölcsönözhető, hamis - nem
     */
    public Boolean isBorrowable() {
        return borrowable.isSelected();
    }

    /**
     * Beállítja a kölcsönözhetőséget a CheckBox szerint
     *
     * @param isSelected ki van-e választva a doboz
     */
    public void setBorrowable(Boolean isSelected){
        borrowable.setSelected(isSelected);
    }

    /**
     * Kitörli a kölcsönözhetőséget állító jelölőnégyzetet, mert valaki
     * kikölcsönözte, és ekkor ez nem állítható
     */
    public void removeBorrowableCheckbox(){
        input.remove(borrowable);
    }
}
