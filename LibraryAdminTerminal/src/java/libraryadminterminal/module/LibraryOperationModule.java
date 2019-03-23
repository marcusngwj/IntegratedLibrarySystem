package libraryadminterminal.module;

import java.util.Scanner;

public class LibraryOperationModule {
    private static final int LEND_BOOK = 1;
    private static final int VIEW_LENT_BOOKS = 2;
    private static final int RETURN_BOOK = 3;
    private static final int EXTEND_BOOK = 4;
    private static final int PAY_FINES = 5;
    private static final int MANAGE_RESERVATION = 6;
    private static final int BACK = 7;

    public LibraryOperationModule() {}
    
    public void enterLibraryOperationMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=LEND_BOOK && response!=VIEW_LENT_BOOKS && response!=RETURN_BOOK
                    && response!=EXTEND_BOOK && response!=PAY_FINES && response!=MANAGE_RESERVATION && response!=BACK) {
                response = getUserResponse();
            }
            
            if (response == LEND_BOOK) {
                
            }
            else if (response == VIEW_LENT_BOOKS) {
                
            }
            else if (response == RETURN_BOOK) {
                
            }
            else if (response == EXTEND_BOOK) {
                
            }
            else if (response == PAY_FINES) {
                
            }
            else if (response == MANAGE_RESERVATION) {
                
            }
            else if (response == BACK) {
                break;
            }
            else {
                displayMessage("Invalid option, please try again!\n");
            }
            
            System.out.println();
        }
    }
    
    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
    
    private String getMainMenu() {
        return "*** ILS :: Library Operation ***\n\n" +
               LEND_BOOK + ": Lend Book\n" +
               VIEW_LENT_BOOKS + ": View Lent Books\n" +
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
