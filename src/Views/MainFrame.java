package Views;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import Models.AudioModel;
import Models.MemberModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    static JTable audioTable;
    static JTable memberTable;
    static JTree borrowTree;
    JPanel leftPanel;
    JPanel lowerPanel;
    JSplitPane horizontalSplitPane;
    JSplitPane verticalSplitPane;
    static AudioModel audioModel = new AudioModel();
    static MemberModel memberModel = new MemberModel();
    static Map<DefaultMutableTreeNode, MemberData> Nodes = new HashMap<>();
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
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                audioModel.Serialize();
                memberModel.Serialize();
            }
        });

        initleftPanel();
        initlowerPanel();
        initborrowTree();
        //todo rentaltree refresh when data added, modified, deleted
        verticalSplitPane.add(horizontalSplitPane);
        verticalSplitPane.add(lowerPanel);

        add(verticalSplitPane, BorderLayout.CENTER);
    }

    void initlowerPanel(){
        audioModel.DeSerialize();
        audioTable = new JTable(audioModel);
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        lowerPanel.setLayout(new BorderLayout());
        setCombobox();

        JPanel audioButtonsPanel = new JPanel();
        audioButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        //audioButtonsPanel.setLayout(new BoxLayout(audioButtonsPanel, BoxLayout.X_AXIS));
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());
        JButton audioreturn  = new JButton("Visszavetel"); //todo szoveg
        audioreturn.addActionListener(new AudioReturnButtonActionListener());
        //todo sort, search

        audioButtonsPanel.add(audioadd);
        audioButtonsPanel.add(audiomodify);
        audioButtonsPanel.add(audiodelete);
        audioButtonsPanel.add(audioreturn);
        lowerPanel.add(audioButtonsPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);
    }

    static void setCombobox(){
        JComboBox comboBox = new JComboBox();
        comboBox.setRenderer(new BorrowerCellRenderer());
        for (MemberData member:MemberModel.members) {
            comboBox.addItem(member);
        }
        comboBox.addItem(null);
        audioTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox));
    }

    void initleftPanel(){
        memberModel.DeSerialize();
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
        horizontalSplitPane.add(leftPanel);
    }

    void initborrowTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tagok kölcsönzései:");
        List<DefaultMutableTreeNode> members = new ArrayList<>();
        for (MemberData data: MemberModel.members) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(data.getName());
            root.add(node);
            members.add(node);
            Nodes.put(node, data);
        }
        for (DefaultMutableTreeNode node: members) {
            for (AudioData audioData: AudioModel.audios) {
                if(Nodes.get(node).equals(audioData.getBorrower()))
                    node.add(new DefaultMutableTreeNode(audioData.getName()));
            }
        }
        borrowTree = new JTree(root);
        borrowTree.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JScrollPane borrowScrollPane = new JScrollPane(borrowTree);
        horizontalSplitPane.add(borrowScrollPane);
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
                    //todo add cover, intro
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
            AudioPanel audioPanel = new AudioPanel();
            audioPanel.setNameValue(audioModel.audios.get(audioTable.getSelectedRow()).getName());
            audioPanel.setArtist(audioModel.audios.get(audioTable.getSelectedRow()).getArtist());
            audioPanel.setReleaseyear(audioModel.audios.get(audioTable.getSelectedRow()).getReleaseYear());
            audioPanel.setStyle(audioModel.audios.get(audioTable.getSelectedRow()).getStyle());
            audioPanel.setType(audioModel.audios.get(audioTable.getSelectedRow()).getType());
            audioPanel.setBorrowable(audioModel.audios.get(audioTable.getSelectedRow()).getBorrowable());
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
    static class AudioReturnButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            audioModel.audios.get(audioTable.getSelectedRow()).setBorrower(null);
            audioModel.fireTableDataChanged();
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
                        setCombobox();
                        audioModel.fireTableDataChanged();
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
            MemberPanel memberPanel = new MemberPanel();
            memberPanel.setNameValue(memberModel.members.get(memberTable.getSelectedRow()).getName());
            memberPanel.setDateOfBirth(memberModel.members.get(memberTable.getSelectedRow()).getDateOfBirth().toString());
            memberPanel.setPhone(memberModel.members.get(memberTable.getSelectedRow()).getPhoneNum());
            if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag modósitása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try{
                    String name = memberPanel.getNameValue();
                    LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                    Integer phone = Integer.parseInt(memberPanel.getPhoneValue());
                    memberModel.removeMember(memberTable.getSelectedRow());
                    memberModel.addMember(name, dateOfBirth, phone);
                    setCombobox();
                    audioModel.fireTableDataChanged();
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
            setCombobox();
            audioModel.fireTableDataChanged();
            DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
            for (Map.Entry<DefaultMutableTreeNode, MemberData> entry : Nodes.entrySet()) {
                if (entry.getValue().equals(memberTable.getSelectedRow())) {
                    model.removeNodeFromParent(entry.getKey());
                    model.reload();
                }
            }
        }
    }



    public static class BorrowerCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null && value instanceof MemberData) {
                MemberData data = (MemberData) value;
                setText(data.getName());
            }
            return this;
        }
    }


}