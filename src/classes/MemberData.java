package classes;

import java.time.LocalDate;
import java.util.*;

/**
 * Hangkölcsönző tag osztálya
 */
public class MemberData {
    /**
     * Tag neve
     */
    String Name;
    /**
     * Tag születési dátuma
     */
    LocalDate DateOfBirth;
    /**
     * Tag telefonszáma
     */
    String PhoneNum;
    /**
     * Tag kölcsönzött könyveinek listája
     */
    final ArrayList<Integer> Borroweds = new ArrayList<>();
    /**
     * Kölcsönzött anyagok története
     */
    ArrayList<Integer> BorrowedHistory = new ArrayList<>();


    /**
     * Tag nevének gettere
     *
     * @return név
     */
    public String getName() {
        return Name;
    }

    /**
     * Születési dátum gettere
     *
     * @return születési dátum
     */
    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    /**
     * Telefonszám gettere
     *
     * @return telefonszám
     */
    public String getPhoneNum() {
        return PhoneNum;
    }

    /**
     * Jelenleg kikölcsönzött anyagok gettere
     *
     * @return jelenleg kikölcsönzött anyagok
     */
    public ArrayList<Integer> getBorroweds() {
        return Borroweds;
    }

    /**
     * Tag nevének settere
     *
     * @param name az új név
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Születési dátum settere
     *
     * @param dateOfBirth az új születési dátum
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    /**
     * Telefonszám gettere
     *
     * @param phoneNum az új telefonszám
     */
    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    /**
     * Egy új kölcsönzés hozzáadása a kölcsönzések listákhoz
     *
     * @param id a kikölcsönzött anyag IDja
     */
    public void addBorrow(int id) {
        Borroweds.add(id);
        BorrowedHistory.add(id);
    }

    /**
     * Egy kölcsönzött anyaig törlése a jelenlegi kölcsönzések közül
     *
     * @param id a kölcsönzött anyag IDja
     */
    public void removeBorrowed(int id) {
        this.Borroweds.remove((Object) id);
    }

    /**
     * MemberData konstruktora
     *
     * @param name        a név
     * @param dateOfBirth a születési dátum
     * @param phoneNum    a telefonszám
     */
    public MemberData(String name, LocalDate dateOfBirth, String phoneNum) {
        Name=name;
        DateOfBirth=dateOfBirth;
        PhoneNum=phoneNum;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Két MemberData összehasonlítására alkalmas equals() függvény
     *
     * @param other az egyik összehasonlítandó tag
     * @return true, ha megegyezik, hamis ha nem
     */
    public Boolean equals(MemberData other) {
        if(other != null){
            return Objects.equals(other.getName(), getName()) && Objects.equals(other.getPhoneNum(), getPhoneNum()) && getDateOfBirth().equals(other.getDateOfBirth());
        }
        return false;
    }
}
