package libraryadminterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.BookEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;

public class LibraryOperationModule {
    private static final int LOAN_BOOK = 1;
    private static final int VIEW_LOANED_BOOKS = 2;
    private static final int RETURN_BOOK = 3;
    private static final int EXTEND_BOOK = 4;
    private static final int PAY_FINES = 5;
    private static final int MANAGE_RESERVATION = 6;
    private static final int BACK = 7;
    
    private static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    
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
            catch (MemberNotFoundException | BookNotFoundException | LoanNotFoundException | LoanException ex) {
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
    
    private void loanBook() throws MemberNotFoundException, BookNotFoundException, LoanException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Lend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Book ID> ");
        Long bookId = Long.valueOf(scanner.nextLine().trim());
        
        MemberEntity member = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        BookEntity book = bookEntityControllerRemote.retrieveBookById(bookId);
        
        LoanEntity newLoan = new LoanEntity(book, member);
        newLoan = loanEntityControllerRemote.persistNewLoanEntity(newLoan);
        displayMessage("Successfully lent book to member. Due Date: " + FORMATTER.format(newLoan.getEndDate()));
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
    
    private void returnBook() throws MemberNotFoundException, LoanNotFoundException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Return Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        
        MemberEntity member = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());
        
        displayLoanTable(loanList);
        System.out.println();
        
        System.out.print("Enter Book ID to Return> ");
        Long bookId = Long.valueOf(scanner.nextLine().trim());
        
        loanEntityControllerRemote.deleteLoan(bookId);
        displayMessage("Book successfully returned.");
    }
    
    private void extendBook() throws MemberNotFoundException, LoanNotFoundException, LoanException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Extend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        
        MemberEntity member = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());
        
        displayLoanTable(loanList);
        System.out.println();
        
        System.out.print("Enter Book ID to Extend> ");
        Long bookId = Long.valueOf(scanner.nextLine().trim());
        
        LoanEntity loan = loanEntityControllerRemote.retrieveLoanByBookId(bookId);
        loan = loanEntityControllerRemote.updateLoan(loan);
        displayMessage("Book successfully extended. New due date: " + FORMATTER.format(loan.getEndDate()));
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
            table += "\n" + String.format("%-5s| %-50s| %-11s", loan.getBook().getBookId(), loan.getBook().getTitle(), FORMATTER.format(loan.getEndDate()));
        }
        
        System.out.print(header);
        System.out.println(table);
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
