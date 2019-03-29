package ejb.session.stateless;

import entity.BookEntity;
import java.util.List;
import util.exception.BookNotFoundException;

public interface BookEntityControllerLocal {

    public BookEntity persistNewBookEntity(BookEntity bookEntity);

    public List<BookEntity> retrieveAllBooks();

    public BookEntity retrieveBookById(Long bookId) throws BookNotFoundException;

    public BookEntity retrieveBookByTitle(String title) throws BookNotFoundException;
    
    public List<BookEntity> searchBookByTitle(String title) throws BookNotFoundException;

    public void updateBook(BookEntity bookEntity);

    public void deleteBook(Long bookId) throws BookNotFoundException;
    
}
