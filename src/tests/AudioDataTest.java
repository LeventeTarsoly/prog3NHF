package tests;

import classes.AudioData;
import classes.Enums;
import classes.MemberData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

//todo commments
class AudioDataTest {

    private AudioData testData = new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Enums.Audiotype.CD, false, null);

    @Test
    void getId() {
        Assertions.assertEquals(testData.getId(), 0);
    }

    @Test
    void getName() {
        Assertions.assertEquals(testData.getName(), "PROG3 NHF");
    }

    @Test
    void getArtist() {
        Assertions.assertEquals(testData.getArtist(), "Kovács Béla");
    }

    @Test
    void getReleaseYear() {
        Assertions.assertEquals(testData.getReleaseYear(), 2023);
    }

    @Test
    void getStyle() {
        Assertions.assertEquals(testData.getStyle(), "Izé");
    }

    @Test
    void getType() {
        Assertions.assertEquals(testData.getType(), Enums.Audiotype.CD);
    }

    @Test
    void getBorrowable() {
        Assertions.assertFalse(testData.getBorrowable());
    }

    @Test
    void getBorrower() {
        Assertions.assertNull(testData.getBorrower());
    }

    @Test
    void setName() {
        testData.setName("NHF?");
        Assertions.assertEquals(testData.getName(), "NHF?");
    }

    @Test
    void setArtist() {
        testData.setArtist("Béla Kovács?");
        Assertions.assertEquals(testData.getArtist(), "Béla Kovács?");
    }

    @Test
    void setReleaseYear() {
        testData.setReleaseYear(1);
        Assertions.assertEquals(testData.getReleaseYear(), 1);
    }

    @Test
    void setStyle() {
        testData.setStyle("???");
        Assertions.assertEquals(testData.getStyle(), "???");
    }

    @Test
    void setType() {
        testData.setType(Enums.Audiotype.Kazetta);
        Assertions.assertEquals(testData.getType(), Enums.Audiotype.Kazetta);
    }

    @Test
    void setBorrowable() {
        testData.setBorrowable(true);
        Assertions.assertTrue(testData.getBorrowable());
    }

    @Test
    void setBorrower() {
        MemberData testBorrower = new MemberData("Cset Elek", LocalDate.of(2023, 12, 5), "123456789");
        testData.setBorrower(testBorrower);
        Assertions.assertEquals(testData.getBorrower(), testBorrower);
    }
}