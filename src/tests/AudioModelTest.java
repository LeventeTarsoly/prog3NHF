//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tests;

import classes.AudioData;
import classes.Enums.Audiotype;
import classes.MemberData;
import models.AudioModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

class AudioModelTest {
    private static AudioModel testAudioModel;
    static AudioData testData1;
    static AudioData testData2;

    AudioModelTest() {
    }

    @BeforeAll
    static void init() {
        ArrayList<AudioData> data = new ArrayList();
        testData1 = new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Audiotype.CD, false, (MemberData)null);
        testData2 = new AudioData(1, "NHF?", "Béla Kovács?", 1, "???", Audiotype.Kazetta, true, new MemberData("Cset Elek", LocalDate.of(2023, 12, 5), "123456789"));
        data.add(testData1);
        data.add(testData2);
        testAudioModel = new AudioModel(data);
    }

    @AfterEach
    public void refresh() {
        testAudioModel.getAudios().clear();
        testData1 = new AudioData(0, "PROG3 NHF", "Kovács Béla", 2023, "Izé", Audiotype.CD, false, (MemberData)null);
        testData2 = new AudioData(1, "NHF?", "Béla Kovács?", 1, "???", Audiotype.Kazetta, true, new MemberData("Cset Elek", LocalDate.of(2023, 12, 5), "123456789"));
        testAudioModel.getAudios().add(testData2);
        testAudioModel.getAudios().add(testData1);
    }

    @Test
    void getAudioAt() {
        Assertions.assertEquals(testAudioModel.getAudioAt(0), testData2);
        Assertions.assertEquals(testAudioModel.getAudioAt(1), testData1);
    }

    @Test
    void getLastAudio() {
        Assertions.assertEquals(testAudioModel.getLastAudio(), testData1);
    }

    @Test
    void addAudio() {
        AudioData testData3 = new AudioData(0, "Testname", "Testartist", 3000, "Teststyle", Audiotype.Hanglemez, true, (MemberData)null);
        testAudioModel.addAudio("Testname", "Testartist", 3000, "Teststyle", Audiotype.Hanglemez, true, (MemberData)null);
        Assertions.assertTrue(testAudioModel.getLastAudio().equals(testData3));
    }

    @Test
    void removeAudio() {
        testAudioModel.removeAudio(0);
        Assertions.assertTrue(testAudioModel.getAudioAt(0).equals(testData1));
    }

    @Test
    void modifyAudio() {
        AudioData testData3 = new AudioData(1, "Testname", "Testartist", 3000, "Teststyle", Audiotype.Hanglemez, true, (MemberData)null);
        testAudioModel.modifyAudio(0, "Testname", "Testartist", 3000, "Teststyle", Audiotype.Hanglemez, true);
        Assertions.assertTrue(testAudioModel.getAudioAt(0).equals(testData3));
    }

    @Test
    void Serialize() {
        String path = "src/tests/AudioModelTest.java";
        testAudioModel.Serialize(path);
        ArrayList<AudioData> data = new ArrayList();
        AudioModel testTemp = new AudioModel(data);
        testTemp.DeSerialize(path);
        File file = new File(path);
        file.delete();
        Assertions.assertTrue(testAudioModel.equals(testTemp));
    }
}
