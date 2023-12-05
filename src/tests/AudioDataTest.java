package tests;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AudioDataTest {

    private AudioData testData = new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Enums.Audiotype.CD, false, null);

    @Test
    void getId() {
        assertEquals(testData.getId(), 0);
    }

    @Test
    void getName() {
        assertEquals(testData.getName(), "PROG3 NHF");
    }

    @Test
    void getArtist() {
        assertEquals(testData.getArtist(), "Kovács Béla");
    }

    @Test
    void getReleaseYear() {
        assertEquals(testData.getReleaseYear(), 2023);
    }

    @Test
    void getStyle() {
        assertEquals(testData.getStyle(), "Izé");
    }

    @Test
    void getType() {
        assertEquals(testData.getType(), Enums.Audiotype.CD);
    }

    @Test
    void getBorrowable() {
        assertFalse(testData.getBorrowable());
    }

    @Test
    void getBorrower() {
        assertNull(testData.getBorrower());
    }

    @Test
    void setName() {
        testData.setName("NHF?");
        assertEquals(testData.getName(), "NHF?");
    }

    @Test
    void setArtist() {
        testData.setArtist("Béla Kovács?");
        assertEquals(testData.getArtist(), "Béla Kovács?");
    }

    @Test
    void setReleaseYear() {
        testData.setReleaseYear(1);
        assertEquals(testData.getReleaseYear(), 1);
    }

    @Test
    void setStyle() {
        testData.setStyle("???");
        assertEquals(testData.getStyle(), "???");
    }

    @Test
    void setType() {
        testData.setType(Enums.Audiotype.Kazetta);
        assertEquals(testData.getType(), Enums.Audiotype.Kazetta);
    }

    @Test
    void setBorrowable() {
        testData.setBorrowable(true);
        assertTrue(testData.getBorrowable());
    }

    @Test
    void setBorrower() {
        MemberData testBorrower = new MemberData("Cset Elek", LocalDate.of(2023, 12, 5), "123456789");
        testData.setBorrower(testBorrower);
        assertEquals(testData.getBorrower(), testBorrower);
    }
}