package ejb.session.stateless;

import entity.BookEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BookNotFoundException;

@Stateless
@Local(BookEntityControllerLocal.class)
@Remote(BookEntityControllerRemote.class)
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    public BookEntityController() {}
    
    @Override
    public BookEntity persistNewBookEntity(BookEntity bookEntity) {
        em.persist(bookEntity);
        em.flush();
        em.refresh(bookEntity);
        return bookEntity;
    }
    
    @Override
    public List<BookEntity> retrieveAllBooks() {
        Query query = em.createQuery("SELECT b FROM BookEntity b");
        return query.getResultList();
    } 
    
    @Override
    public BookEntity retrieveBookById(Long bookId) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM BookEntity b WHERE b.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        
        try {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new BookNotFoundException("Book Id " + bookId + " does not exist!");
        }
    }
    
    @Override
    public BookEntity retrieveBookByTitle(String title) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM BookEntity b WHERE b.title = :inTitle");
        query.setParameter("inTitle", title);
        
        try {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new BookNotFoundException("Book Title" + title + " does not exist!");
        }
    }
    
    @Override
    public List<BookEntity> searchBookByTitle(String title) throws BookNotFoundException {
        Query query = em.createQuery("SELECT b FROM BookEntity b WHERE b.title LIKE '%" + title + "%'");
       
      
        
        try {
            return (List<BookEntity>) query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            System.out.println("Entered Exception here");
            throw new BookNotFoundException("Book Title" + title + " does not exist!");
        }
    }
    
    @Override
    public void updateBook(BookEntity bookEntity) {
        em.merge(bookEntity);
    }
    
    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException {
        BookEntity bookToRemove = retrieveBookById(bookId);
        em.remove(bookToRemove);
    }
}
