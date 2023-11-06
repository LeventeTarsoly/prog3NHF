package Classes;

import java.util.*;

//Hangkölcsönző tag osztálya
public class MemberData {
    //Mesterséges ID
    int Id;
    //Tag neve
    String Name;
    //Tag születési dátuma
    Date DateOfBirth;
    //Tag telefonszáma
    int PhoneNum;
    //Tag kölcsönzött könyveinek listája
    ArrayList<Integer> BorrowedIds = new ArrayList<>();
}
