package entity;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.exception.MemberEntityException;

public class MemberEntityTest {
    
    public MemberEntityTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test1_NewMember() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "Male", "44", "+(123) - 456-78-90", "3702 W Sample St # 106A, South Bend, IN 46619, USA");
        assertNotNull(memberEntity);
    }
    
    @Test(expected = MemberEntityException.class)
    public void test2_NewMember_IdentityNumber_LengthLargerThan50_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027AS7483027AS7483027AS7483027AS7483027AS7483027A", "987654", "Tony", "Teo", "Male", "44", "87297373", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test3_NewMember_SecurityCode_LengthLargerThan50_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456789012345678901234567890123456789012345678901", "Tony", "Teo", "Male", "44", "87297373", "11 Tampines Ave 3");
    }
    
    @Test
    public void test4_NewMember_Gender_Valid() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "m", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "M", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "male", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "Male", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Tony", "Teo", "MALE", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Janice", "Teo", "f", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Janice", "Teo", "F", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Janice", "Teo", "female", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Janice", "Teo", "Female", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
        memberEntity = new MemberEntity("S7483027A", "987654", "Janice", "Teo", "FEMALE", "44", "87297373", "11 Tampines Ave 3");
        assertNotNull(memberEntity);
    }
    
    @Test(expected = MemberEntityException.class)
    public void test5_NewMember_Gender_InvalidOption_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Unknown", "44", "87297373", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test6_NewMember_Gender_InvalidCharacters_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male44", "44", "87297373", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test7_NewMember_Age_SpecialCharacters_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male", "age100!", "87297373", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test8_NewMember_Age_MoreThan3Digits_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male", "9999", "87297373", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test9_NewMember_Phone_InalidCharacters_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male", "44", "+(123) - 456-78-90 phone", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test10_NewMember_Phone_LengthLargerThan50_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male", "44", "+(123) - 456-78-90 - 456-78-90 - 456-78-90 - 456-78-90", "11 Tampines Ave 3");
    }
    
    @Test(expected = MemberEntityException.class)
    public void test11_NewMember_Address_LengthLargerThan255_ExceptionThrown() throws MemberEntityException {
        MemberEntity memberEntity = new MemberEntity("S7483027A", "123456", "Tony", "Teo", "Male", "44", "+(123)-456-78-90", "3702 W Sample St # 106A, South Bend, IN 46619, USA 3702 W Sample St # 106A, South Bend, IN 46619, USA 3702 W Sample Street # 106A, South Bend, IN 46619, USA 3702 W Sample St # 106A, South Bend, IN 46619, USA 3702 W Sample St # 106A, South Bend, IN 46619, USA");
    }
}
