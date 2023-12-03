package Views;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerFrame extends JFrame {
    //todo slider, timer follows clip
    private final String PICTURELOCATION = "src/Data/Picture/";
    private final String AUDIOLOCATION;

    public PlayerFrame(String id, String name) {
        super(name);
        AUDIOLOCATION = "src/Data/Audio/" + id + ".wav";
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 500);
        BufferedImage myPicture = getImage(id + ".png", 400, 400);
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        add(picLabel, BorderLayout.NORTH);
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setSize(400, 100);
        JButton playerButton = new JButton();
        BufferedImage buttonImage = getImage("ButtonIcon.png", 61, 61);

        playerButton.setSize(61, 61);
        playerButton.setIcon(new ImageIcon(buttonImage));
        playerButton.setBorder(BorderFactory.createEmptyBorder());

        JLabel currentTime = new JLabel("00:00");

        playerPanel.add(playerButton, BorderLayout.WEST);
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

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    long value = slider.getValue();
                    player.jump(value);
                }
            }
        });

        sliderPanel.add(slider);

        JLabel endTime = new JLabel(String.valueOf(player.getTime() / 1000000));
        sliderPanel.add(endTime);
        playerPanel.add(sliderPanel);
        add(playerPanel, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.stop();
            }
        });
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    BufferedImage getImage(String filename, int width, int length) {
        BufferedImage img = null;
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

    private class AudioPlayer {
        Long currentFrame;
        Clip clip;
        boolean isPlaying = false;
        AudioInputStream audioInputStream;


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

        public boolean isPlaying() {
            return isPlaying;
        }

        public void play() {
            if (!isPlaying) {
                clip.start();
                isPlaying = true;
            }
        }

        public void pause() {
            if (isPlaying) {
                clip.stop();
                isPlaying = false;
                currentFrame = clip.getMicrosecondPosition();
            }
        }

        public void jump(long c) {
            if (c >= 0 && c <= clip.getMicrosecondLength()) {
                clip.stop();
                clip.setMicrosecondPosition(c);
                currentFrame = c;
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            }
        }

        public Long getTime() {
            return clip.getMicrosecondLength();
        }

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
