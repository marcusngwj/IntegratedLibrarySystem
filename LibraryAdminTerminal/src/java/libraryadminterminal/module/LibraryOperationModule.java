package libraryadminterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.BookEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.UnsuccessfulLoanException;

public class LibraryOperationModule {
    private static final int LOAN_BOOK = 1;
    private static final int VIEW_LOANED_BOOKS = 2;
    private static final int RETURN_BOOK = 3;
    private static final int EXTEND_BOOK = 4;
    private static final int PAY_FINES = 5;
    private static final int MANAGE_RESERVATION = 6;
    private static final int BACK = 7;
    
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private LoanEntityControllerRemote loanEntityControllerRemote;
            
    public LibraryOperationModule() {}

    public LibraryOperationModule(MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, LoanEntityControllerRemote loanEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.loanEntityControllerRemote = loanEntityControllerRemote;
    }

    public void enterLibraryOperationMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=LOAN_BOOK && response!=VIEW_LOANED_BOOKS && response!=RETURN_BOOK
                    && response!=EXTEND_BOOK && response!=PAY_FINES && response!=MANAGE_RESERVATION && response!=BACK) {
                response = getUserResponse();
            }
            try {
                if (response == LOAN_BOOK) {
                    loanBook();
                }
                else if (response == VIEW_LOANED_BOOKS) {
                    viewLoanedBooks();
                }
                else if (response == RETURN_BOOK) {
                    returnBook();
                }
                else if (response == EXTEND_BOOK) {
                    extendBook();
                }
                else if (response == PAY_FINES) {
                    payFines();
                }
                else if (response == MANAGE_RESERVATION) {
                    manageReservation();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (MemberNotFoundException | BookNotFoundException | UnsuccessfulLoanException ex) {
                displayMessage(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                displayMessage("Please enter a valid numeric input.");
            }
            finally {
                System.out.println();
            }
            
            
        }
    }
    
    private void loanBook() throws NumberFormatException, MemberNotFoundException, BookNotFoundException, UnsuccessfulLoanException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Lend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Book ID> ");
        Long bookID = Long.valueOf(scanner.nextLine().trim());
        
        MemberEntity member = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        BookEntity book = bookEntityControllerRemote.retrieveBookById(bookID);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 2);

        LoanEntity newLoan = new LoanEntity(book, cal.getTime(), member);
        newLoan = loanEntityControllerRemote.persistNewLoanEntity(newLoan);
        displayMessage("Successfully lent book to member. Due Date: " + dateFormat.format(cal.getTime()));
    }
    
    private void viewLoanedBooks() throws MemberNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: View Lent Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        
        MemberEntity member = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());
        
        displayLoanTable(loanList);
    }
    
    private void returnBook() {
        
    }
    
    private void extendBook() {
        
    }
    
    private void payFines() {
        
    }
    
    private void manageReservation() {
        
    }
    
    private void displayLoanTable(List<LoanEntity> loanList) {
        System.out.println("Currently Lent Books:\n");
        
        String header = String.format("%-5s| %-50s| %-11s", "Id", "Title", "Due Date");
        
        String table = "";
        for (LoanEntity loan : loanList) {
            table += String.format("%-5s| %-50s| %-11s", loan.getLoanId(), loan.getBook().getTitle(), loan.getEndDate());
        }
        
        displayMessage(header);
        displayMessage(table);
    }
    
    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
    
    private String getMainMenu() {
        return "*** ILS :: Library Operation ***\n\n" +
               LOAN_BOOK + ": Lend Book\n" +
               VIEW_LOANED_BOOKS + ": View Lent Books\n" +
               RETURN_BOOK + ": Return Book\n" +
               EXTEND_BOOK + ": Extend Book\n" +
               PAY_FINES + ": Pay Fines\n" +
               MANAGE_RESERVATION + ": Manage Reservations\n" +
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
    
}
