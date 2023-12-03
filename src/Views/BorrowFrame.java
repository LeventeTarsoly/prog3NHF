package Views;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import Models.AudioModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Views.MainFrame.addTableStringSorter;

public class BorrowFrame extends JFrame {
    private JTable rentalHistoryTable;
    JPanel borrowMenuPanel = new JPanel();

    public BorrowFrame(MemberData selectedMember) {
        super("Kölcsönzési történet");

        // Initialize the table with rental history data
        initRentalHistoryTable(selectedMember);

        // Set up the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1300, 300));
        setResizable(false);
        add(borrowMenuPanel, BorderLayout.NORTH);
        add(new JScrollPane(rentalHistoryTable), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initRentalHistoryTable(MemberData selectedMember) {
        // Assuming you have a method to get the rental history of a member
        List<AudioData> rentalHistory = getRentalHistory(selectedMember);

        // Create a table model with rental history data
        TableModel rentalHistoryTableModel = new TableModel() {
            public List<AudioData> borrows = rentalHistory;
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
                switch(columnIndex) {
                    case 2: return Integer.class;
                    case 4: return Enums.Audiotype.class;
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                AudioData audio = borrows.get(rowIndex);

                switch(columnIndex) {
                    case 0: return audio.getName();
                    case 1: return audio.getArtist();
                    case 2: return audio.getReleaseYear();
                    case 3: return audio.getStyle();
                    case 4: return audio.getType();
                    default: return null;
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                //Automatically added but not used
            }

            @Override
            public void addTableModelListener(TableModelListener l) {
                //Automatically added but not used
            }

            @Override
            public void removeTableModelListener(TableModelListener l) {
                //Automatically added but not used
            }
        };

        // Create the table
        rentalHistoryTable = new JTable(rentalHistoryTableModel);

        JLabel SortLabel = new JLabel("Szűrés:");
        JButton StyleFilterButton = new JButton("Stílus szerint");
        ArrayList<String> data = new ArrayList<>();
        data.add("");
        for (AudioData audio: AudioModel.audios) {
            if(!data.contains(audio.getStyle()))
                data.add(audio.getStyle());
        }

        JComboBox StyleFilter = new JComboBox(data.toArray());
        StyleFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(rentalHistoryTableModel);
            rentalHistoryTable.setRowSorter(TableSorter);
            String text = StyleFilter.getSelectedItem().toString();
            if (text.isEmpty()) {
                TableSorter.setRowFilter(null);
            } else {
                TableSorter.setRowFilter(new MainFrame.StringRowFilter(text, 3));
            }
        });

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
                TableSorter.setRowFilter(new MainFrame.AudioTypeRowFilter(type, 4));
            }
            else
                TableSorter.setRowFilter(null);
        });

        String[] borrowables = new String[]{"","Igen","Nem"};
        JComboBox BorrowableFilter = new JComboBox(borrowables);
        JButton BorrowableFilterButton = new JButton("Kölcsönözhetőség szerint");
        BorrowableFilterButton.addActionListener(e -> {
            TableRowSorter<TableModel> TableSorter = new TableRowSorter<>(rentalHistoryTableModel);
            rentalHistoryTable.setRowSorter(TableSorter);
            if(!BorrowableFilter.getSelectedItem().equals(""))
                TableSorter.setRowFilter(new MainFrame.BoolRowFilter(BorrowableFilter.getSelectedItem().equals("Igen"), 5));
            else
                TableSorter.setRowFilter(null);
        });

        JButton NameFilterButton = new JButton("Cím szerint");
        JTextField NameFilter = addTableStringSorter(rentalHistoryTable,rentalHistoryTableModel,NameFilterButton, 15,0);
        JButton ArtistFilterButton = new JButton("Előadó szerint");
        JTextField ArtistFilter = addTableStringSorter(rentalHistoryTable,rentalHistoryTableModel,ArtistFilterButton, 15,1);

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

    private List<AudioData> getRentalHistory(MemberData selectedMember) {
        // Implement this method to get the rental history of the selected member
        // For example, you can iterate over all audios and find the ones that were borrowed by the member
        List<AudioData> rentalHistory = new ArrayList<>();
        for (AudioData audio : AudioModel.audios) {
            if (audio.getBorrower() != null && audio.getBorrower().equals(selectedMember)) {
                rentalHistory.add(audio);
            }
        }
        return rentalHistory;
    }
}
