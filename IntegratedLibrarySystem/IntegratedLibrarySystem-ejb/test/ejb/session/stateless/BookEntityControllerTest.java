/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.exception.BookEntityException;

/**
 *
 * @author Marcus
 */
public class BookEntityControllerTest {

    BookEntityControllerRemote bookEntityControllerRemote = lookupBookEntityControllerRemote();
    
    public BookEntityControllerTest() {
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

//    @Test
//    public void test1CreateNewBook() throws BookEntityException {
//        BookEntity newBookEntity = new BookEntity("First Book", "S123456", "1990");
//        newBookEntity = bookEntityControllerRemote.persistNewBookEntity(newBookEntity);
//        
//        assertNotNull(newBookEntity.getBookId());
//        assertEquals(12l, newBookEntity.getBookId().longValue());
//    }
    
    @Test(expected = BookEntityException.class)
    public void test2_CreateNewBook_InvalidYear_Alphabets_ExceptionThrown() throws BookEntityException {
        BookEntity newBookEntity = new BookEntity("Book", "S123456", "ooo");
        newBookEntity = bookEntityControllerRemote.persistNewBookEntity(newBookEntity);   
    }
    
    @Test(expected = BookEntityException.class)
    public void test3_CreateNewBook_InvalidYear_SepcialCharacters_ExceptionThrown() throws BookEntityException {
        BookEntity newBookEntity = new BookEntity("Book", "S123456", "!@#$");
        newBookEntity = bookEntityControllerRemote.persistNewBookEntity(newBookEntity);   
    }
    
    @Test(expected = BookEntityException.class)
    public void test4_CreateNewBook_InvalidYear_MoreThan4Digits_ExceptionThrown() throws BookEntityException {
        BookEntity newBookEntity = new BookEntity("Book", "S123456", "12345");
        newBookEntity = bookEntityControllerRemote.persistNewBookEntity(newBookEntity);   
    }
    
    @Test(expected = BookEntityException.class)
    public void test5_CreateNewBook_InvalidYear_LessThan4Digits_ExceptionThrown() throws BookEntityException {
        BookEntity newBookEntity = new BookEntity("Book", "S123456", "1");
        newBookEntity = bookEntityControllerRemote.persistNewBookEntity(newBookEntity);   
    }

    private BookEntityControllerRemote lookupBookEntityControllerRemote() {
        try {
            Context c = new InitialContext();
            return (BookEntityControllerRemote) c.lookup("java:global/IntegratedLibrarySystem/IntegratedLibrarySystem-ejb/BookEntityController!ejb.session.stateless.BookEntityControllerRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
}
