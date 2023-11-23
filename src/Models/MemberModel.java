package Models;

import Classes.AudioData;
import Classes.MemberData;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MemberModel extends AbstractTableModel {
    List<MemberData> members = new ArrayList<MemberData>();
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
        return null;
    }
}
