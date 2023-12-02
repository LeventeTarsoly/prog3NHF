package Views;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import Models.AudioModel;
import Models.BorrowModel;
import Models.MemberModel;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    JTable audioTable;
    JTable memberTable;
    JTree borrowTree;
    JPanel leftPanel;
    JPanel lowerPanel;
    JSplitPane horizontalSplitPane;
    JSplitPane verticalSplitPane;
    AudioModel audioModel = new AudioModel(new ArrayList<>());
    MemberModel memberModel = new MemberModel();
    DefaultMutableTreeNode root;
    JComboBox StyleFilter;
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
        audioModel.DeSerialize();
        audioTable = new JTable(audioModel);
        audioTable.addPropertyChangeListener(new AudioTablePropertyChangedListener());
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        lowerPanel.setLayout(new BorderLayout());
        setBorrowerCombobox();
        setTypeCombobox();
        //todo doubleckick -> playerframe
        JPanel audioMenuPanel = new JPanel();
        audioMenuPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());
        JButton audioreturn  = new JButton("Visszavétel");
        audioreturn.addActionListener(new AudioReturnButtonActionListener());
        JLabel SortLabel = new JLabel("Szűrés:");

        JButton StyleFilterButton = new JButton("Stílus szerint");
        setStyleCombobox();
        StyleFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(audioModel);
            audioTable.setRowSorter(TableSorter);
            String text = StyleFilter.getSelectedItem().toString();
            if (text.isEmpty()) {
                TableSorter.setRowFilter(null);
            } else {
                TableSorter.setRowFilter(new StringRowFilter(text, 3));
            }
        });

        DefaultComboBoxModel<Enums.Audiotype> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(null);
        comboBoxModel.addAll(List.of(Enums.Audiotype.values()));
        JComboBox TypeFilter = new JComboBox(comboBoxModel);
        JButton TypeFilterButton = new JButton("Típus szerint");
        TypeFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(audioModel);
            audioTable.setRowSorter(TableSorter);
            if(TypeFilter.getSelectedItem()!=null){
                Enums.Audiotype type = (Enums.Audiotype) TypeFilter.getSelectedItem();
                TableSorter.setRowFilter(new AudioTypeRowFilter(type, 4));
            }
            else
                TableSorter.setRowFilter(null);
        });

        String[] borrowables = new String[]{"","Igen","Nem"};
        JComboBox BorrowableFilter = new JComboBox(borrowables);
        JButton BorrowableFilterButton = new JButton("Kölcsönözhetőség szerint");
        BorrowableFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(audioModel);
            audioTable.setRowSorter(TableSorter);
            if(!BorrowableFilter.getSelectedItem().equals(""))
                TableSorter.setRowFilter(new BoolRowFilter(BorrowableFilter.getSelectedItem().equals("Igen"), 5));
            else
                TableSorter.setRowFilter(null);
        });

        JButton NameFilterButton = new JButton("Cím szerint");
        JTextField NameFilter = addTableStringSorter(audioTable,audioModel,NameFilterButton, 15,0);
        JButton ArtistFilterButton = new JButton("Előadó szerint");
        JTextField ArtistFilter = addTableStringSorter(audioTable,audioModel,ArtistFilterButton, 15,1);

        audioMenuPanel.add(audioadd);
        audioMenuPanel.add(audiomodify);
        audioMenuPanel.add(audiodelete);
        audioMenuPanel.add(audioreturn);
        audioMenuPanel.add(SortLabel);
        audioMenuPanel.add(NameFilter);
        audioMenuPanel.add(NameFilterButton);
        audioMenuPanel.add(ArtistFilter);
        audioMenuPanel.add(ArtistFilterButton);
        audioMenuPanel.add(StyleFilter);
        audioMenuPanel.add(StyleFilterButton);
        audioMenuPanel.add(TypeFilter);
        audioMenuPanel.add(TypeFilterButton);
        audioMenuPanel.add(BorrowableFilter);
        audioMenuPanel.add(BorrowableFilterButton);
        lowerPanel.add(audioMenuPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);
    }

    public static JTextField addTableStringSorter(JTable table, TableModel model,JButton filterButton, int TFlength, int column){
        JTextField filter = new JTextField(TFlength);
        filterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(model);
            table.setRowSorter(TableSorter);
            String text = filter.getText();
            if (text.isEmpty()) {
                TableSorter.setRowFilter(null);
            } else {
                TableSorter.setRowFilter(new StringRowFilter(text, column));
            }
        });
        return filter;
    }

    void setBorrowerCombobox(){
        JComboBox comboBox = new JComboBox();
        comboBox.setRenderer(new BorrowerCellRenderer());
        for (MemberData member:MemberModel.getMembers()) {
            comboBox.addItem(member);
        }
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                MemberData selectedMember = (MemberData) comboBox.getSelectedItem();

                int selectedRow = audioTable.getSelectedRow();
                if (selectedRow != -1 && selectedMember!=null) {
                    selectedMember.addBorrow(audioModel.getAudioAt(selectedRow).getId());

                    //memberModel.updateMember(selectedMember);
                    BorrowModel model = (BorrowModel) borrowTree.getModel();
                    model.reload();
                    borrowTree.expandRow(0);
                }
            }
        });
        audioTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox));
    }

    void setStyleCombobox(){
        ArrayList<String> data = new ArrayList<>();
        data.add("");
        for (AudioData audio: audioModel.audios) {
            if(!data.contains(audio.getStyle()))
                data.add(audio.getStyle());
        }
        StyleFilter = new JComboBox(data.toArray());
    }

    void initleftPanel(){
        memberModel.DeSerialize();
        leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5,8,5,5));
        leftPanel.setLayout(new BorderLayout());
        memberTable = new JTable(memberModel);
        memberTable.addPropertyChangeListener(new MemberTablePropertyChangedListener());

        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if(e.getClickCount()==2){
                    MemberData borrower = memberModel.getMemberAt(memberTable.getSelectedRow());
                    new BorrowPanel(borrower);
                }
            }
        });
        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        JPanel memberMenuPanel = new JPanel();
        memberMenuPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        JButton memberadd  = new JButton("Hozzáadás");
        memberadd.addActionListener(new MemberAddButtonActionListener());
        JButton membermodify = new JButton("Módosítás");
        membermodify.addActionListener(new MemberModifyButtonActionListener());
        JButton memberdelete = new JButton("Törlés");
        memberdelete.addActionListener(new MemberDeleteButtonActionListener());

        JButton FilterButton = new JButton("Szűrés név szerint");
        JTextField NameFilter = addTableStringSorter(memberTable,memberModel,FilterButton, 15,0);

        memberMenuPanel.add(memberadd);
        memberMenuPanel.add(membermodify);
        memberMenuPanel.add(memberdelete);
        memberMenuPanel.add(NameFilter);
        memberMenuPanel.add(FilterButton);

        leftPanel.add(memberMenuPanel, BorderLayout.NORTH);
        leftPanel.add(memberScrollPane, BorderLayout.CENTER);
        horizontalSplitPane.add(leftPanel);
    }

    void initborrowTree(){
        root = new DefaultMutableTreeNode("Tagok kölcsönzései:");
        for (MemberData data: MemberModel.getMembers()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(data.getName());
            root.add(node);
        }
        BorrowModel borrowModel = new BorrowModel(root, MemberModel.members, audioModel.audios);
        borrowTree = new JTree(borrowModel);
        borrowTree.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JScrollPane borrowScrollPane = new JScrollPane(borrowTree);
        horizontalSplitPane.add(borrowScrollPane);
    }

    class AudioAddButtonActionListener implements ActionListener {
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
                    setStyleCombobox();
                    //todo add cover, intro
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    class AudioModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            audioPanel.setNameValue(audioModel.audios.get(audioTable.getSelectedRow()).getName());
            audioPanel.setArtist(audioModel.audios.get(audioTable.getSelectedRow()).getArtist());
            audioPanel.setReleaseyear(audioModel.audios.get(audioTable.getSelectedRow()).getReleaseYear());
            audioPanel.setStyle(audioModel.audios.get(audioTable.getSelectedRow()).getStyle());
            audioPanel.setType(audioModel.audios.get(audioTable.getSelectedRow()).getType());
            audioPanel.setBorrowable(audioModel.audios.get(audioTable.getSelectedRow()).getBorrowable());
            MemberData borrower = audioModel.audios.get(audioTable.getSelectedRow()).getBorrower();
            if(borrower!=null)
                audioPanel.removeBorrowableCheckbox();
            //todo add cover, intro
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();

                    audioModel.modifyAudio(audioTable.getSelectedRow(),name, artist, releaseyear, style, type, borrowable);

                    BorrowModel model = (BorrowModel) borrowTree.getModel();
                    model.reload();
                    setBorrowerCombobox();
                    setStyleCombobox();
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    class AudioDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memberModel.removeBorrow(audioModel.getAudioAt(audioTable.getSelectedRow()).getId());
            audioModel.removeAudio(audioTable.getSelectedRow());
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
            setStyleCombobox();
        }
    }
    class AudioReturnButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int removeid = audioModel.getAudioAt(audioTable.getSelectedRow()).getId();
            memberModel.removeBorrow(removeid);
            audioModel.audios.get(audioTable.getSelectedRow()).setBorrower(null);
            audioModel.fireTableDataChanged();
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
            setStyleCombobox();
        }
    }

    class MemberAddButtonActionListener implements ActionListener {
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
                        BorrowModel model = (BorrowModel) borrowTree.getModel();
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
                        root.add(newNode);
                        model.nodeStructureChanged(root);
                        model.reload();
                    }
                    catch (Exception E){
                        JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                    }
                }
        }
    }
    class MemberModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberPanel memberPanel = new MemberPanel();
            MemberData selectedMember = MemberModel.members.get(memberTable.getSelectedRow());
            memberPanel.setNameValue(selectedMember.getName());
            memberPanel.setDateOfBirth(selectedMember.getDateOfBirth().toString());
            memberPanel.setPhone(selectedMember.getPhoneNum());
            if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag módositása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try{
                    String name = memberPanel.getNameValue();
                    LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                    Integer phone = Integer.parseInt(memberPanel.getPhoneValue());

                    memberModel.modifyMember(memberTable.getSelectedRow(), name, dateOfBirth, phone);
                    memberDataChanged(selectedMember);

                    BorrowModel model = (BorrowModel) borrowTree.getModel();
                    model.reload();
                    setBorrowerCombobox();
                    memberModel.fireTableDataChanged();
                    audioModel.fireTableDataChanged();
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    void memberDataChanged(MemberData changedMember){
        for (int id:changedMember.getBorroweds()) {
            for (AudioData audio:audioModel.audios) {
                if(audio.getId()==id)
                    audio.setBorrower(changedMember);
            }
        }
    }

    class MemberDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memberModel.removeMember(memberTable.getSelectedRow());
            setBorrowerCombobox();
            audioModel.fireTableDataChanged();
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
        }
    }

    class AudioTablePropertyChangedListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(borrowTree!=null){
                BorrowModel model = (BorrowModel) borrowTree.getModel();
                model.reload();
            }
        }
    }

    class MemberTablePropertyChangedListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(memberTable.getSelectedRow()!=-1){
                BorrowModel model = (BorrowModel) borrowTree.getModel();
                model.reload();
                MemberData selectedMember = MemberModel.members.get(memberTable.getSelectedRow());
                memberDataChanged(selectedMember);
                setBorrowerCombobox();
                audioModel.fireTableDataChanged();
            }
        }
    }

    class BorrowerCellRenderer extends DefaultListCellRenderer {
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

    static class StringRowFilter extends RowFilter{

        String Filter;
        int Column;
        StringRowFilter(String filter, int column){
            Filter = filter;
            Column = column;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getStringValue(Column).contains(Filter);
        }
    }

    static class BoolRowFilter extends RowFilter{

        Boolean Filter;
        int Column;
        BoolRowFilter(Boolean filter, int column){
            Filter = filter;
            Column = column;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getValue(Column).equals(Filter);
        }
    }

    static class AudioTypeRowFilter extends RowFilter{

        Enums.Audiotype Filter;
        int Column;
        AudioTypeRowFilter(Enums.Audiotype filter, int column){
            Filter = filter;
            Column = column;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getValue(Column).equals(Filter);
        }
    }
}