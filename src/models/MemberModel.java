package models;

import classes.AudioData;
import classes.MemberData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Az tagok táblájához tartozó Model
 */
public class MemberModel extends AbstractTableModel {
    /**
     * A tagok adatainak listája
     */
    private static final List<MemberData> members = new ArrayList<>();

    /**
     * A tagok listájának gettere
     *
     * @return a tagok listája
     */
    public static List<MemberData> getMembers(){return members;}
    @Override
    public int getRowCount() {
        return members.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Visszaadja a paraméterben megadott helyen levő MemberDatát a listából
     *
     * @param idx a kiválasztandó tag indexe
     * @return A listából idxedik helyen kiszedett MemberData
     */
    public MemberData getMemberAt(int idx) {
        return members.get(idx);
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MemberData member = members.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> member.getName();
            case 1 -> member.getDateOfBirth();
            case 2 -> member.getPhoneNum();
            default -> null;
        };
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
        return switch (column) {
            case 1 -> LocalDate.class;
            default -> String.class;
        };
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0 -> members.get(rowIndex).setName((String) aValue);
            case 1 -> members.get(rowIndex).setDateOfBirth((LocalDate) aValue);
            case 2 -> members.get(rowIndex).setPhoneNum((String) aValue);
        }
        fireTableDataChanged();
    }

    /**
     * Új MemberData felvétele a listához
     * Ehhez létrehoz egy új MemberDatát, és ezt teszi bele a listába
     *
     * @param name        új tag neve
     * @param dateOfBirth új tag születési dátuma
     * @param phoneNum    új telefonszáma
     */
    public void addMember(String name, LocalDate dateOfBirth, String phoneNum) {
        members.add(new MemberData(name, dateOfBirth, phoneNum));
		 fireTableRowsInserted(0, members.size()-1);
    }

    /**
     * A lista paraméterben megadott helyén levő elem törlése
     *
     * @param idx a törlendő MemberData helye
     */
    public void removeMember(AudioModel audioModel, int idx) {
        //ha kitörlök egy elemet a tagok közül, a kölcsönzések közül is törölni kell
        for (AudioData audio : audioModel.getAudios()) {
            if(members.get(idx).equals(audio.getBorrower()))
                audio.setBorrower(null);
        }

        members.remove(idx);
        fireTableRowsInserted(0, members.size()-1);
    }

    /**
     * A lista paraméterben megadott helyén levő elem módosítása
     *
     * @param idx         a listában megadott helye az tagnak
     * @param name        új tag neve
     * @param dateOfBirth új tag születési dátuma
     * @param phoneNum    új telefonszáma
     */
    public void modifyMember(int idx, String name, LocalDate dateOfBirth, String phoneNum) {
        MemberData member = members.get(idx);
        if(!Objects.equals(member.getName(), name))
            member.setName(name);
        if(!Objects.equals(member.getDateOfBirth(), dateOfBirth))
            member.setDateOfBirth(dateOfBirth);
        if(!Objects.equals(member.getPhoneNum(), phoneNum))
            member.setPhoneNum(phoneNum);
    }

    /**
     * A paraméterben megadott IDjű anyag törlése a tagok kölcsönzései közül
     *
     * @param id a törlendő ID
     */
    public void removeBorrow(int id) {
        for (MemberData member:members){
            if (member.getBorroweds().contains(id))
                member.removeBorrowed(id);
        }
    }

    /**
     * MemberModel Deszerializálása Google gson segítségével
     */
    public void DeSerialize(){
        File file = new File("src/Data/Members.json");
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
            MemberData[] array = gson.fromJson(jsonArray, MemberData[].class);
            //Lista feltöltése a tömb adataival
            if(array.length!=0)
                members.addAll(Arrays.asList(array));
        }
    }

    /**
     * MemberModel Szerializálása Google gson segítségével.
     */
    public void Serialize(){
        File file = new File("src/Data/Members.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        //Dátum formátum szerializálásához használt szerializáló
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setLenient().create();
        //Tömbbé alakítja a listát
        MemberData[] array = members.toArray(new MemberData[0]);
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

}
