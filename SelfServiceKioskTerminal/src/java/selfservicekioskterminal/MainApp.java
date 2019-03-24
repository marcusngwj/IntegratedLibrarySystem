/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import selfservicekioskterminal.module.KioskBookOperationModule;
import selfservicekioskterminal.module.KioskOperationModule;
import selfservicekioskterminal.module.RegisterationKioskOperationModule;
import util.exception.InvalidLoginException;

/**
 *
 * @author limwe
 */
public class MainApp {

    private MemberEntityControllerRemote memberEntityControllerRemote;
    private RegisterationKioskOperationModule registerationKioskOperationModule;
    private KioskOperationModule kioskOperationModule;
    private KioskBookOperationModule kioskBookOperationModule;
    MemberEntity currentMember;

    MainApp(MemberEntityControllerRemote memberEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
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
                registerationKioskOperationModule.doRegisterMember();
            } else if (response == LOGIN_NUMBER) {
                try {
                    executeLogin();
                    executeMainAction();
                } catch (InvalidLoginException ex) {
                    System.out.println(ex);
                }
            } else if (response == EXIT_NUMBER) {

            } else {
                printInvalidResponseMessage();
            }
        }
    }

    private void executeMainAction() {
        printKioskMain();
        printMemberName();
        printKioskMainMenu();
        final int BORROW_BOOK = 1;
        final int VIEW_LENT_BOOK = 2;
        final int RETURN_BOOK = 3;
        final int EXTEND_BOOK = 4;
        final int PAY_FINE = 5;
        final int SEARCH_BOOK = 6;
        final int RESERVE_BOOK = 7;
        final int LOGOUT = 8;

        int option = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
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
                    kioskBookOperationModule.enterSearchBook();
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
