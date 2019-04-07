package bookdropmachineclient;

import bookdropmachineclient.module.LibraryOperationModule;
import java.util.InputMismatchException;
import java.util.Scanner;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.LoanException_Exception;
import ws.client.LoanNotFoundException_Exception;
import ws.client.MemberEntity;
import ws.client.ReservationNotFoundException_Exception;

/**
 *
 * @author ang
 */
public class MainApp {

    private MemberEntity currentMemberEntity;
    private LibraryOperationModule libraryOperationModule;

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("*** Welcome to BDM Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                try {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        try {
                            doLogin();
                            libraryOperationModule = new LibraryOperationModule(currentMemberEntity);
                            menuMain();
                        } catch (InvalidLoginException_Exception ex) {
                            System.err.println(ex.getMessage());
                        }
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid option, please try again!\n");
                    scanner.nextLine();
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginException_Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** BDM Client :: Login ***\n");

        System.out.print("Enter Identity Number> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        String password = scanner.nextLine().trim();

        currentMemberEntity = WebService.memberLogin(username, password);
        System.out.println("Login successful!");
    }

    private void menuMain() {
        final int VIEW_LENT_BOOK = 1;
        final int RETURN_BOOK = 2;
        final int EXTEND_BOOK = 3;
        final int PAY_FINE = 4;
        final int RESERVE_BOOK = 5;
        final int LOGOUT = 6;

        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("*** BDM Client :: Main ***\n");
            System.out.println("You are login as " + currentMemberEntity.getFirstName() + " " + currentMemberEntity.getLastName() + "\n");
            System.out.println("1: View Lent Books");
            System.out.println("2: Return Book");
            System.out.println("3: Extend Book");
            System.out.println("4: Pay Fines");
            System.out.println("5: Reserve Book");
            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                try {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    switch (response) {
                        case VIEW_LENT_BOOK:
                            libraryOperationModule.enterViewLentBook();
                            break;
                        case RETURN_BOOK:
                            libraryOperationModule.enterReturnBook();
                            break;
                        case EXTEND_BOOK:
                            libraryOperationModule.enterExtendBook();
                            break;
                        case PAY_FINE:
                            libraryOperationModule.enterPayFine();
                            break;
                        case RESERVE_BOOK:
                            libraryOperationModule.enterReserveBook();
                            break;
                        case LOGOUT:
                            break;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid option, please try again!\n");
                    scanner.nextLine();
                } catch (LoanNotFoundException_Exception | LoanException_Exception | NumberFormatException | FineNotFoundException_Exception | BookNotFoundException_Exception | ReservationNotFoundException_Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }

            if (response == LOGOUT) {
                break;
            }
        }
    }
}
