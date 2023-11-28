package Classes;

import java.time.LocalDate;
import java.util.*;

/**
 *Hangkölcsönző tag osztálya
 */
public class MemberData {
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

    @Override
    public String toString() {
        return getName();
    }

    public Boolean equals(MemberData other) {
        if(other != null){
            if(Objects.equals(other.getName(), getName()) &&other.getPhoneNum()==getPhoneNum()&&getDateOfBirth().equals(other.getDateOfBirth()))
                return true;
        }
        return false;
    }
}
