package views;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import models.AudioModel;
import models.BorrowModel;
import models.MemberModel;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fő Frame
 */
public class MainFrame extends JFrame {
    /**
     * A hanganyagok táblája
     */
    JTable audioTable;
    /**
     * A tagok táblája
     */
    JTable memberTable;
    /**
     * A kölcsönzésekhez tartozó fa
     */
    JTree borrowTree;
    /**
     * A tagok tábláját és az azokhoz tartozó komponenseket tartalmazó Panel
     */
    JPanel leftPanel;
    /**
     * A hanganyagok tábláját és az azokhoz tartozó komponenseket tartalmazó Panel
     */
    JPanel lowerPanel;
    /**
     * Vízszintes elválaszó Panel
     */
    final JSplitPane horizontalSplitPane;
    /**
     * Függőleges elválasztó Panel
     */
    final JSplitPane verticalSplitPane;
    /**
     * Az anyagok táblájához tartozó model
     */
    final AudioModel audioModel = new AudioModel(new ArrayList<>());
    /**
     * A tagok táblájához tartozó model
     */
    final MemberModel memberModel = new MemberModel(new ArrayList<>());
    /**
     * A fához tartozó gyökér
     */
    DefaultMutableTreeNode root;
    /**
     * A stílushoz tartozó filter ComboBoxa
     */
    JComboBox StyleFilter;

