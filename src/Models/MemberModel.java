package Models;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;

import javax.swing.table.AbstractTableModel;
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
    //TODO member table getvalueat
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
            case 1: return Date.class;
            case 2: return Integer.class;
            default: return String.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /*public void addStudent(String name, String neptun) {
		 students.add(new Student(name, neptun, false, 0));
		 fireTableRowsInserted(0, students.size()-1);
	 }
	 */
}
