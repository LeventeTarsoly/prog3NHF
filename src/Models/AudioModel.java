package Models;

import Classes.AudioData;
import Classes.Enums;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AudioModel extends AbstractTableModel {
    public List<AudioData> audios = new ArrayList<AudioData>();
    @Override
    public int getRowCount() {
        return audios.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AudioData audio = audios.get(rowIndex);
        switch(columnIndex) {
            case 0: return audio.getName();
            case 1: return audio.getArtist();
            case 2: return audio.getReleaseYear();
            case 3: return audio.getStyle();
            case 4: return audio.getType();
            case 5: return audio.isBorrowable();
            case 6: return audio.getBorrowerName();
            default: return audio.getId();
        }
    }

    public String getColumnName(int column){
        String res = "";
        switch (column) {
            case 0 -> res="Cím";
            case 1 -> res="Előadó";
            case 2 -> res="Kiadás éve";
            case 3 -> res="Stílus";
            case 4 -> res="Típus";
            case 5 -> res="Kölcsönözhető";
            case 6 -> res="Kölcsönző";
        }
        return res;
    }

    public Class<?> getColumnClass(int column){
        switch(column) {
            case 2: return Integer.class;
            case 4: return Enums.Audiotype.class;
            case 5: return Boolean.class;
            default: return String.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0 -> audios.get(rowIndex).setName((String) aValue);
            case 1 -> audios.get(rowIndex).setArtist((String) aValue);
            case 2 -> audios.get(rowIndex).setReleaseYear((Integer) aValue);
            case 3 -> audios.get(rowIndex).setStyle((String) aValue);
            case 4 -> audios.get(rowIndex).setType((Enums.Audiotype) aValue);
            case 5 -> audios.get(rowIndex).setBorrowable((Boolean) aValue);
        }
    }

    public void addAudio(String name, String artist, Integer releaseyear, String style, Enums.Audiotype type, Boolean borrowable) {
		 audios.add(new AudioData(audios.size(),name, artist, releaseyear, style, type, borrowable));
		 fireTableRowsInserted(0, audios.size()-1);
    }

    public void removeAudio(int idx){
        audios.remove(idx);
        fireTableRowsInserted(0, audios.size()-1);
    }
    //todo deserialize
    public void DeSerialize(){

    }
    //todo serialize
    public void Serialize(){

    }

}
