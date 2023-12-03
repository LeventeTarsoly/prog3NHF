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
        //lejátszásbeli időt kijelző Label
        JLabel currentTime = new JLabel("00:00");

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        sliderPanel.add(currentTime);


        AudioPlayer player = new AudioPlayer(AUDIOLOCATION);
        player.play();
        playerButton.addActionListener(e -> {
            if (player.isPlaying)
                player.pause();
            else
                player.play();
        });
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, (int) player.clip.getMicrosecondLength(), 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                long value = slider.getValue();
                player.jump(value);
            }
        });

        sliderPanel.add(slider);

        JLabel endTime = new JLabel(String.valueOf(player.getTime() / 1000000));
        sliderPanel.add(endTime);

        playerPanel.add(playerButton, BorderLayout.WEST);
        playerPanel.add(sliderPanel);
        add(playerPanel, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.stop();
            }
        });
    }

    /**
     * Resize image buffered image.
     *
     * @param originalImage the original image
     * @param targetWidth   the target width
     * @param targetHeight  the target height
     * @return the buffered image
     */
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    /**
     * Gets image.
     *
     * @param filename the filename
     * @param width    the width
     * @param length   the length
     * @return the image
     */
    BufferedImage getImage(String filename, int width, int length) {
        BufferedImage img;
        //todo slider, timer follows clip
        String PICTURELOCATION = "src/Data/Picture/";
        try {
            img = ImageIO.read(new File(PICTURELOCATION + filename));
            img = resizeImage(img, width, length);
        } catch (IOException e) {
            try {
                img = ImageIO.read(new File(PICTURELOCATION + "BaseBackground.png"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return img;
    }

    private static class AudioPlayer {
        /**
         * The Current frame.
         */
        Long currentFrame;
        /**
         * The Clip.
         */
        Clip clip;
        /**
         * The Is playing.
         */
        boolean isPlaying = false;
        /**
         * The Audio input stream.
         */
        AudioInputStream audioInputStream;


        /**
         * Instantiates a new Audio player.
         *
         * @param path the path
         */
        public AudioPlayer(String path) {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        /**
         * Is playing boolean.
         *
         * @return the boolean
         */
        public boolean isPlaying() {
            return isPlaying;
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
                currentFrame = clip.getMicrosecondPosition();
            }
        }

        /**
         * Jump.
         *
         * @param c the c
         */
        public void jump(long c) {
            if (c >= 0 && c <= clip.getMicrosecondLength()) {
                clip.stop();
                clip.setMicrosecondPosition(c);
                currentFrame = c;
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            }
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        public Long getTime() {
            return clip.getMicrosecondLength();
        }

        /**
         * Stop.
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
