package bookdropmachineclient.module;

import bookdropmachineclient.WebService;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.helper.DateHelper;
import util.helper.MoneyHelper;
import ws.client.BookEntity;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineEntity;
import ws.client.FineNotFoundException_Exception;
import ws.client.LoanEntity;
import ws.client.LoanException_Exception;
import ws.client.LoanNotFoundException_Exception;
import ws.client.MemberEntity;
import ws.client.ReservationException_Exception;
import ws.client.ReservationEntity;
import ws.client.ReservationNotFoundException_Exception;

/**
 *
 * @author ang
 */
public class LibraryOperationModule {

    private static final int MAX_LOAN = 3;
    private MemberEntity member;

    public LibraryOperationModule() {

    }

    public LibraryOperationModule(MemberEntity currentMemberEntity) {
        this.member = currentMemberEntity;
    }

    public void enterViewLentBook() {
        System.out.println("*** BDM Client :: View Lent Books ***\n");
        List<LoanEntity> loanList = WebService.retrieveLoansByMemberId(member.getMemberId());
        displayLoanTable(loanList);
    }

    public void enterReturnBook() throws LoanNotFoundException_Exception, LoanException_Exception, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** BDM Client :: Return Book ***\n");

        List<LoanEntity> loanList = WebService.retrieveLoansByMemberId(member.getMemberId());

        displayLoanTable(loanList);
        System.out.println();

