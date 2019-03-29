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
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.MemberExistsException;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.MemberNotFoundException;
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

    public void enterBorrowBook(MemberEntity member) {
        printBorrowBookMain();

        Scanner sc = new Scanner(System.in);
        Long currBookId = sc.nextLong();

        //Things to consider: 
        //1) Unpaid Fines  2)Member has already lent 3 books   
        //3)Book is reserved by another member with higher priority 
//        boolean hasUnpaidFine = checkUnpaidFine(member);
//        boolean hasLentMax = checkLentMax(member);
//        boolean hasBeenReserved = checkReserved(currBookId);
        boolean hasUnpaidFine = false;
        boolean hasLentMax = false;
        boolean hasBeenReserved = false;

        if (!hasUnpaidFine && !hasLentMax && !hasBeenReserved) {
            //Do lent book:
            //Create loanEntity

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 14);
            Date dueDate = c.getTime();

            try {
                BookEntity bookToLend = bookEntityControllerRemote.retrieveBookById(currBookId);

                LoanEntity newLoan = new LoanEntity(bookToLend, dueDate, member); //bookEntity, endDate, memberEntity

                //Adds a new loan entity to database
                loanEntityControllerRemote.createNewLoanEntity(newLoan);

                //Update this loan onto the member's list of loans
                List<LoanEntity> updatedLoanList = member.getLoans();
                updatedLoanList.add(newLoan);
                member.setLoans(updatedLoanList);
                memberEntityControllerRemote.updateMember(member);

                //Update the loan entity within book entity
                bookToLend.setLoan(newLoan);
                bookEntityControllerRemote.updateBook(bookToLend);

                //Print Success Message
                printLentSuccessMessage(dueDate);

            } catch (LoanException stlne) {
                System.out.println(stlne.getMessage());
            } catch (BookNotFoundException bnfe) {
                System.out.println(bnfe.getMessage());
            } catch (MemberExistsException me) {
                System.out.println(me.getMessage());
            }
        } else {
            printBorrowError(hasUnpaidFine, hasLentMax, hasBeenReserved);
        }
    }

    public void enterViewLentBook(MemberEntity member) {
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");
        System.out.println("Currently Lent Books:");

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
        Long bookId = Long.valueOf(scanner.nextLine().trim());

        loanEntityControllerRemote.removeLoan(bookId, member.getMemberId());
        displayMessage("Book successfully returned.");
    }

    public void enterExtendBook(MemberEntity member) throws MemberNotFoundException, LoanNotFoundException, LoanException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");

        List<LoanEntity> loanList = loanEntityControllerRemote.retrieveLoansByMemberId(member.getMemberId());

        displayLoanTable(loanList);
        System.out.println();

        System.out.print("Enter Book ID to Extend> ");
        Long bookId = Long.valueOf(scanner.nextLine().trim());

        LoanEntity loan = loanEntityControllerRemote.retrieveLoanByBookId(bookId);
        loan = loanEntityControllerRemote.extendLoan(loan);
        displayMessage("Book successfully extended. New due date: " + DateHelper.format(loan.getEndDate()));
    }

    public void enterPayFine(MemberEntity member) throws FineNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Pay Fines ***\n");

        System.out.println("Unpaid Fines for Member:");

        String identityNumber = member.getIdentityNumber();
        List<FineEntity> fineList = fineEntityControllerRemote.retrieveFinesByMemberIdentityNumber(identityNumber);

        displayFineTable(fineList);
        System.out.println();

        if (fineList.size() > 0) {
            System.out.print("Enter Fine to Settle> ");
            Long fineId = Long.valueOf(scanner.nextLine().trim());
            System.out.print("Select Payment Method (1: Cash, 2: Card)> ");
            int paymentMode = Integer.valueOf(scanner.nextLine().trim());
            fineEntityControllerRemote.deleteFine(fineId);
            displayMessage("Fine successfully paid.");
        } else {
            displayMessage("There are no outstanding fine.");
        }
    }

    public void enterSearchBook() throws BookNotFoundException {
        
        /**
         * All books with search string having partial or full match are listed.
         * Details of whether the book is currently available, is on hold with reservation or if applicable a due date is shown.
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n"
                + "Enter Title to Search> ");
        String title = scanner.nextLine().trim();

        List<BookEntity> bookEntities = bookEntityControllerRemote.searchBookByTitle(title);
//        BookEntity bookEntities = bookEntityControllerRemote.retrieveBookByTitle(title);

        System.out.println();
        System.out.println("Search Results:");

        System.out.println("Id |Title | Availability");
//        System.out.println(bookEntities.getBookId() + "  | " + bookEntities.getTitle() + "  | " + "Available Now ");
        for (BookEntity currBook : bookEntities) {
            if (isLoaned(currBook)) {
                System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + currBook.getLoan().getEndDate());
            } 
//            else if(isReserved(currBook)) {
//                System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Due on " + getAvailableReservationDate(currBook));
//            } 
            else {
                System.out.println(currBook.getBookId() + "  | " + currBook.getTitle() + "  | " + "Available Now ");
            }
        }

    }
    private String getAvailableReservationDate(BookEntity currBook) {
        Date latestDate = currBook.getReservations().get(0).getCreatedOn();
        List<ReservationEntity> reservedList = currBook.getReservations();
        
        for(int i = 1; i < reservedList.size() ; i ++) {
            ReservationEntity currReserved = reservedList.get(i);
            if(currReserved.getCreatedOn().compareTo(latestDate) > 0 ) {
                latestDate = currReserved.getCreatedOn();
            }
        }
        
        return DateHelper.format(latestDate); 
    }
    private boolean isReserved(BookEntity currBook) {
        //If is reserved: get the list, then search for latest available due date
        
        
        //To-Do
        return false;
    }
    
    private boolean isLoaned(BookEntity currBook) {
        try {
            loanEntityControllerRemote.retrieveLoanByBookId(currBook.getBookId());
            return true;
        } catch (LoanNotFoundException ex) {
            return false;
        }
    }
    public void enterReserveBook(MemberEntity member) {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** Self-Service Kiosk :: Reserve Book ***\n");

        System.out.println("Enter Title to Search> ");
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

    private void printReserveBookMain() {

    }

    private boolean checkLent(Long currBookId) {
        //To-Do
        return true;
    }

    private boolean checkReserved(Long currBookId) {
        //To-Do
        return true;
    }

    private boolean checkLentMax(MemberEntity member) {
        if (member.getLoans().size() == MAX_LOAN) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUnpaidFine(MemberEntity member) {
        if (member.getFines().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void printBorrowBookMain() {
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");
        System.out.print("Enter Book Id: ");
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

}
