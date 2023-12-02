package Models;

import Classes.AudioData;
import Classes.MemberData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MemberModel extends AbstractTableModel {
    public static List<MemberData> members = new ArrayList<MemberData>();

    public static List<MemberData> getMembers(){return members;}
    @Override
    public int getRowCount() {
        return members.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    public MemberData getMemberAt(int rowidx){
        return members.get(rowidx);
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MemberData member = members.get(rowIndex);
        switch(columnIndex) {
            case 0: return member.getName();
            case 1: return member.getDateOfBirth();
            case 2: return member.getPhoneNum();
            default: return null;
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
        fireTableDataChanged();
    }

    public void addMember(String name, LocalDate dateOfBirth, Integer phoneNum) {
		 members.add(new MemberData(members.size(), name, dateOfBirth, phoneNum));
		 fireTableRowsInserted(0, members.size()-1);
    }

    public void removeMember(int idx) {
        for (AudioData audio:AudioModel.audios) {
            if(members.get(idx).equals(audio.getBorrower()))
                audio.setBorrower(null);
        }
        members.remove(idx);
        fireTableRowsInserted(0, members.size()-1);
    }

    public void modifyMember(int idx, String name, LocalDate dateOfBirth, Integer phoneNum){
        MemberData member = members.get(idx);
        if(!Objects.equals(member.getName(), name))
            member.setName(name);
        if(!Objects.equals(member.getDateOfBirth(), dateOfBirth))
            member.setDateOfBirth(dateOfBirth);
        if(!Objects.equals(member.getPhoneNum(), phoneNum))
            member.setPhoneNum(phoneNum);
    }

    public void removeBorrow(int borrow){
        for (MemberData member:members){
            if(member.getBorroweds().contains(borrow))
                member.removeBorrowed(borrow);
        }
    }

    public void DeSerialize(){
        File file = new File("src/Data/Members.json");
        if(file.exists()){
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
            MemberData[] array = gson.fromJson(jsonArray, MemberData[].class);
            if(array.length!=0)
                members.addAll(Arrays.asList(array));
        }
    }
    public void Serialize(){
        File file = new File("src/Data/Members.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setLenient().create();
        MemberData[] array = members.toArray(new MemberData[0]);
        String jsonArray = gson.toJson(array);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void borrow(MemberData member, AudioData audio){

    }
}
