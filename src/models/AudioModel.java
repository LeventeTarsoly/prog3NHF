package models;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A hanganyagok táblájához tartozó Model
 */
public class AudioModel extends AbstractTableModel implements Serializable {
    /**
     * Az anyagok adatainak listája
     */
    private ArrayList<AudioData> audios;

    /**
     * Új lista létrehozása
     *
     * @param data Az új anyagok listája
     */
    public AudioModel(ArrayList<AudioData> data){
        audios = data;
    }

    /**
     * Új AudioData létrehozásakor használt ID számláló,
     * hogy ne kapjon az új anyag olyan ID-t, ami már létezik
     */
    int idcnt = 0;

    public  ArrayList<AudioData> getAudios() {
        return (ArrayList<AudioData>) audios;
    }

    @Override
    public int getRowCount() {
        return audios.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    /**
     * Visszaadja a paraméterben megadott helyen levő AudioDatát a listából
     *
     * @param idx a kiválasztandó anyag indexe
     * @return A listából idxedik helyen kiszedett AudioData
     */
    public AudioData getAudioAt(int idx) {
        return audios.get(idx);
    }

    /**
     * Visszaadja az utolsó AudioDatát a listából
     *
     * @return Az utolsó AudioData
     */
    public AudioData getLastAudio(){
        return audios.get(audios.size() - 1);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        AudioData audio = audios.get(rowIndex);
        //ha a kölcsönző oszlopát kell visszaadni, meg kel nézni, van-e neki egyáltalán
        if (columnIndex == 6) {
            return audio.getBorrower()!=null ? audio.getBorrower().getName() : null;
        }
        return switch (columnIndex) {
            case 0 -> audio.getName();
            case 1 -> audio.getArtist();
            case 2 -> audio.getReleaseYear();
            case 3 -> audio.getStyle();
            case 4 -> audio.getType();
            case 5 -> audio.getBorrowable();
            default -> null;
        };
    }

    public String getColumnName(int column){
        String res;
        switch (column) {
            case 0 -> res="Cím";
            case 1 -> res="Előadó";
            case 2 -> res="Kiadás éve";
            case 3 -> res="Stílus";
            case 4 -> res="Típus";
            case 5 -> res="Kölcsönözhető";
            case 6 -> res="Kölcsönző";
            default -> res="";
        }
        return res;
    }

    public Class<?> getColumnClass(int column){
        return switch (column) {
            case 2 -> Integer.class;
            case 4 -> Enums.Audiotype.class;
            case 5 -> Boolean.class;
            default -> String.class;
        };
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex==6 && !audios.get(rowIndex).getBorrowable())
            return false;
        else return columnIndex != 5 || audios.get(rowIndex).getBorrower() == null;
    }

    //ha a könyvet kikölcsönzik, nem lehet utána értelmszerűen kikölcsönözni,amíg vissza nem hozzák
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
            }
        }
        fireTableDataChanged();
    }

    /**
     * Új AudioData felvétele a listához
     * Ehhez létrehoz egy új AudioDatát, és ezt teszi bele a listába
     *
     * @param name        új anyag címe
     * @param artist      új anyag előadója
     * @param releaseyear új anyag kiadásának éve
     * @param style       új anyag stílusa
     * @param type        új anyag típusa
     * @param borrowable  új anyag kölcsönözhetősége
     * @param borrower    új anyag kölcsönzője
     */
    public void addAudio(String name, String artist, Integer releaseyear, String style, Enums.Audiotype type, Boolean borrowable, MemberData borrower) {
		 audios.add(new AudioData(idcnt++,name, artist, releaseyear, style, type, borrowable, borrower));
		 fireTableRowsInserted(0, audios.size()-1);
    }

    /**
     * A lista paraméterben megadott helyén levő elem törlése
     *
     * @param idx a törlendő AudioData helye
     */
    public void removeAudio(int idx){
        audios.remove(idx);
        fireTableRowsDeleted(0, audios.isEmpty() ? 0 :audios.size()-1);
    }

    /**
     * A lista paraméterben megadott helyén levő elem módosítása
     *
     * @param idx         a listában megadott helye az anyagnak
     * @param name        új anyag címe
     * @param artist      új anyag előadója
     * @param releaseyear új anyag kiadásának éve
     * @param style       új anyag stílusa
     * @param type        új anyag típusa
     * @param borrowable  új anyag kölcsönözhetősége
     */
    public void modifyAudio(int idx, String name, String artist, Integer releaseyear, String style, Enums.Audiotype type, Boolean borrowable){
        AudioData audio = audios.get(idx);
        if(!Objects.equals(audio.getName(), name))
            audio.setName(name);
        if(!Objects.equals(audio.getArtist(), artist))
            audio.setArtist(artist);
        if(!Objects.equals(audio.getReleaseYear(), releaseyear))
            audio.setReleaseYear(releaseyear);
        if(!Objects.equals(audio.getStyle(), style))
            audio.setStyle(style);
        if(!Objects.equals(audio.getType(), type))
            audio.setType(type);
        if(!Objects.equals(audio.getBorrowable(), borrowable))
            audio.setBorrowable(borrowable);
        fireTableRowsUpdated(0, audios.size() - 1);

    }

    /**
     * AudioModel Deszerializálása Google gson segítségével
     */
    public void DeSerialize(String path){
        File file = new File(path);
        if(file.exists()){
            GsonBuilder gsonBuilder = new GsonBuilder();
            //Dátum formátum deszerializálásához használt deszerializáló
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            Gson gson = gsonBuilder.setLenient().create();
            //json beolvasásához használt String
            String jsonArray;
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                jsonArray = br.readLine();
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Tömb generálása a json Stringből
            AudioData[] array = gson.fromJson(jsonArray, AudioData[].class);
            //Lista feltöltése a tömb adataival
            if (array.length != 0)
                audios.addAll(Arrays.asList(array));
        }
        //ID generálásához használt segédváltozó beállítása az eddigi maximális ID értékre+1
        if (!audios.isEmpty())
            idcnt = audios.get(audios.size() - 1).getId() + 1;
    }

    /**
     * AudioModel Szerializálása Google gson segítségével.
     */
    public void Serialize(String path){
        File file = new File(path);
        GsonBuilder gsonBuilder = new GsonBuilder();
        //Dátum formátum szerializálásához használt szerializáló
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setLenient().create();
        //Tömbbé alakítja a listát
        AudioData[] array = audios.toArray(new AudioData[0]);
        //Json Stringgé konvertálja az adatokat
        String jsonArray = gson.toJson(array);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(AudioModel other){
        for (int i = 0; i < this.getAudios().size(); i++) {
            if(!getAudioAt(i).equals(other.getAudioAt(i)))
                return false;
        }
        return true;
    }
}
