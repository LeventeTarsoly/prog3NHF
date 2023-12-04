package views;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import models.AudioModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static views.MainFrame.addTableStringSorter;

/**
 * A kölcsönzési történetet megjelenítő Frame
 */
public class BorrowFrame extends JFrame {
    private JTable rentalHistoryTable;
    /**
     * A Frame fő panele
     */
    final JPanel borrowMenuPanel = new JPanel();

    AudioModel audioModel;

    /**
     * BorrowFrame létehozása
     *
     * @param selectedMember a kiválasztott, tag, akinek a kölcsönzéseit
     *                       jeleníti meg a Frame
     */
    public BorrowFrame(MemberData selectedMember, AudioModel audioModel) {
        super("Kölcsönzési történet");
        this.audioModel=audioModel;

        initRentalHistoryTable(selectedMember);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1300, 300));
        setResizable(false);
        add(borrowMenuPanel, BorderLayout.NORTH);
        add(new JScrollPane(rentalHistoryTable), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Inicializálja a táblát az adatokkal
     */

    private void initRentalHistoryTable(MemberData selectedMember) {
        List<AudioData> rentalHistory = selectedMember.getBorrowedHistory();

        // A rentalHistory listából csinál egy Modelt a táblához
        TableModel rentalHistoryTableModel = new TableModel() {
            public final List<AudioData> borrows = rentalHistory;
            @Override
            public int getRowCount() {
                return borrows.size();
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int columnIndex) {
                String res = "";
                switch (columnIndex) {
                    case 0 -> res="Cím";
                    case 1 -> res="Előadó";
                    case 2 -> res="Kiadás éve";
                    case 3 -> res="Stílus";
                    case 4 -> res="Típus";
                }
                return res;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 2 -> Integer.class;
                    case 4 -> Enums.Audiotype.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                AudioData audio = borrows.get(rowIndex);

                return switch (columnIndex) {
                    case 0 -> audio.getName();
                    case 1 -> audio.getArtist();
                    case 2 -> audio.getReleaseYear();
                    case 3 -> audio.getStyle();
                    case 4 -> audio.getType();
                    default -> null;
                };
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                //Automatikusan generált, de nem használt metódus
            }

            @Override
            public void addTableModelListener(TableModelListener l) {
                //Automatikusan generált, de nem használt metódus
            }

            @Override
            public void removeTableModelListener(TableModelListener l) {
                //Automatikusan generált, de nem használt metódus
            }
        };

        // Tábla létrehozása a Model segítségével
        rentalHistoryTable = new JTable(rentalHistoryTableModel);


        //Létrehozza a szűréshez a komponenseket
        //Szűrés Label
        JLabel SortLabel = new JLabel("Szűrés:");

        //Stílus szerinti szűrés gombja, szövegdoboza
        JButton StyleFilterButton = new JButton("Stílus szerint");
        ArrayList<String> data = new ArrayList<>();
        data.add("");
        for (AudioData audio: audioModel.getAudios()) {
            if(!data.contains(audio.getStyle()))
                data.add(audio.getStyle());
        }
        JComboBox StyleFilter = new JComboBox(data.toArray());
        StyleFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(rentalHistoryTableModel);
            rentalHistoryTable.setRowSorter(TableSorter);
            String text = Objects.requireNonNull(StyleFilter.getSelectedItem()).toString();
            if (text.isEmpty()) {
                TableSorter.setRowFilter(null);
            } else {
                TableSorter.setRowFilter(new MainFrame.StringRowFilter(text, 3));
            }
        });

        //Típus szerinti szűrés gombja, ComboBoxa
        DefaultComboBoxModel<Enums.Audiotype> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(null);
        comboBoxModel.addAll(List.of(Enums.Audiotype.values()));
        JComboBox TypeFilter = new JComboBox(comboBoxModel);
        JButton TypeFilterButton = new JButton("Típus szerint");
        TypeFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(rentalHistoryTableModel);
            rentalHistoryTable.setRowSorter(TableSorter);
            if(TypeFilter.getSelectedItem()!=null){
                Enums.Audiotype type = (Enums.Audiotype) TypeFilter.getSelectedItem();
                TableSorter.setRowFilter(new MainFrame.AudioTypeRowFilter(type));
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
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(rentalHistoryTableModel);
            rentalHistoryTable.setRowSorter(TableSorter);
            if (!Objects.requireNonNull(BorrowableFilter.getSelectedItem()).equals(""))
                TableSorter.setRowFilter(new MainFrame.BoolRowFilter(BorrowableFilter.getSelectedItem().equals("Igen")));
            else
                TableSorter.setRowFilter(null);
        });

        //Cím szerinti szűrés gombja, szövegdoboza
        JButton NameFilterButton = new JButton("Cím szerint");
        JTextField NameFilter = addTableStringSorter(rentalHistoryTable,rentalHistoryTableModel,NameFilterButton, 15,0);
        //Előadó szerinti szűrés gombja, szövegdoboza
        JButton ArtistFilterButton = new JButton("Előadó szerint");
        JTextField ArtistFilter = addTableStringSorter(rentalHistoryTable,rentalHistoryTableModel,ArtistFilterButton, 15,1);

        //A felső menühoz hozzáadja a szűréshez szükséges komponenseket
        borrowMenuPanel.add(SortLabel);
        borrowMenuPanel.add(NameFilter);
        borrowMenuPanel.add(NameFilterButton);
        borrowMenuPanel.add(ArtistFilter);
        borrowMenuPanel.add(ArtistFilterButton);
        borrowMenuPanel.add(StyleFilter);
        borrowMenuPanel.add(StyleFilterButton);
        borrowMenuPanel.add(TypeFilter);
        borrowMenuPanel.add(TypeFilterButton);
        borrowMenuPanel.add(BorrowableFilter);
        borrowMenuPanel.add(BorrowableFilterButton);
    }

    /**
     * Lekéri a tag történetét és egy listába teszi a kölcsönzött anyagokat
     *
     * @param selectedMember a kiválasztott tag a táblából
     * @return a kiválasztott tag kölcsönzött anyagainak tisája
     */
    private List<AudioData> getRentalHistory(MemberData selectedMember) {
        List<AudioData> rentalHistory = new ArrayList<>();
        for (AudioData audio : audioModel.getAudios()) {
            if (audio.getBorrower() != null && audio.getBorrower().equals(selectedMember)) {
                rentalHistory.add(audio);
            }
        }
        return rentalHistory;
    }
}
