package Models;

import Classes.AudioData;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AudioModel extends AbstractTableModel {
    List<AudioData> audios = new ArrayList<AudioData>();
    @Override
    public int getRowCount() {
        return audios.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }
    //TODO audio table getvalueat
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
