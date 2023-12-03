package views;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A lejátszó Frameje
 */
public class PlayerFrame extends JFrame {

    /**
     * Lejátszó inicializálása
     *
     * @param id   hanganyag idja
     * @param name hanganyag neve
     */
    public PlayerFrame(String id, String name) {
        super(name);
        String AUDIOLOCATION = "src/Data/Audio/" + id + ".wav";
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 500);
        //megnyitja a képet
        BufferedImage myPicture = getImage(id + ".png", 400, 400);
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        add(picLabel, BorderLayout.NORTH);
        //lejátszó komponenseket tartalmazó panele
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setSize(400, 100);
        //play/pause gomb
        JButton playerButton = new JButton();
        BufferedImage buttonImage = getImage("ButtonIcon.png", 61, 61);
        playerButton.setSize(61, 61);
        playerButton.setIcon(new ImageIcon(buttonImage));
        playerButton.setBorder(BorderFactory.createEmptyBorder());

        //todo slider, timer follows clip
        //Időket jelző labelek és a slider panelja
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        //lejátszásbeli időt kijelző Label
        JLabel currentTime = new JLabel("00:00");

        //meghívja a lejátszót
        AudioPlayer player = new AudioPlayer(AUDIOLOCATION);
        player.play();
        //play/pause gomb listenerje
        playerButton.addActionListener(e -> {
            if (player.isPlaying)
                player.pause();
            else
                player.play();
        });
        //zenében ugráláshoz használt slider
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, (int) player.clip.getMicrosecondLength(), 0);
        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                long value = slider.getValue();
                player.jump(value);
            }
        });
        //zene végét jelző Label
        JLabel endTime = new JLabel(String.valueOf(player.getTime() / 1000000));

        //sliderpanelhez hozzáadja a komponenseket
        sliderPanel.add(currentTime);
        sliderPanel.add(slider);
        sliderPanel.add(endTime);

        //menühöz hozzáadja a komponenseket
        playerPanel.add(playerButton, BorderLayout.WEST);
        playerPanel.add(sliderPanel);
        add(playerPanel, BorderLayout.SOUTH);
        //ha bezárja a framet a zenét is leállítja
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.stop();
            }
        });
    }

    /**
     * Újraméretezi a képet
     *
     * @param originalImage a formázandó kép
     * @param targetWidth   a célszélesség
     * @param targetHeight  a célmagasság
     * @return a formázott kép
     */
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    /**
     * Megkeresi és méretezi a képet
     *
     * @param filename the filename
     * @param width    the width
     * @param length   the length
     * @return the image
     */
    BufferedImage getImage(String filename, int width, int length) {
        BufferedImage img;
        String PICTURELOCATION = "src/Data/Picture/";
        try {
            img = ImageIO.read(new File(PICTURELOCATION + filename));
            img = resizeImage(img, width, length);
        } catch (IOException e) {
            try {
                //ha nem találja az eredeti képet, egy stoc kép lesz helyette
                img = ImageIO.read(new File(PICTURELOCATION + "BaseBackground.png"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return img;
    }

    private static class AudioPlayer {
        /**
         * hanganyag lejátszására használt Clip
         */
        Clip clip;
        /**
         * bool arra, hogy éppen megy-e a zene
         */
        boolean isPlaying = false;
        /**
         * AudioInputStream
         */
        AudioInputStream audioInputStream;


        /**
         * AudioPlayer létrehozása
         *
         * @param path a zene elérési útja
         */
        public AudioPlayer(String path) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //loopban megy amíg be nem zárják
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        /**
         * Play.
         */
        public void play() {
            if (!isPlaying) {
                clip.start();
                isPlaying = true;
            }
        }

        /**
         * Pause.
         */
        public void pause() {
            if (isPlaying) {
                clip.stop();
                isPlaying = false;
            }
        }

        /**
         * Egy időre ugrik
         *
         * @param jumpTo az idő, ahova ugornia kell
         */
        public void jump(long jumpTo) {
            if (jumpTo >= 0 && jumpTo <= clip.getMicrosecondLength()) {
                clip.stop();
                clip.setMicrosecondPosition(jumpTo);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            }
        }

        /**
         * Megadja, milyen hosszú a szám
         *
         * @return szám hossza
         */
        public Long getTime() {
            return clip.getMicrosecondLength();
        }

        /**
         * mikor bezáródik a Frame, le kell állítani a lejátszást
         */
        public void stop() {
            clip.stop();
            clip.close();
            try {
                audioInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
