package entity;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.exception.BookEntityException;

public class BookEntityTest {
    
    public BookEntityTest() {
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
    public void test1_NewBook() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Iron Valley From the Witch", "ISBN-13: 978-1-4028-9462-6", "1990");
        assertNotNull(bookEntity);
    }
    
    @Test(expected = BookEntityException.class)
    public void test2_NewBook_Title_LengthLargerThan255_ExceptionThrown() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper The Blood and the New Bookkeeper", "ISBN 1-56389-668-0", "1990");
    }
    
    @Test(expected = BookEntityException.class)
    public void test3_NewBook_ISBN_SpecialCharacters_ExceptionThrown() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Iron Valley From the Witch", "ISBN 1+56389(668)-0", "1990");
    }
    
    @Test(expected = BookEntityException.class)
    public void test4_NewBook_ISBN_LengthLargerThan50_ExceptionThrown() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Iron Valley From the Witch", "ISBN-13: 978-1-4028-9462-61243564765897654312535657", "1990");
    }
    
    @Test(expected = BookEntityException.class)
    public void test5_NewBook_Year_NotDigits_ExceptionThrown() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Iron Valley From the Witch", "ISBN-13: 978-1-4028-9462-61", "A124");
    }
    
    @Test(expected = BookEntityException.class)
    public void test5_NewBook_Year_LengthLargerThan4_ExceptionThrown() throws BookEntityException {
        BookEntity bookEntity = new BookEntity("The Iron Valley From the Witch", "ISBN-13: 978-1-4028-9462-61", "12345");
    }
}
