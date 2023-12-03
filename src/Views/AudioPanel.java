package Views;

import Classes.Enums;
import Models.BorrowModel;

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

    private JButton addCover;
    private JButton addAudio;

    /**
     *
     */
    JPanel input;

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
        label.add(new JLabel("Borítókép", SwingConstants.RIGHT));
        label.add(new JLabel("Zeneszám", SwingConstants.RIGHT));
        this.mainPanel.add(label, BorderLayout.WEST);

        // a felhasználó által szerkeszthető komponensek létrehozása és panelhez adása
        input = new JPanel(new GridLayout(0, 1, 2, 2));
        name = new JTextField(10);
        artist = new JTextField(10);
        releaseyear = new JTextField(4);
        style = new JTextField(10);
        type = new JComboBox(Enums.Audiotype.values());
        borrowable = new JCheckBox();
        addCover = new JButton("Tallózás");
        addCover.addActionListener(new addButtonActionListener("png", this));
        addAudio = new JButton("Tallózás");
        addAudio.addActionListener(new addButtonActionListener("wav", this));

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

    class addButtonActionListener implements ActionListener {
        String Extension;
        JPanel parentPanel;
        addButtonActionListener(String ext, JPanel panel){
            Extension = ext;
            parentPanel = panel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(Extension+" fájlok", Extension));
            if(fileChooser.showOpenDialog(parentPanel) == JFileChooser.APPROVE_OPTION){
                if(Extension.equals("png")){
                    File copied = new File("src/Data/Picture/new.png");
                    try {
                        com.google.common.io.Files.copy(fileChooser.getSelectedFile(), copied);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if(Extension.equals("wav")){
                    File copied = new File("src/Data/Audio/new.wav");
                    try {
                        com.google.common.io.Files.copy(fileChooser.getSelectedFile(), copied);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        }

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

    public void removeBorrowableCheckbox(){
        input.remove(borrowable);
    }
}