    /**
     * A fő Frame létrehozása
     */
    public MainFrame(){
        super("Hangkölcsönző");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.horizontalSplitPane.setContinuousLayout(true);
        this.verticalSplitPane.setContinuousLayout(true);
        verticalSplitPane.setDividerLocation(400);
        horizontalSplitPane.setDividerLocation(900);
        //ha bezárod az ablakot elmenti az adatokat
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                audioModel.Serialize("src/Data/Audios.json");
                memberModel.Serialize("src/Data/Members.json");
            }
        });

        initleftPanel();
        initlowerPanel();
        initborrowTree();
        verticalSplitPane.add(horizontalSplitPane);
        verticalSplitPane.add(lowerPanel);

        add(verticalSplitPane, BorderLayout.CENTER);
    }

    /**
     * A hanganyagok Panelének létrehozása
     */
    void initlowerPanel(){
        //inicializálja a táblát és a fő Panelt
        audioModel.DeSerialize("src/Data/Audios.json");
        audioTable = new JTable(audioModel);
        audioTable.addPropertyChangeListener(new AudioTablePropertyChangedListener());
        JScrollPane audioScrollPane = new JScrollPane(audioTable);
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        lowerPanel.setLayout(new BorderLayout());

        setBorrowerCombobox();
        setTypeCombobox();

        //A tábla feletti komponenseket tartalmazó Panel
        JPanel audioMenuPanel = new JPanel();
        audioMenuPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        //Új hozzáadásához tartozó gomb
        JButton audioadd  = new JButton("Hozzáadás");
        audioadd.addActionListener(new AudioAddButtonActionListener());
        //Kiválasztott elem módosításához tartozó gomb
        JButton audiomodify = new JButton("Módosítás");
        audiomodify.addActionListener(new AudioModifyButtonActionListener());
        //Kiválasztott elem törléséhez tartozó gomb
        JButton audiodelete = new JButton("Törlés");
        audiodelete.addActionListener(new AudioDeleteButtonActionListener());
        //Kiválasztott elem kölcsönzésének törléséhez tartozó gomb
        JButton audioreturn  = new JButton("Visszavétel");
        audioreturn.addActionListener(new AudioReturnButtonActionListener());
        //Kiválasztott elemnek megnyílik a lejátszó, ha van megadott audio file
        JButton playAudio  = new JButton("Lejátszás");
        playAudio.addActionListener(e -> {
            AudioData audio = audioModel.getAudioAt(audioTable.getSelectedRow());
            File f = new File("src/Data/Audio/" + audio.getId() + ".wav");
            if (f.exists()) {
                PlayerFrame playerFrame = new PlayerFrame(String.valueOf(audio.getId()), audio.getName());
                playerFrame.setVisible(true);
            }
        });

        //Létrehozza a szűréshez a komponenseket
        //Szűrés Label
        JLabel SortLabel = new JLabel("Szűrés:");

        //Cím szerinti szűrés gombja, szövegdoboza
        JButton NameFilterButton = new JButton("Cím szerint");
        JTextField NameFilter = addTableStringSorter(audioTable,audioModel,NameFilterButton, 5,0);

        //Előadó szerinti szűrés gombja, szövegdoboza
        JButton ArtistFilterButton = new JButton("Előadó szerint");
        JTextField ArtistFilter = addTableStringSorter(audioTable,audioModel,ArtistFilterButton, 5,1);

        //Stílus szerinti szűrés gombja, szövegdoboza
        JButton StyleFilterButton = new JButton("Stílus szerint");
        setStyleCombobox();
        StyleFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(audioModel);
            audioTable.setRowSorter(TableSorter);
            String text = Objects.requireNonNull(StyleFilter.getSelectedItem()).toString();
            if (text.isEmpty()) {
                TableSorter.setRowFilter(null);
            } else {
                TableSorter.setRowFilter(new StringRowFilter(text, 3));
            }
        });

        //Típus szerinti szűrés gombja, ComboBoxa
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
                TableSorter.setRowFilter(new AudioTypeRowFilter(type));
            }
            else
                TableSorter.setRowFilter(null);
        });

        //Kölcsönözhetőség szerinti szűrés gombja, ComboBoxa
        //Azért kell a ComboBox, hogy ki lehessen kapcsolni a szűrést
        String[] borrowables = new String[]{"","Igen","Nem"};
        JComboBox BorrowableFilter = new JComboBox(borrowables);
        JButton BorrowableFilterButton = new JButton("Kölcsönözhetőség szerint");
        BorrowableFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(audioModel);
            audioTable.setRowSorter(TableSorter);
            if (!Objects.requireNonNull(BorrowableFilter.getSelectedItem()).equals(""))
                TableSorter.setRowFilter(new BoolRowFilter(BorrowableFilter.getSelectedItem().equals("Igen")));
            else
                TableSorter.setRowFilter(null);
        });

        //A menü Panelhez hozzáadja a komponenseket
        audioMenuPanel.add(audioadd);
        audioMenuPanel.add(audiomodify);
        audioMenuPanel.add(audiodelete);
        audioMenuPanel.add(audioreturn);
        audioMenuPanel.add(playAudio);
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

        //Az alsó fő panelhez is hozzáadja a táblát, és a menüsort
        lowerPanel.add(audioMenuPanel, BorderLayout.NORTH);
        lowerPanel.add(audioScrollPane, BorderLayout.CENTER);
    }

    /**
     * String szerinti szűrő a táblához
     *
     * @param table        a tábla
     * @param model        a tábla modelje
     * @param filterButton a szűréshez tartozó gomb
     * @param TFlength     a textfield hossza
     * @param column       a szűrni kívánt oszlop
     * @return a szövegdoboz, ami szerint szűr
     */
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

    /**
     * A táblában a kölcsönzésekhez használt ComboBox inicializálása
     */
    void setBorrowerCombobox(){
        JComboBox comboBox = new JComboBox();
        /*
        betölti a tagokat a listából
         */
        comboBox.setRenderer(new BorrowerCellRenderer());
        for (MemberData member:memberModel.getMembers()) {
            comboBox.addItem(member);
        }
        comboBox.addItemListener(e -> {
            MemberData selectedMember = (MemberData) comboBox.getSelectedItem();

            int selectedRow = audioTable.getSelectedRow();
            /*
            ha van kiválasztott sor és az ahhoz tartozó kiválasztott combobox nem üres, akkor
            a taghoz hozzáadja a kölcsönzést és frissíti a kölcsönzési fát
            */
            if (selectedRow != -1 && selectedMember != null) {
                selectedMember.addBorrow(audioModel.getAudioAt(selectedRow));

                BorrowModel model = (BorrowModel) borrowTree.getModel();
                model.reload();
                borrowTree.expandRow(0);
            }
        });
        audioTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox));
    }

    /**
     * A táblában a típushoz használt ComboBox inicializálása
     */
    void setTypeCombobox(){
        JComboBox comboBox = new JComboBox(Enums.Audiotype.values());
        audioTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));
    }

    /**
     * A stílus szűréséhez használt ComboBox inicializálása
     */
    void setStyleCombobox(){
        ArrayList<String> data = new ArrayList<>();
        data.add("");
        for (AudioData audio : audioModel.getAudios()) {
            if(!data.contains(audio.getStyle()))
                data.add(audio.getStyle());
        }
        StyleFilter = new JComboBox(data.toArray());
    }

    /**
     * A tagok Panelének létrehozása
     */
    void initleftPanel(){
        /*
        inicializálja a táblát és a fő Panelt
         */
        memberModel.DeSerialize("src/Data/Members.json");
        leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5,8,5,5));
        leftPanel.setLayout(new BorderLayout());
        memberTable = new JTable(memberModel);
        memberTable.addPropertyChangeListener(new MemberTablePropertyChangedListener());

        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        //A tábla feletti komponenseket tartalmazó Panel
        JPanel memberMenuPanel = new JPanel();
        memberMenuPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        //Új hozzáadásához tartozó gomb
        JButton memberadd  = new JButton("Hozzáadás");
        memberadd.addActionListener(new MemberAddButtonActionListener());
        //Kiválasztott elem módosításához tartozó gomb
        JButton membermodify = new JButton("Módosítás");
        membermodify.addActionListener(new MemberModifyButtonActionListener());
        //Kiválasztott elem törléséhez tartozó gomb
        JButton memberdelete = new JButton("Törlés");
        memberdelete.addActionListener(new MemberDeleteButtonActionListener());
        //Kiválasztott elem kölcsönzési történetét mutató Frame megnyitása
        JButton openHistory = new JButton("Kölcsönzések");
        openHistory.addActionListener(e -> {
            MemberData borrower = memberModel.getMemberAt(memberTable.getSelectedRow());
            new BorrowFrame(borrower, audioModel);
        });

        //Létrehozza a név szerinti szűréshez a komponenseket
        JButton FilterButton = new JButton("Szűrés név szerint");
        JTextField NameFilter = addTableStringSorter(memberTable,memberModel,FilterButton, 15,0);

        //A menü Panelhez hozzáadja a komponenseket
        memberMenuPanel.add(memberadd);
        memberMenuPanel.add(membermodify);
        memberMenuPanel.add(memberdelete);
        memberMenuPanel.add(openHistory);
        memberMenuPanel.add(NameFilter);
        memberMenuPanel.add(FilterButton);

        //Az alsó fő panelhez is hozzáadja a táblát, és a menüsort
        leftPanel.add(memberMenuPanel, BorderLayout.NORTH);
        leftPanel.add(memberScrollPane, BorderLayout.CENTER);
        horizontalSplitPane.add(leftPanel);
    }

    /**
     * A kölcsönzések fájának inicializálása
     */
    void initborrowTree(){
        root = new DefaultMutableTreeNode("Tagok kölcsönzései:");
        BorrowModel borrowModel = new BorrowModel(root, memberModel.getMembers(), audioModel.getAudios());
        borrowTree = new JTree(borrowModel);
        borrowTree.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JScrollPane borrowScrollPane = new JScrollPane(borrowTree);
        horizontalSplitPane.add(borrowScrollPane);
    }

    /**
     * Új hanganyag felvitelének gombjához tartozó Listener
     */
    class AudioAddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            //Meghív egy audioPanelt
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    //lekéri a bevitt adatokat
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();

                    //létrehozza az új hanganyagot
                    audioModel.addAudio(name, artist, releaseyear, style, type, borrowable, null);
                    //frissíti a stílus szerinti szűrő comboboxot
                    setStyleCombobox();
                    //beállítja a fileok neveit, ha lettek létrehozva
                    File audioFile = new File("src/Data/Audio/new.wav");
                    if(audioFile.exists())
                        audioFile.renameTo(new File("src/Data/Audio/"+audioModel.getLastAudio().getId()+".wav"));
                    File coverFile = new File("src/Data/Picture/new.png");
                    if(coverFile.exists())
                        coverFile.renameTo(new File("src/Data/Picture/"+audioModel.getLastAudio().getId()+".png"));
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * Hanganyag módosításának gombjához tartozó Listener
     */
    class AudioModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = new AudioPanel();
            //betölti az eredeti adatokat
            audioPanel.setNameValue(audioModel.getAudioAt(audioTable.getSelectedRow()).getName());
            audioPanel.setArtist(audioModel.getAudioAt(audioTable.getSelectedRow()).getArtist());
            audioPanel.setReleaseyear(audioModel.getAudioAt(audioTable.getSelectedRow()).getReleaseYear());
            audioPanel.setStyle(audioModel.getAudioAt(audioTable.getSelectedRow()).getStyle());
            audioPanel.setType(audioModel.getAudioAt(audioTable.getSelectedRow()).getType());
            audioPanel.setBorrowable(audioModel.getAudioAt(audioTable.getSelectedRow()).getBorrowable());
            MemberData borrower = audioModel.getAudioAt(audioTable.getSelectedRow()).getBorrower();
            //ha van kölcsönző, kiszedi a kölcsönözhetőség checkboxát
            if(borrower!=null)
                audioPanel.removeBorrowableCheckbox();
            //Meghív egy audioPanelt
            if (JOptionPane.showConfirmDialog(null, audioPanel.getMainPanel(), "Hanganyag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    //lekéri a bevitt adatokat
                    String name = audioPanel.getNameValue();
                    String artist = audioPanel.getArtist();
                    Integer releaseyear = audioPanel.getReleaseYear();
                    String style = audioPanel.getStyle();
                    Enums.Audiotype type = audioPanel.getType();
                    Boolean borrowable = audioPanel.isBorrowable();

                    //módosítja a kiválasztott hanganyagot
                    audioModel.modifyAudio(audioTable.getSelectedRow(),name, artist, releaseyear, style, type, borrowable);

                    //Frissíti a fát
                    BorrowModel model = (BorrowModel) borrowTree.getModel();
                    model.reload();

                    //frissíti a stílus szerinti szűrő comboboxot
                    setStyleCombobox();

                    //beállítja a fileok neveit, ha lettek létrehozva
                    File audioFile = new File("src/Data/Audio/new.wav");
                    if(audioFile.exists())
                        audioFile.renameTo(new File("src/Data/Audio/"+audioModel.getAudioAt(audioTable.getSelectedRow()).getId()+".wav"));
                    File coverFile = new File("src/Data/Picture/new.png");
                    if(coverFile.exists())
                        coverFile.renameTo(new File("src/Data/Picture/"+audioModel.getAudioAt(audioTable.getSelectedRow()).getId()+".png"));
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * Hanganyag törlésének gombjához tartozó Listener
     */
    class AudioDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //ha volt tallózva neki file, azt is törli
            File audioFile = new File("src/Data/Audio/"+audioModel.getAudioAt(audioTable.getSelectedRow()).getId()+".wav");
            if(audioFile.exists())
                audioFile.delete();
            File coverFile = new File("src/Data/Picture/"+audioModel.getAudioAt(audioTable.getSelectedRow()).getId()+".png");
            if(coverFile.exists())
                coverFile.delete();
            //ha törlünk egy hanganyagot, a kölcsönzések közül is törölni kell
            memberModel.removeBorrow(audioModel.getAudioAt(audioTable.getSelectedRow()).getId());
            audioModel.removeAudio(audioTable.getSelectedRow());
            //fa frissítése
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
            setStyleCombobox();
        }
    }

    /**
     * Hanganyag kölcsönzésének megszüntetésének gombjához tartozó Listener
     */
    class AudioReturnButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //kitörli a kölcsönző kölcsönzött adatai közül
            int removeid = audioModel.getAudioAt(audioTable.getSelectedRow()).getId();
            memberModel.removeBorrow(removeid);
            //kitörli a kölcsönzést a hanganyagból
            audioModel.getAudioAt(audioTable.getSelectedRow()).setBorrower(null);
            audioModel.fireTableDataChanged();
            //frissíti a fát
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
            setStyleCombobox();
        }
    }

    /**
     * Új tag felvitelének gombjához tartozó Listener
     */
    class MemberAddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                MemberPanel memberPanel = new MemberPanel();
            //Meghív egy memberPanelt
                if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag hozzáadása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try{
                        //lekéri a bevitt adatokat
                        String name = memberPanel.getNameValue();
                        LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                        String phone = memberPanel.getPhoneValue();

                        //létrehozza az új tagot
                        memberModel.addMember(name, dateOfBirth, phone);
                        //frissíti a kölcsönzőkhöz tartozó comboboxot
                        setBorrowerCombobox();
                        audioModel.fireTableDataChanged();
                        //frissíti a fát
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

    /**
     * Tag módosításának gombjához tartozó Listener
     */
    class MemberModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberPanel memberPanel = new MemberPanel();
            //betölti az eredeti adatokat
            MemberData selectedMember = memberModel.getMembers().get(memberTable.getSelectedRow());
            memberPanel.setNameValue(selectedMember.getName());
            memberPanel.setDateOfBirth(selectedMember.getDateOfBirth().toString());
            memberPanel.setPhone(selectedMember.getPhoneNum());
            //Meghív egy memberPanelt
            if (JOptionPane.showConfirmDialog(null, memberPanel.getMainPanel(), "Tag módositása", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try{
                    //lekéri a bevitt adatokat
                    String name = memberPanel.getNameValue();
                    LocalDate dateOfBirth = LocalDate.parse(memberPanel.getDateOfBirthValue());
                    String phone = memberPanel.getPhoneValue();

                    //módosítja a kiválasztott tagot
                    memberModel.modifyMember(memberTable.getSelectedRow(), name, dateOfBirth, phone);
                    memberDataChanged(selectedMember);

                    //frissíti a kölcsönzőkhöz tartozó comboboxot
                    setBorrowerCombobox();
                    audioModel.fireTableDataChanged();
                    //Frissíti a fát
                    BorrowModel model = (BorrowModel) borrowTree.getModel();
                    model.reload();
                    memberModel.fireTableDataChanged();
                }
                catch (Exception E){
                    JOptionPane.showMessageDialog(null, "Rossz formátum", "Hibás formátum", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * Ha módosul egy tag adata, akkor azt átállítja az anyagok táblájában is
     *
     * @param changedMember a módosult tag
     */
    void memberDataChanged(MemberData changedMember){
        for (int id:changedMember.getBorroweds()) {
            for (AudioData audio : audioModel.getAudios()) {
                if(audio.getId()==id)
                    audio.setBorrower(changedMember);
            }
        }
    }

    /**
     * Tag törlésének gombjához tartozó Listener
     */
    class MemberDeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memberModel.removeMember(audioModel, memberTable.getSelectedRow());
            //frissíti a kölcsönzők comboboxát a hanganyagok táblájában
            setBorrowerCombobox();
            audioModel.fireTableDataChanged();
            //frissíti ad fát
            BorrowModel model = (BorrowModel) borrowTree.getModel();
            model.reload();
        }
    }

    /**
     * A hanganyagok tábblájának módosulásához tartozó listener
     */
    class AudioTablePropertyChangedListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(borrowTree!=null){
                //frissíti a fát
                BorrowModel model = (BorrowModel) borrowTree.getModel();
                model.reload();
            }
        }
    }

    /**
     * A hanganyagok tábblájának módosulásához tartozó listener
     */
    class MemberTablePropertyChangedListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(memberTable.getSelectedRow()!=-1){
                //frissíti a fát
                BorrowModel model = (BorrowModel) borrowTree.getModel();
                model.reload();
                //frissíti a hanganyok tábláját
                MemberData selectedMember = memberModel.getMembers().get(memberTable.getSelectedRow());
                memberDataChanged(selectedMember);
                setBorrowerCombobox();
                audioModel.fireTableDataChanged();
            }
        }
    }

    /**
     * A kölcsönzők kivaálasztásához használt comboboxhoz tartozó renderer
     */
    static class BorrowerCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MemberData data) {
                setText(data.getName());
            }
            return this;
        }
    }

    /**
     * String típusú oszlopok szűréséhez használt RowFilter
     */
    static class StringRowFilter extends RowFilter{

        /**
         * a filter
         */
        final String Filter;
        /**
         * az oszlop sorszáma
         */
        final int Column;

        /**
         * konstuktoe
         *
         * @param filter a filter
         * @param column a kiválaszott oszlop
         */
        StringRowFilter(String filter, int column){
            Filter = filter;
            Column = column;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getStringValue(Column).contains(Filter);
        }
    }

    /**
     * Bool típusú oszlopok szűréséhez használt RowFilter
     */
    static class BoolRowFilter extends RowFilter{

        /**
         * a filter
         */
        final Boolean Filter;
        /**
         * az oszlop sorszáma
         */
        final int Column;

        /**
         * konstruktor
         * csak a kölcsönzések oszlopához használt
         *
         * @param filter a filter
         */
        BoolRowFilter(Boolean filter) {
            Filter = filter;
            Column = 5;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getValue(Column).equals(Filter);
        }
    }

    /**
     * A hanganyak típusának szűréséhez használt RowFilter
     */
    static class AudioTypeRowFilter extends RowFilter{

        /**
         * A Filter.
         */
        final Enums.Audiotype Filter;
        /**
         * az oszlop sorszáma
         */
        final int Column;

        /**
         * konstruktor
         * csak a típus oszlophoz használt
         *
         * @param filter a filter
         */
        AudioTypeRowFilter(Enums.Audiotype filter) {
            Filter = filter;
            Column = 4;
        }
        @Override
        public boolean include(Entry entry) {
            return entry.getValue(Column).equals(Filter);
        }
    }
}