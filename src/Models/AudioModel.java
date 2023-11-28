package Models;

import Classes.AudioData;
import Classes.Enums;
import Classes.MemberData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioModel extends AbstractTableModel implements Serializable {
    public static List<AudioData> audios = new ArrayList<>();
    @Override
    public int getRowCount() {
        return audios.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        AudioData audio = audios.get(rowIndex);
        if (columnIndex == 6) {
                for(MemberData member : MemberModel.members){
                    if(member.equals(audio.getBorrower()))
                        return member.getName();
                }
        }
        switch(columnIndex) {
            case 0: return audio.getName();
            case 1: return audio.getArtist();
            case 2: return audio.getReleaseYear();
            case 3: return audio.getStyle();
            case 4: return audio.getType();
            case 5: return audio.getBorrowable();
            default: return null;
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
        if(columnIndex==6 && !audios.get(rowIndex).getBorrowable())
            return false;
        else if(columnIndex==5 && audios.get(rowIndex).getBorrower()!=null)
            return false;

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
        if(columnIndex==6){
            if(aValue!=null){
                audios.get(rowIndex).setBorrower((MemberData) aValue);
                audios.get(rowIndex).setBorrowable(false);
                fireTableDataChanged();
            }
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
    public void DeSerialize(){
        File file = new File("src/Data/Audios.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setLenient().create();
        String jsonArray;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            jsonArray = br.readLine();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AudioData[] array = gson.fromJson(jsonArray, AudioData[].class);
        audios.addAll(Arrays.asList(array));
    }
    public void Serialize(){
        File file = new File("src/Data/Audios.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setLenient().create();
        AudioData[] array = audios.toArray(new AudioData[0]);
        String jsonArray = gson.toJson(array);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
