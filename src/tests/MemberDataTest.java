package tests;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class MemberDataTest {
    private MemberData testData = new MemberData("Kovács Béla", LocalDate.of(2023,12,5), "123456789");

    @Test
    void getName() {
        assertEquals(testData.getName(), "Kovács Béla");
    }
    @Test
    void getDateOfBirth() {
        assertEquals(testData.getDateOfBirth(), LocalDate.of(2023,12,5));
    }

    @Test
    void getPhoneNum() {
        assertEquals(testData.getPhoneNum(), "123456789");
    }

    @Test
    void getBorroweds() {
        assertEquals(testData.getBorroweds(), new ArrayList<>());
    }

    @Test
    void setName() {
        testData.setName("Béla Kovács?");
        assertEquals(testData.getName(), "Béla Kovács?");
    }

    @Test
    void setDateOfBirth() {
        testData.setDateOfBirth(LocalDate.of(1111,11,1));
        assertEquals(testData.getDateOfBirth(), LocalDate.of(1111,11,1));
    }

    @Test
    void setPhoneNum() {
        testData.setPhoneNum("987654321");
        assertEquals(testData.getPhoneNum(), "987654321");
    }

    @Test
    void addBorrow() {
        AudioData testAudioData = new AudioData(126, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Enums.Audiotype.CD, false, null);
        testData.addBorrow(testAudioData);
        assertEquals(testData.getBorroweds().get(0), 126);
        assertNotEquals(testData.getBorrowedHistory().get(0),testAudioData);
        //az idnak 0nak kell lennie
        AudioData testAudioData2 = new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Enums.Audiotype.CD, false, null);
        assertTrue(testData.getBorrowedHistory().get(0).equals(testAudioData2));
    }

    @Test
    void removeBorrowed() {
        testData.addBorrow(new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Enums.Audiotype.CD, false, null));
        testData.removeBorrowed(0);
        assertEquals(testData.getBorroweds(), new ArrayList<>());
    }

    @Test
    void testToString() {
        assertEquals(testData.toString(), "Kovács Béla");
    }

    @Test
    void testEquals() {
        MemberData testTemp = new MemberData("asd", LocalDate.of(1,1,1), "321");
        assertNotEquals(testData, testTemp);
    }
}