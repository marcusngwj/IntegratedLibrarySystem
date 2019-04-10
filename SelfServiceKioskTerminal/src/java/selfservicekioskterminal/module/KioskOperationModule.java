/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import entity.BookEntity;
import entity.FineEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.MemberExistsException;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.ReservationException;
import util.exception.ReservationNotFoundException;
import util.helper.DateHelper;
import util.helper.MoneyHelper;

/**
 *
 * @author limwe
 */
public class KioskOperationModule {

    private MemberEntityControllerRemote memberEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private LoanEntityControllerRemote loanEntityControllerRemote;
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;

    final int MAX_LOAN = 3;
    private KioskReservationManagementModule reservationManagementModule;

    public KioskOperationModule() {
    }

    public KioskOperationModule(MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, LoanEntityControllerRemote loanEntityControllerRemote, FineEntityControllerRemote fineEntityControllerRemote, ReservationEntityControllerRemote reservationEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.loanEntityControllerRemote = loanEntityControllerRemote;
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;

        this.reservationManagementModule = new KioskReservationManagementModule(reservationEntityControllerRemote);
    }

    public void enterBorrowBook(MemberEntity member) throws BookNotFoundException, LoanException {

        //Things to consider: 
        //1) Unpaid Fines  2)Member has already lent 3 books   
        //3)Book is reserved by another member with higher priority 
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");
        System.out.print("Enter Book Id: ");
        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);
        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);

            BookEntity book = bookEntityControllerRemote.retrieveBookById(bookId);

            LoanEntity newLoan = new LoanEntity(book, member);
            newLoan = loanEntityControllerRemote.createNewLoanEntity(newLoan);
            displayMessage("Successfully lent book to member. Due Date: " + DateHelper.format(newLoan.getEndDate()));
        } else {
            printInvalidBookFormat();
        }
    }

    public void enterViewLentBook(MemberEntity member) {
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");

        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());
        displayLoanTable(loanList);
    }

    public void enterReturnBook(MemberEntity member) throws MemberNotFoundException, LoanNotFoundException, LoanException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Return Book ***\n");

        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());

        displayLoanTable(loanList);
        System.out.println();

        System.out.print("Enter Book ID to Return> ");

        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);
        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);
            loanEntityControllerRemote.removeLoan(bookId, member.getMemberId());
            displayMessage("Book successfully returned.");
        } else {
            printInvalidBookFormat();
        }
    }

    public void enterExtendBook(MemberEntity member) throws MemberNotFoundException, LoanNotFoundException, LoanException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");

        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());

        displayLoanTable(loanList);
        System.out.println();

        System.out.print("Enter Book ID to Extend> ");
        String bookIdStr = scanner.nextLine().trim();
        boolean isNum = checkNum(bookIdStr);
        if (isNum) {
            Long bookId = Long.valueOf(bookIdStr);
            LoanEntity loan = loanEntityControllerRemote.extendLoan(bookId, member.getMemberId());
            displayMessage("Book successfully extended. New due date: " + DateHelper.format(loan.getEndDate()));
        } else {
            printInvalidBookFormat();
        }
    }

    public void enterPayFine(MemberEntity member) throws FineNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Pay Fines ***\n");

        String identityNumber = member.getIdentityNumber();
        List<FineEntity> fineList = fineEntityControllerRemote.retrieveFinesByMemberIdentityNumber(identityNumber);

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
                fineEntityControllerRemote.removeFine(fineId, member.getMemberId());
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

    public void enterSearchBook() throws BookNotFoundException {

        /**
         * All books with search string having partial or full match are listed.
         * Details of whether the book is currently available, is on hold with
         * reservation or if applicable a due date is shown.
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Search Book ***");
        System.out.print("Enter Title to Search > ");
        String title = scanner.nextLine().trim();

        List<BookEntity> bookEntities = bookEntityControllerRemote.searchBookByTitle(title);

        System.out.println();
        System.out.println("Search Results:");

        String header = String.format("%-5s| %-50s| %-20s", "Id", "Title", "Availability");
        String table = "";
        System.out.print(header);
        //Details of whether the book is currently available, is on hold with reservation or if applicable a due date is shown.
        for (BookEntity currBook : bookEntities) {
            boolean onLoaned = isLoaned(currBook);
             boolean onReserved = isReserved(currBook);
            if(onLoaned) {
               try {
                    //System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getBookLoanedDate(currBook));
                     table += "\n" + String.format("%-5s| %-50s| Due on %-20s", currBook.getBookId(), currBook.getTitle(), getBookLoanedDate(currBook));
               } catch (LoanNotFoundException ex) {
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
//            boolean onReserved = isReserved(currBook);
//            if (onLoaned ) {
//                try {
//                    System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getBookLoanedDate(currBook));
//                } catch (LoanNotFoundException ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else if (onLoaned && onReserved || !onLoaned && onReserved) {
//                try {
//                    System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getAvailableReservationDate(currBook));
//                } catch (ReservationNotFoundException ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else {
//                System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Available Now ");
//            }
        }
        System.out.println(table);
    }

    public void enterReserveBook(MemberEntity member) throws BookNotFoundException, ReservationNotFoundException {
        final String SUCCESS_RESERVED = "Book successfully reserved.";
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Reserve Book ***\n");

        System.out.print("Enter Title to Search> ");
        String title = scanner.nextLine().trim();

        List<BookEntity> bookEntities = bookEntityControllerRemote.searchBookByTitle(title);
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
               } catch (LoanNotFoundException ex) {
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
//                } catch (LoanNotFoundException ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else if (onLoaned && onReserved || !onLoaned && onReserved) {
//                try {
//                    System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getAvailableReservationDate(currBook));
//                } catch (ReservationNotFoundException ex) {
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
            BookEntity currBook = bookEntityControllerRemote.retrieveBookById(bookId);
            ReservationEntity newReservation = new ReservationEntity(currBook, member);

            try {
                reservationEntityControllerRemote.createNewReservationEntity(newReservation);
                //Print Reservation Success msg
                displayMessage(SUCCESS_RESERVED);
            } catch (LoanException le) {
                System.out.println(le.getMessage());
            } catch (ReservationException mre) {
                System.out.println(mre.getMessage());
            } catch (LoanNotFoundException lnfe) {
                System.out.println(lnfe.getMessage());
            }
        } else {
            printInvalidBookFormat();
        }
    }

    private String getBookLoanedDate(BookEntity currBook) throws LoanNotFoundException {
        try {
            LoanEntity currLoan = loanEntityControllerRemote.retrieveLoanByBookId(currBook.getBookId());
            Date bookLoanedDate = currLoan.getEndDate();

            return DateHelper.format(bookLoanedDate);
        } catch (LoanNotFoundException lnfe) {
            throw new LoanNotFoundException(lnfe.getMessage());
        }
    }

    private String getAvailableReservationDate(BookEntity currBook) throws ReservationNotFoundException {

        try {
            Date latestDate = reservationEntityControllerRemote.retrieveLatestReservationDate(currBook.getBookId());

            return DateHelper.format(latestDate);
        } catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException(ex.getMessage());
        }
    }

    private boolean isReserved(BookEntity currBook) {
        //If is reserved: get the list, then search for latest available due date

        List<ReservationEntity> reservationList = reservationEntityControllerRemote.retrieveReservationsByBookId(currBook.getBookId());
        if (reservationList.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isLoaned(BookEntity currBook) {
        try {
            loanEntityControllerRemote.retrieveLoanByBookId(currBook.getBookId());
            return true;
        } catch (LoanNotFoundException ex) {
            return false;
        }
    }

    private void printLentSuccessMessage(Date dueDate) {
        System.out.println("Successfully lent book. Due Date: " + dueDate + ".\n");
    }

    private void printBorrowError(boolean hasUnpaidFine, boolean hasLentMax, boolean hasBeenReserved) {
        if (hasUnpaidFine) {
            System.out.println("You have existing Unpaid Fines. Please pay them before borrowing");
        }
        if (hasLentMax) {
            System.out.println("You have already borrowed 3 books. Please return some of them before attempting to borrow again.");
        }
        if (hasBeenReserved) {
            System.out.println("Book has already been reserved.");
        }

    }

    private void displayLoanTable(List<LoanEntity> loanList) {
        System.out.println("Currently Lent Books:\n");

        String header = String.format("%-5s| %-50s| %-11s", "Id", "Title", "Due Date");

        String table = "";
        for (LoanEntity loan : loanList) {
            table += "\n" + String.format("%-5s| %-50s| %-11s", loan.getBook().getBookId(), loan.getBook().getTitle(), DateHelper.format(loan.getEndDate()));
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
