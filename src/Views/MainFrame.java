package Views;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import Models.AudioModel;
import Models.BorrowModel;
import Models.MemberModel;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    static DefaultMutableTreeNode root;
    static MemberModel memberModel = new MemberModel();
    static Map<DefaultMutableTreeNode, MemberData> MemberNodes = new HashMap<>();
    static Map<DefaultMutableTreeNode, AudioData> AudioNodes = new HashMap<>();
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
        verticalSplitPane.add(horizontalSplitPane);
        verticalSplitPane.add(lowerPanel);

        add(verticalSplitPane, BorderLayout.CENTER);
    }

    void initlowerPanel(){
        //todo tree reload when data modified in table
        audioModel.DeSerialize();
        audioTable = new JTable(audioModel);
        audioTable.addPropertyChangeListener(new audioTablePropertyChangedListener());
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        lowerPanel.setLayout(new BorderLayout());
        setBorrowerCombobox();

        JPanel audioButtonsPanel = new JPanel();
        audioButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        //audioButtonsPanel.setLayout(new BoxLayout(audioButtonsPanel, BoxLayout.X_AXIS));
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());
        JButton audioreturn  = new JButton("Visszavétel");
        audioreturn.addActionListener(new AudioReturnButtonActionListener());
        //todo audiotable sort, search

        audioButtonsPanel.add(audioadd);
        audioButtonsPanel.add(audiomodify);
        audioButtonsPanel.add(audiodelete);
        audioButtonsPanel.add(audioreturn);
        lowerPanel.add(audioButtonsPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);
    }
//todo type combobox
    static void setBorrowerCombobox(){
        JComboBox comboBox = new JComboBox();
        comboBox.setRenderer(new BorrowerCellRenderer());
        for (MemberData member:MemberModel.members) {
            comboBox.addItem(member);
        }
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                MemberData selectedMember = (MemberData) comboBox.getSelectedItem();

                int selectedRow = audioTable.getSelectedRow();
                if (selectedRow != -1 && selectedMember!=null) {
                    selectedMember.addBorrow(audioModel.getBorrow(selectedRow));

                    //memberModel.updateMember(selectedMember);
                    DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
                    model.reload();
                    borrowTree.expandRow(0);
                }
            }
        });
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
        root = new DefaultMutableTreeNode("Tagok kölcsönzései:");
        List<DefaultMutableTreeNode> members = new ArrayList<>();
        for (MemberData data: MemberModel.members) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(data.getName());
            root.add(node);
            members.add(node);
            MemberNodes.put(node, data);
        }
        for (DefaultMutableTreeNode membernode: members) {
            for (AudioData audioData: AudioModel.audios) {
                if(MemberNodes.get(membernode).equals(audioData.getBorrower())){
                    DefaultMutableTreeNode audionode =  new DefaultMutableTreeNode(audioData.getName());
                    membernode.add(audionode);
                    AudioNodes.put(audionode, audioData);
                }
            }
        }
        BorrowModel borrowModel = new BorrowModel(new DefaultMutableTreeNode("Tagok kölcsönzései:"), MemberModel.members);
        borrowTree = new JTree(borrowModel);
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

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
                    audioModel.addAudio(name, artist, releaseyear, style, type, borrowable, null);
                    AudioNodes.put(node, AudioModel.audios.get(AudioModel.audios.size()-1));
                    //todo add cover, intro
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    static class AudioModifyButtonActionListener implements ActionListener {
        //todo tree reload audiomodify
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            audioPanel.setNameValue(AudioModel.audios.get(audioTable.getSelectedRow()).getName());
            audioPanel.setArtist(AudioModel.audios.get(audioTable.getSelectedRow()).getArtist());
            audioPanel.setReleaseyear(AudioModel.audios.get(audioTable.getSelectedRow()).getReleaseYear());
            audioPanel.setStyle(AudioModel.audios.get(audioTable.getSelectedRow()).getStyle());
            audioPanel.setType(AudioModel.audios.get(audioTable.getSelectedRow()).getType());
            audioPanel.setBorrowable(AudioModel.audios.get(audioTable.getSelectedRow()).getBorrowable());
            MemberData borrower = AudioModel.audios.get(audioTable.getSelectedRow()).getBorrower();
            if(borrower!=null)
                audioPanel.removeBorrowable();
            //todo add cover, intro
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();

                    DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
                    DefaultMutableTreeNode currentNode = getcurrentNode(AudioModel.audios.get(audioTable.getSelectedRow()), AudioNodes);
                    //model.removeNodeFromParent(currentNode);
                    //model.nodeChanged(currentNode);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);

                    audioModel.removeAudio(audioTable.getSelectedRow());

                    audioModel.addAudio(name, artist, releaseyear, style, type, borrowable, borrower);
                    model.reload();
                    //MemberNodes.put(node, MemberModel.members.get(MemberModel.members.size()-1));
                    setBorrowerCombobox();
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
            DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
            DefaultMutableTreeNode currentNode = getcurrentNode(AudioModel.audios.get(audioTable.getSelectedRow()), AudioNodes);
            model.removeNodeFromParent(currentNode);
            model.reload();
            audioModel.removeAudio(audioTable.getSelectedRow());
        }
    }
    static class AudioReturnButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
            DefaultMutableTreeNode currentNode = getcurrentNode(AudioModel.audios.get(audioTable.getSelectedRow()), AudioNodes);
            model.removeNodeFromParent(currentNode);
            model.reload();
            AudioModel.audios.get(audioTable.getSelectedRow()).setBorrower(null);
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
                        setBorrowerCombobox();
                        audioModel.fireTableDataChanged();
                        DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
                        model.reload();
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
            memberPanel.setNameValue(MemberModel.members.get(memberTable.getSelectedRow()).getName());
            memberPanel.setDateOfBirth(MemberModel.members.get(memberTable.getSelectedRow()).getDateOfBirth().toString());
            memberPanel.setPhone(MemberModel.members.get(memberTable.getSelectedRow()).getPhoneNum());
            if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag módositása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try{
                    String name = memberPanel.getNameValue();
                    LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                    Integer phone = Integer.parseInt(memberPanel.getPhoneValue());

                    DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
                    DefaultMutableTreeNode currentNode = getcurrentNode(MemberModel.members.get(memberTable.getSelectedRow()), MemberNodes);
                    model.removeNodeFromParent(currentNode);
                    memberModel.removeMember(memberTable.getSelectedRow());
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
                    root.add(node);
                    model.reload();
                    memberModel.addMember(name, dateOfBirth, phone);
                    MemberNodes.put(node, MemberModel.members.get(MemberModel.members.size()-1));
                    setBorrowerCombobox();
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
            setBorrowerCombobox();
            audioModel.fireTableDataChanged();
            DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
            model.reload();
        }
    }

    static <T> DefaultMutableTreeNode getcurrentNode(T data,Map<DefaultMutableTreeNode, T> Nodes){
        for (Map.Entry<DefaultMutableTreeNode, T> entry : Nodes.entrySet()) {
            if (data.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    static class audioTablePropertyChangedListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            DefaultTreeModel model = (DefaultTreeModel) borrowTree.getModel();
            model.reload();
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