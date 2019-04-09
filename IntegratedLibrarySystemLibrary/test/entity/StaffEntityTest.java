package entity;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.exception.StaffEntityException;

public class StaffEntityTest {
    
    public StaffEntityTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void test1_NewStaff() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager", "password");
        assertNotNull(staffEntity);
    }
    
    @Test
    public void test2_NewStaff_FirstName_HasWhitespace_Valid() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda Jeremial", "Chua", "manager", "password");
        assertNotNull(staffEntity);
    }
    
    @Test(expected = StaffEntityException.class)
    public void test3_NewStaff_FirstName_Digits_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("123", "Chua", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test4_NewStaff_FirstName_SpecialCharacters_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("!@#$@#", "Chua", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test5_NewStaff_FirstName_LengthLargerThan50_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Aloysius Brandon Charlie Dephanie Elizabeth Felicia", "Chua", "manager", "password");
    }
    
    @Test
    public void test6_NewStaff_Username_SpecialCharacters_Valid() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager123", "password");
        assertNotNull(staffEntity);
    }
    
    @Test(expected = StaffEntityException.class)
    public void test7_NewStaff_Username_HasWhitespace_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager 123", "password");
        assertNotNull(staffEntity);
    }
    
    @Test(expected = StaffEntityException.class)
    public void test8_NewStaff_Username_LengthLargerThan50_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "supercalifragilisticexpialidocioussupercalifragilisticexpialidocious", "password");
        assertNotNull(staffEntity);
    }
    
    @Test(expected = StaffEntityException.class)
    public void test9_NewStaff_Password_LengthLargerThan50_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager", "supercalifragilisticexpialidocioussupercalifragilisticexpialidocious");
        assertNotNull(staffEntity);
    }
}
