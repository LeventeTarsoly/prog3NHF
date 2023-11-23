package Views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //TODO MainFrame
    JTable audioTable;
    JTable memberTable;
    JTree rentalTree;
    JPanel upperPanel;
    JPanel lowerPanel;
    public MainFrame(){
        super("Hangkölcsönző");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(350, 150);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        audioTable = new JTable();
        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        lowerPanel.add(audioTable);

        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.SOUTH);
    }
}