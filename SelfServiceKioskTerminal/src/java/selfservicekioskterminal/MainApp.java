/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import selfservicekioskterminal.module.KioskOperationModule;
import selfservicekioskterminal.module.KioskRegistrationOperationModule;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.LoanException;
import util.exception.LoanNotFoundException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author limwe
 */
public class MainApp {

    private MemberEntityControllerRemote memberEntityControllerRemote;
    private LoanEntityControllerRemote loanEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;

    private KioskRegistrationOperationModule registerationKioskOperationModule;
    private KioskOperationModule kioskOperationModule;

    MemberEntity currentMember;

    MainApp(MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote,
            LoanEntityControllerRemote loanEntityControllerRemote, FineEntityControllerRemote fineEntityControllerRemote,
            ReservationEntityControllerRemote reservationEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.loanEntityControllerRemote = loanEntityControllerRemote;
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;

    }

    void runApp() {
        final int REGISTER_NUMBER = 1;
        final int LOGIN_NUMBER = 2;
        final int EXIT_NUMBER = 3;

        while (true) {
            int response = 0;
            printKioskWelcomeMessage();
            response = getUserResponse();

            if (response == REGISTER_NUMBER) {
                registerationKioskOperationModule = new KioskRegistrationOperationModule(memberEntityControllerRemote);
                registerationKioskOperationModule.doRegisterMember();
            } else if (response == LOGIN_NUMBER) {
                try {
                    executeLogin();
                    executeMainAction();
                } catch (InvalidLoginException ex) {
                    System.out.println(ex);
                }
            } else if (response == EXIT_NUMBER) {
                break;
            } else {
                printInvalidResponseMessage();
            }
        }
    }

    private void executeMainAction() {

        final int BORROW_BOOK = 1;
        final int VIEW_LENT_BOOK = 2;
        final int RETURN_BOOK = 3;
        final int EXTEND_BOOK = 4;
        final int PAY_FINE = 5;
        final int SEARCH_BOOK = 6;
        final int RESERVE_BOOK = 7;
        final int LOGOUT = 8;

        kioskOperationModule = new KioskOperationModule(memberEntityControllerRemote, bookEntityControllerRemote,
                loanEntityControllerRemote, fineEntityControllerRemote, reservationEntityControllerRemote);
        int option = 0;

        while (true) {
            printKioskMain();
            printMemberName();
            printKioskMainMenu();
            System.out.println();
            System.out.print(">");
            Scanner scanner = new Scanner(System.in);
            try {
                option = scanner.nextInt();
                boolean isLogout = false;
                switch (option) {
                    case BORROW_BOOK:
                        kioskOperationModule.enterBorrowBook(currentMember);
                        break;
                    case VIEW_LENT_BOOK:
                        kioskOperationModule.enterViewLentBook(currentMember);
                        break;
                    case RETURN_BOOK:
                        kioskOperationModule.enterReturnBook(currentMember);
                        break;
                    case EXTEND_BOOK:
                        kioskOperationModule.enterExtendBook(currentMember);
                        break;
                    case PAY_FINE:
                        kioskOperationModule.enterPayFine(currentMember);
                        break;
                    case SEARCH_BOOK:
                        kioskOperationModule.enterSearchBook();
                        break;
                    case RESERVE_BOOK:
                        kioskOperationModule.enterReserveBook(currentMember);
                        break;
                    case LOGOUT:
                        isLogout = true;
                        break;
                    default:
                        printInvalidOption();
                }
                if (isLogout) {
                    break;
                }
            } catch (MemberNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LoanNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LoanException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FineNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BookNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void printLentSuccessMsg() {

    }

    private void printInvalidOption() {
        System.out.println("Invalid Option. Please select 1-8");
    }

    private void printKioskMainMenu() {
        System.out.println("1: Borrow Book");
        System.out.println("2: View Lent Books");
        System.out.println("3: Return Book");
        System.out.println("4: Extend Book");
        System.out.println("5: Pay Fines");
        System.out.println("6: Search Book");
        System.out.println("7: Reserve Book");
        System.out.println("8: Logout");

    }

    private void printMemberName() {
        System.out.println("You are login as " + currentMember.getFirstName() + " " + currentMember.getLastName() + "\n");
    }

    private void printKioskMain() {
        System.out.println("*** Self-Service Kiosk :: Main ***\n");
    }

    private void executeLogin() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String securityCode = "";

        System.out.println();
        System.out.println("*** Self-Service :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        securityCode = scanner.nextLine().trim();

        currentMember = memberEntityControllerRemote.memberLogin(username, securityCode);
        printLoginSuccess();
    }

    private void printLoginSuccess() {
        System.out.println("Login successful!");
    }

    private void printInvalidResponseMessage() {
        System.out.println("Invalid Option Chosen.");
    }

    private void printKioskWelcomeMessage() {
        System.out.println("*** Welcome to Self-Service Kiosk ***\n"
                + "1: Register\n"
                + "2: Login\n"
                + "3: Exit\n");
    }

    private void printKioskRegisterMessage() {
        System.out.println("*** Self-Service Kiosk :: Register ***\n");
    }

    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
}
