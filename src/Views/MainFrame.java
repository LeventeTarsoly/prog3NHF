package Views;

import Classes.AudioData;
import Classes.MemberData;
import Models.AudioModel;
import Models.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    //TODO MainFrame
    JTable audioTable;
    JTable memberTable;
    JTree rentalTree;
    JPanel upperPanel;
    JPanel lowerPanel;
    JSplitPane horizontalSplitPane;
    JSplitPane verticalSplitPane;
    public MainFrame(){
        super("Hangkölcsönző");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.horizontalSplitPane.setContinuousLayout(true);
        this.verticalSplitPane.setContinuousLayout(true);

        MemberModel memberModel = new MemberModel();
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberModel.members.add(new MemberData());
        memberTable = new JTable(memberModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        AudioModel audioModel = new AudioModel();
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioModel.audios.add(new AudioData());
        audioTable = new JTable(audioModel);
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        audioScrollPane.setSize(JFrame.WIDTH, 300);

        JTree jTree = new JTree();

        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        JPanel leftPanel = new JPanel();

        JPanel audioButtonsPanel = new JPanel();
        BoxLayout audioButtonsPanelLayout = new BoxLayout(audioButtonsPanel, BoxLayout.X_AXIS);
        audioButtonsPanel.setLayout(audioButtonsPanelLayout);
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());

        JButton memberadd  = new JButton("Hozzáadás");
        memberadd.addActionListener(new MemberAddButtonActionListener());
        JButton membermodify = new JButton("Módosítás");
        membermodify.addActionListener(new MemberModifyButtonActionListener());
        JButton memberdelete = new JButton("Törlés");
        memberdelete.addActionListener(new MemberDeleteButtonActionListener());

        audioButtonsPanel.add(audioadd);
        audioButtonsPanel.add(audiomodify);
        audioButtonsPanel.add(audiodelete);
        lowerPanel.add(audioButtonsPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);

        leftPanel.add(memberadd);
        leftPanel.add(membermodify);
        leftPanel.add(memberdelete);
        leftPanel.add(memberScrollPane);

        horizontalSplitPane.add(leftPanel);
        horizontalSplitPane.add(jTree);
        upperPanel.add(horizontalSplitPane, BorderLayout.CENTER);
        verticalSplitPane.add(upperPanel);
        verticalSplitPane.add(lowerPanel);

        add(verticalSplitPane, BorderLayout.CENTER);
    }

    static class AudioAddButtonActionListener implements ActionListener {
        //TODO audioadd button
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Tag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                /*String name = memberPanel.getNameValue();
                String dob = memberPanel.getDateOfBirthValue();
                String phone = memberPanel.getPhoneValue();*/
            }
        }
    }
    static class AudioModifyButtonActionListener implements ActionListener {
        //TODO audiomodify button
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class AudioDeleteButtonActionListener implements ActionListener {
        //TODO audiodelete button
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    static class MemberAddButtonActionListener implements ActionListener {
        //TODO Memberadd button
        @Override
        public void actionPerformed(ActionEvent e) {
                MemberPanel memberPanel = new MemberPanel();
                if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    String name = memberPanel.getNameValue();
                    String dob = memberPanel.getDateOfBirthValue();
                    String phone = memberPanel.getPhoneValue();
                }
        }
    }
    static class MemberModifyButtonActionListener implements ActionListener {
        //TODO Membermodify button
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class MemberDeleteButtonActionListener implements ActionListener {
        //TODO Memberdelete button
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}