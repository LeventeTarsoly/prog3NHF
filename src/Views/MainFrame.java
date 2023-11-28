package Views;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import Models.AudioModel;
import Models.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Date;

public class MainFrame extends JFrame {
    //TODO mainframe
    static JTable audioTable;
    static JTable memberTable;
    JTree rentalTree;
    JPanel leftPanel;
    JPanel lowerPanel;
    JSplitPane horizontalSplitPane;
    JSplitPane verticalSplitPane;
    static AudioModel audioModel = new AudioModel();
    static MemberModel memberModel = new MemberModel();
    public MainFrame(){
        super("Hangkölcsönző");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.horizontalSplitPane.setContinuousLayout(true);
        this.verticalSplitPane.setContinuousLayout(true);
        verticalSplitPane.setDividerLocation(400);
        horizontalSplitPane.setDividerLocation(900);

        /*for (int i = 0; i < 40; i++) {
            audioModel.audios.add(new AudioData());
            memberModel.members.add(new MemberData());
        }*/

        initlowerPanel();
        initleftPanel();
        //TODO rentaltree
        //todo rentaltree refresh when data added, modified
        rentalTree = new JTree();
        rentalTree.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        horizontalSplitPane.add(leftPanel);
        horizontalSplitPane.add(rentalTree);
        //upperPanel.add(horizontalSplitPane, BorderLayout.CENTER);
        verticalSplitPane.add(horizontalSplitPane);
        verticalSplitPane.add(lowerPanel);

        add(verticalSplitPane, BorderLayout.CENTER);
    }

    void initlowerPanel(){
        audioTable = new JTable(audioModel);
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        lowerPanel.setLayout(new BorderLayout());

        JPanel audioButtonsPanel = new JPanel();
        audioButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        audioButtonsPanel.setLayout(new BoxLayout(audioButtonsPanel, BoxLayout.X_AXIS));
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());
        //todo sort, search

        audioButtonsPanel.add(audioadd);
        audioButtonsPanel.add(audiomodify);
        audioButtonsPanel.add(audiodelete);
        lowerPanel.add(audioButtonsPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);
    }

    void initleftPanel(){
        leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5,8,5,5));
        leftPanel.setLayout(new BorderLayout());
        memberTable = new JTable(memberModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        JPanel memberButtonsPanel = new JPanel();
        memberButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        memberButtonsPanel.setLayout(new BoxLayout(memberButtonsPanel, BoxLayout.X_AXIS));
        JButton memberadd  = new JButton("Hozzáadás");
        memberadd.addActionListener(new MemberAddButtonActionListener());
        JButton membermodify = new JButton("Módosítás");
        membermodify.addActionListener(new MemberModifyButtonActionListener());
        JButton memberdelete = new JButton("Törlés");
        memberdelete.addActionListener(new MemberDeleteButtonActionListener());
        //todo rentalhistory, sort, search

        memberButtonsPanel.add(memberadd);
        memberButtonsPanel.add(membermodify);
        memberButtonsPanel.add(memberdelete);
        leftPanel.add(memberButtonsPanel, BorderLayout.NORTH);
        leftPanel.add(memberScrollPane, BorderLayout.CENTER);
    }
    static class AudioAddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();
                    //todo add cover, song
                    audioModel.addAudio(name, artist, releaseyear, style, type, borrowable);
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    static class AudioModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioData audio = audioModel.audios.get(audioTable.getSelectedRow());
            AudioPanel audioPanel = new AudioPanel();
            audioPanel.setNameValue(audio.getName());
            audioPanel.setArtist(audio.getArtist());
            audioPanel.setReleaseyear(audio.getReleaseYear());
            audioPanel.setStyle(audio.getStyle());
            audioPanel.setType(audio.getType());
            audioPanel.setBorrowable(audio.isBorrowable());
            //todo add cover, intro
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();
                    audioModel.removeAudio(audioTable.getSelectedRow());
                    audioModel.addAudio(name, artist, releaseyear, style, type, borrowable);
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    static class AudioDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            audioModel.removeAudio(audioTable.getSelectedRow());
        }
    }

    static class MemberAddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                MemberPanel memberPanel = new MemberPanel();
                if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try{
                        String name = memberPanel.getNameValue();
                        LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                        Integer phone = Integer.parseInt(memberPanel.getPhoneValue());
                        memberModel.addMember(name, dateOfBirth, phone);
                    }
                    catch (Exception E){
                        JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                    }
                }
        }
    }
    static class MemberModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberData member = memberModel.members.get(memberTable.getSelectedRow());
            MemberPanel memberPanel = new MemberPanel();
            memberPanel.setNameValue(member.getName());
            memberPanel.setDateOfBirth(member.getDateOfBirth().toString());
            memberPanel.setPhone(member.getPhoneNum());
            if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag modósitása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try{
                    String name = memberPanel.getNameValue();
                    LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                    Integer phone = Integer.parseInt(memberPanel.getPhoneValue());
                    memberModel.removeMember(memberTable.getSelectedRow());
                    memberModel.addMember(name, dateOfBirth, phone);
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    static class MemberDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memberModel.removeMember(memberTable.getSelectedRow());
        }
    }
}