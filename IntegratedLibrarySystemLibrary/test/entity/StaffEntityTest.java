package entity;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.exception.StaffEntityException;
import util.exception.StaffExistsException;

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
    
    @Test(expected = StaffEntityException.class)
    public void test2_NewStaff_InvalidFirstName_Digits_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("123", "Chua", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test3_NewStaff_InvalidFirstName_SpecialCharacters_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("!@#$@#", "Chua", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test4_NewStaff_InvalidFirstName_Digits_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "123", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test5_NewStaff_InvalidLastName_SpecialCharacters_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "^%T#$%", "manager", "password");
    }
    
    @Test(expected = StaffEntityException.class)
    public void test6_NewStaff_InvalidUsername_SpecialCharacters_ExceptionThrown() throws StaffEntityException {
        StaffEntity staffEntity = new StaffEntity("Linda", "Chua", "manager123@#$", "password");
    }
}
