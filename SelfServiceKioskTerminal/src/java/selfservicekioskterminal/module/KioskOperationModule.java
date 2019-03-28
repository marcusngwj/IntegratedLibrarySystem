/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.BookEntity;
import entity.LoanEntity;
import entity.MemberEntity;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import util.exception.BookNotFoundException;
import util.exception.MemberExistsException;
import util.exception.LoanException;

/**
 *
 * @author limwe
 */
public class KioskOperationModule {

    LoanEntityControllerRemote loanEntityControllerRemote;
    BookEntityControllerRemote bookEntityControllerRemote;
    MemberEntityControllerRemote memberEntityControllerRemote;
    
    final int MAX_LOAN = 3;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public KioskOperationModule() {
        
    }
    public void enterBorrowBook(MemberEntity member) {
        printBorrowBookMain();

        Scanner sc = new Scanner(System.in);
        Long currBookId = sc.nextLong();

        //Things to consider: 
        //1) Unpaid Fines  2)Member has already lent 3 books   
        //3)Book is reserved by another member with higher priority 
        boolean hasUnpaidFine = checkUnpaidFine(member);
        boolean hasLentMax = checkLentMax(member);
        boolean hasBeenReserved = checkReserved(currBookId);

        if (!hasUnpaidFine && !hasLentMax && !hasBeenReserved) {
            //Do lent book:
            //Create loanEntity
          
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 14);
            Date dueDate = c.getTime();
            
            try {
                BookEntity bookToLend = bookEntityControllerRemote.retrieveBookById(currBookId);
            
                LoanEntity newLoan = new LoanEntity(bookToLend,dueDate,member); //bookEntity, endDate, memberEntity
                
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
        printViewLentBookMain();
    }

    public void enterReturnBook(MemberEntity member) {
        printReturnBookMain();
    }

    public void enterExtendBook(MemberEntity member) {
        printExtendBookMain();
    }

    public void enterPayFine(MemberEntity member) {
        printPayFineMain();
    }

    public void enterReserveBook(MemberEntity member) {
        printReserveBookMain();
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

    private void printReserveBookMain() {
        System.out.println("*** Self-Service Kiosk :: Reserve Book ***\n");

        System.out.println("Enter Title to Search> ");
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

    private void printPayFineMain() {
        System.out.println("*** Self-Service Kiosk :: Pay Fines ***\n");

        System.out.println("Unpaid Fines for Member:");
        System.out.println("Id |Amount");
    }

    private void printExtendBookMain() {
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");

        // System.out.println("\nCurrently Lent Books:");
        // System.out.println("Id |Title | Due Date");
    }

    private void printReturnBookMain() {
        System.out.println("*** Self-Service Kiosk :: Return Book ***\n");

        // System.out.println("\nCurrently Lent Books:");
        // System.out.println("Id |Title | Due Date");
    }

    private void printViewLentBookMain() {
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");
        System.out.println("Currently Lent Books:");
//        System.out.println("Id |Title | Due Date");

        //To-Dos
    }

    private void printBorrowBookMain() {
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");
        System.out.println("Enter Book Id: ");
    }
}
