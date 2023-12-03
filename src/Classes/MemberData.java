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
    ArrayList<Integer> Borroweds = new ArrayList<>();
    ArrayList<Integer> BorrowedHistory = new ArrayList<>();


    public String getName() {
        return Name;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public int getPhoneNum() {
        return PhoneNum;
    }

    public ArrayList<Integer> getBorroweds() {
        return Borroweds;
    }
    public ArrayList<Integer> getBorrowedHistory() {
        return BorrowedHistory;
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

    public void setBorroweds(ArrayList<Integer> borroweds) {
        Borroweds = borroweds;
    }
    public void removeBorrowed(int borrowed) {
        this.Borroweds.remove((Object) borrowed);
    }
    public void setBorrowedHistory(ArrayList<Integer> history) {
        BorrowedHistory = history;
    }

    public void addBorrow(int id){Borroweds.add(id); BorrowedHistory.add(id);}

    public MemberData(String  name, LocalDate dateOfBirth, Integer phoneNum){
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
