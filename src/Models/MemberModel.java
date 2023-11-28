package Models;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberModel extends AbstractTableModel {
    public List<MemberData> members = new ArrayList<MemberData>();
    @Override
    public int getRowCount() {
        return members.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MemberData member = members.get(rowIndex);
        switch(columnIndex) {
            case 0: return member.getName();
            case 1: return member.getDateOfBirth();
            case 2: return member.getPhoneNum();
            default: return member.getId();
        }
    }

    public String getColumnName(int column){
        String res = "";
        switch (column) {
            case 0 -> res="Név";
            case 1 -> res="Születési dátum";
            case 2 -> res="Telefonszám";
        }
        return res;
    }

    public Class<?> getColumnClass(int column){
        switch(column) {
            case 1: return LocalDate.class;
            case 2: return Integer.class;
            default: return String.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0 -> members.get(rowIndex).setName((String) aValue);
            case 1 -> members.get(rowIndex).setDateOfBirth((LocalDate) aValue);
            case 2 -> members.get(rowIndex).setPhoneNum((Integer) aValue);
        }
    }

    public void addMember(String name, LocalDate dateOfBirth, Integer phoneNum) {
		 members.add(new MemberData(members.size(), name, dateOfBirth, phoneNum));
		 fireTableRowsInserted(0, members.size()-1);
    }

    public void removeMember(int idx) {
        members.remove(idx);
        fireTableRowsInserted(0, members.size()-1);
    }

    //todo deserialize
    public void DeSerialize(){

    }
    //todo serialize
    public void Serialize(){

    }
}
