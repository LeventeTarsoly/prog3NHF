//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tests;

import classes.MemberData;
import models.MemberModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

class MemberModelTest {
    private static MemberModel testMemberModel;
    static MemberData testData1;
    static MemberData testData2;

    MemberModelTest() {
    }

    @BeforeAll
    static void init() {
        ArrayList<MemberData> data = new ArrayList();
        testData1 = new MemberData("Kovács Béla", LocalDate.of(2023, 12, 5), "123456789");
        testData2 = new MemberData("testname", LocalDate.of(1, 1, 1), "987654321");
        data.add(testData1);
        data.add(testData2);
        testMemberModel = new MemberModel(data);
    }

    @Test
    void getMemberAt() {
        Assertions.assertEquals(testMemberModel.getMemberAt(0), testData1);
        Assertions.assertEquals(testMemberModel.getMemberAt(1), testData2);
    }

    @Test
    void addMember() {
        MemberData testData3 = new MemberData("testname", LocalDate.of(1, 1, 1), "?");
        testMemberModel.addMember("testname", LocalDate.of(1, 1, 1), "?");
        Assertions.assertTrue(testMemberModel.getMemberAt(2).equals(testData3));
    }

    @Test
    void modifyMember() {
        MemberData testData3 = new MemberData("Testname", LocalDate.of(2, 2, 2), "!");
        testMemberModel.modifyMember(0, "Testname", LocalDate.of(2, 2, 2), "!");
        Assertions.assertTrue(testMemberModel.getMemberAt(0).equals(testData3));
    }

    @Test
    void removeBorrow() {
    }

    @Test
    void Serialize() {
        String path = "src/tests/MemberModelTest.java";
        testMemberModel.Serialize(path);
        ArrayList<MemberData> data = new ArrayList();
        MemberModel testTemp = new MemberModel(data);
        testTemp.DeSerialize(path);
        File file = new File(path);
        file.delete();
        Assertions.assertTrue(testMemberModel.equals(testTemp));
    }
}