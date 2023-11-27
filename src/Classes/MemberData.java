package Classes;

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
    Date DateOfBirth;
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

    public Date getDateOfBirth() {
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

    public void setDateOfBirth(Date dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public void setPhoneNum(int phoneNum) {
        PhoneNum = phoneNum;
    }

    public void setBorrowedIds(ArrayList<Integer> borrowedIds) {
        BorrowedIds = borrowedIds;
    }
}
