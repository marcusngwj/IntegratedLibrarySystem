package libraryadminterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import entity.BookEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.BookEntityException;
import util.exception.BookNotFoundException;

public class BookManagementModule {
    private static final int ADD_BOOK = 1;
    private static final int VIEW_BOOK_DETAILS = 2;
    private static final int UPDATE_BOOK = 3;
    private static final int DELETE_BOOK = 4;
    private static final int VIEW_ALL_BOOKS = 5;
    private static final int BACK = 6;
    
    private BookEntityControllerRemote bookEntityControllerRemote;

    public BookManagementModule() {}
    
    public BookManagementModule(BookEntityControllerRemote bookEntityControllerRemote) {
        this.bookEntityControllerRemote = bookEntityControllerRemote;
    }
    
    public void enterBookManagementMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=ADD_BOOK && response!=VIEW_BOOK_DETAILS && response!=UPDATE_BOOK 
                    && response!=DELETE_BOOK && response!=VIEW_ALL_BOOKS && response!=BACK) {
                response = getUserResponse();
            }
            
            try {
                if (response == ADD_BOOK) {
                    addBook();
                }
                else if (response == VIEW_BOOK_DETAILS) {
                    viewBookDetails();
                }
                else if (response == UPDATE_BOOK) {
                    updateBook();
                }
                else if (response == DELETE_BOOK) {
                    deleteBook();
                }
                else if (response == VIEW_ALL_BOOKS) {
                    viewAllBooks();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (BookNotFoundException | BookEntityException ex) {
                displayMessage(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                displayMessage("You have not entered a valid numerical value.\n");
            }
            finally {
                System.out.println();
            }
        }
    }
    
    private void addBook() throws BookEntityException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Book Management :: Add Book ***\n");
        System.out.print("Enter title> ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter ISBN> ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter year> ");
        String year = scanner.nextLine().trim();
        
        BookEntity newBook = new BookEntity(title, isbn, year);
        newBook = bookEntityControllerRemote.persistNewBookEntity(newBook);
        displayMessage("Book has been added successfully!");
    }
    
    private void viewBookDetails() throws BookNotFoundException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Book Management :: View Book Details ***\n");
        System.out.print("Enter title> ");
        String title = scanner.nextLine().trim();
        
        BookEntity book = bookEntityControllerRemote.retrieveBookByTitle(title);
        displayMessage(formatBookDetail(book));
    }
    
    private void updateBook() throws BookNotFoundException, BookEntityException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Book Management :: Update Book ***\n");
        System.out.print("Enter book id> ");
        long bookId = Long.valueOf(scanner.nextLine().trim());
        BookEntity book = bookEntityControllerRemote.retrieveBookById(bookId);
        
        System.out.print("Enter Title (blank if no change)> ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter ISBN (blank if no change)> ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Year (blank if no change)> ");
        String year = scanner.nextLine().trim();
        
        book.updateStaff(title, isbn, year);
        bookEntityControllerRemote.updateBook(book);
        displayMessage("Book updated successfully!\n");
    }
    
    
    private void deleteBook() throws BookNotFoundException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Book Management :: Delete Book ***\n");
        System.out.print("Enter book id> ");
        long bookId = Long.valueOf(scanner.nextLine().trim());
        
        BookEntity bookToRemove = bookEntityControllerRemote.retrieveBookById(bookId);
        
        System.out.printf("Confirm Delete Book %s (Book ID: %d) (Enter 'Y' to Delete)> ", bookToRemove.getTitle(), bookToRemove.getBookId());
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            displayMessage("Deleting...");
            bookEntityControllerRemote.deleteBook(bookToRemove.getBookId());
            displayMessage("Book deleted successfully!\n");
        }
        else {
            displayMessage("Book NOT deleted!\n");
        }
    }
    
    private void viewAllBooks() {
        List<BookEntity> bookList = bookEntityControllerRemote.retrieveAllBooks();
        String table = formatTableRow("Id", "Title", "ISBN", "Year");
        for (BookEntity book : bookList) {
            table += "\n" + formatTableRow(book.getBookId().toString(), book.getTitle(), book.getIsbn(), book.getYear().toString());
        }
        displayMessage(table);
    }
    
    private String formatBookDetail(BookEntity book) {
        String header = formatTableRow("Id", "Title", "ISBN", "Year");
        String row = formatTableRow(book.getBookId().toString(), book.getTitle(), book.getIsbn(), book.getYear().toString());
        return header + "\n" + row;
    }
    
    private String formatTableRow(String id, String title, String isbn, String year) {
        return String.format("%-5s| %-50s| %-50s| %-4s", id, title, isbn, year);
    }
    
    private String getMainMenu() {
        return "*** ILS :: Administration Operation :: Book Management ***\n\n" +
               ADD_BOOK + ": Add Book\n" +
               VIEW_BOOK_DETAILS + ": View Book Details\n" +
               UPDATE_BOOK + ": Update Book\n" +
               DELETE_BOOK + ": Delete Book\n" +
               VIEW_ALL_BOOKS + ": View All Book\n" + 
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
    
    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
}
