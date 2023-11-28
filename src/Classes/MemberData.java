package Classes;

import java.time.LocalDate;
import java.util.*;

/**
 *Hangkölcsönző tag osztálya
 */
public class MemberData {
    /**
     *Mesterséges ID
     */
    int Id;
    /**
     *Tag neve
     */
    String Name;
    /**
     *Tag születési dátuma
     */
    LocalDate DateOfBirth;
    /**
     *Tag telefonszáma
     */
    int PhoneNum;
    /**
     *Tag kölcsönzött könyveinek listája
     */
    ArrayList<Integer> BorrowedIds = new ArrayList<>();

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public int getPhoneNum() {
        return PhoneNum;
    }

    public ArrayList<Integer> getBorrowedIds() {
        return BorrowedIds;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public void setPhoneNum(int phoneNum) {
        PhoneNum = phoneNum;
    }

    public void setBorrowedIds(ArrayList<Integer> borrowedIds) {
        BorrowedIds = borrowedIds;
    }
    public MemberData(Integer id,String  name, LocalDate dateOfBirth, Integer phoneNum){
        Name=name;
        DateOfBirth=dateOfBirth;
        PhoneNum=phoneNum;
    }
}