        System.out.print("Enter Book ID to Return> ");

        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);
        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);
            WebService.removeLoan(bookId, member.getMemberId());
            displayMessage("Book successfully returned.");
        } else {
            printInvalidBookFormat();
        }
    }

    public void enterExtendBook() throws LoanNotFoundException_Exception, LoanException_Exception, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** BDM Client :: Extend Book ***\n");

        List<LoanEntity> loanList = WebService.retrieveLoansByMemberId(member.getMemberId());

        displayLoanTable(loanList);
        System.out.println();

        System.out.print("Enter Book ID to Extend> ");
        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);
        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);
            LoanEntity loan = WebService.extendLoan(bookId, member.getMemberId());
            displayMessage("Book successfully extended. New due date: " + DateHelper.format(loan.getEndDate().toGregorianCalendar().getTime()));
        } else {
            printInvalidBookFormat();
        }
    }

    public void enterPayFine() throws FineNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** BDM Client :: Pay Fines ***\n");

        String identityNumber = member.getIdentityNumber();
        List<FineEntity> fineList = WebService.retrieveFinesByMemberIdentityNumber(identityNumber);

        displayFineTable(fineList);
        System.out.println();

        if (fineList.size() > 0) {
            System.out.print("Enter Fine to Settle> ");
            String bookIdStr = scanner.nextLine().trim();
            boolean isNumFine = checkNum(bookIdStr);
            System.out.print("Select Payment Method (1: Cash, 2: Card)> ");
            String paymentMtdStr = scanner.nextLine().trim();
            boolean isNumPaymentMtd = checkNum(paymentMtdStr);
            if (isNumFine && isNumPaymentMtd) {
                Long fineId = Long.valueOf(bookIdStr);
                int paymentMode = Integer.valueOf(paymentMtdStr);
                WebService.removeFine(fineId, member.getMemberId());
                displayMessage("Fine successfully paid.");
            } else {
                if (!isNumFine) {
                    printInvalidFineFormat();
                }
                if (!isNumPaymentMtd) {
                    printInvalidPaymentFormat();
                }
            }
        } else {
            displayMessage("There are no outstanding fine.");
        }
    }

    public void enterReserveBook() throws BookNotFoundException_Exception, ReservationNotFoundException_Exception {
        final String SUCCESS_RESERVED = "Book successfully reserved.";
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** BDM Client :: Reserve Book ***\n");

        System.out.print("Enter Title to Search> ");
        String title = scanner.nextLine().trim();

        List<BookEntity> bookEntities = WebService.searchBookByTitle(title);
        System.out.println();
        System.out.println("Search Results:");

        String header = String.format("%-5s| %-50s| %-20s", "Id", "Title", "Availability");
        String table = "";
        System.out.print(header);

        for (BookEntity currBook : bookEntities) {
            boolean onLoaned = isLoaned(currBook);
            boolean onReserved = isReserved(currBook);
             if(onLoaned) {
               try {
                    //System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getBookLoanedDate(currBook));
                     table += "\n" + String.format("%-5s| %-50s| Due on %-20s", currBook.getBookId(), currBook.getTitle(), getBookLoanedDate(currBook));
               } catch (LoanNotFoundException_Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                if(onReserved) {
                    String msg = "Available but is Reserved";
                    table += "\n" + String.format("%-5s| %-50s| %-20s", currBook.getBookId(), currBook.getTitle(), msg);
                   // System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Available but is Reserved");
                } else {
                    String msg = "Available Now";
                    table += "\n" + String.format("%-5s| %-50s| %-20s", currBook.getBookId(), currBook.getTitle(), msg);
                    //System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Available Now ");
                }
            }
//            if (onLoaned && !onReserved) {
//                try {
//                    System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getBookLoanedDate(currBook));
//                } catch (LoanNotFoundException_Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else if (onLoaned && onReserved || !onLoaned && onReserved) {
//                try {
//                    System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getAvailableReservationDate(currBook));
//                } catch (ReservationNotFoundException_Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else {
//                System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Available Now ");
//            }
        }
        System.out.println(table);
        System.out.println();
        System.out.print("Enter Book ID to Reserve: ");
        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);

        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);
            BookEntity currBook = WebService.retrieveBookById(bookId);
            ReservationEntity newReservation = new ReservationEntity();
            newReservation.setBook(currBook);
            newReservation.setMember(member);

            try {
                WebService.createNewReservationEntity(newReservation);
                //Print Reservation Success msg
                displayMessage(SUCCESS_RESERVED);
            } catch (LoanException_Exception | ReservationException_Exception | LoanNotFoundException_Exception le) {
                System.out.println(le.getMessage());
            }
        } else {
            printInvalidBookFormat();
        }
    }

    private String getBookLoanedDate(BookEntity currBook) throws LoanNotFoundException_Exception {
        try {
            LoanEntity currLoan = WebService.retrieveLoanByBookId(currBook.getBookId());
            Date bookLoanedDate = currLoan.getEndDate().toGregorianCalendar().getTime();
            return DateHelper.format(bookLoanedDate);
        } catch (LoanNotFoundException_Exception lnfe) {
            throw lnfe;
        }
    }

    private String getAvailableReservationDate(BookEntity currBook) throws ReservationNotFoundException_Exception {

        try {
            Date latestDate = WebService.retrieveLatestReservationDate(currBook.getBookId());
            return DateHelper.format(latestDate);
        } catch (ReservationNotFoundException_Exception ex) {
            throw ex;
        }
    }

    private boolean isReserved(BookEntity currBook) {
        //If is reserved: get the list, then search for latest available due date

        List<ReservationEntity> reservationList = WebService.retrieveReservationsByBookId(currBook.getBookId());
        if (reservationList.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isLoaned(BookEntity currBook) {
        try {
            WebService.retrieveLoanByBookId(currBook.getBookId());
            return true;
        } catch (LoanNotFoundException_Exception ex) {
            return false;
        }
    }

    private void displayLoanTable(List<LoanEntity> loanList) {
        System.out.println("Currently Lent Books:\n");

        String header = String.format("%-5s| %-50s| %-11s", "Id", "Title", "Due Date");

        String table = "";
        for (LoanEntity loan : loanList) {
            table += "\n" + String.format("%-5s| %-50s| %-11s", loan.getBook().getBookId(), loan.getBook().getTitle(), DateHelper.format(loan.getEndDate().toGregorianCalendar().getTime()));
        }

        System.out.print(header);
        System.out.println(table);
    }

    private void displayFineTable(List<FineEntity> fineList) {
        System.out.println("Unpaid Fines for Member:\n");

        String header = String.format("%-5s| %-50s", "Id", "Amount");

        String table = "";
        for (FineEntity fine : fineList) {
            table += "\n" + String.format("%-5s| %-50s", fine.getFineId(), MoneyHelper.format(fine.getAmount()));
        }

        System.out.print(header);
        System.out.println(table);
    }

    private void displayMessage(String message) {
        System.out.println(message);
    }

    private boolean checkNum(String optionStr) {
        final String OPTION_PATTERN = "[0-9]+";
        Pattern optionPattern = Pattern.compile(OPTION_PATTERN);
        Matcher optionMatcher = optionPattern.matcher(optionStr);

        if (!optionMatcher.matches()) {
            return false;
        }

        return true;
    }

    private void printInvalidBookFormat() {
        System.out.println("Please Enter a valid book number");
        System.out.println();
    }

    private void printInvalidFineFormat() {
        System.out.println("Please enter a valid fine number");
    }

    private void printInvalidPaymentFormat() {
        System.out.println("Please enter a valid payment method");
    }
}
