package libraryadminterminal;

import java.util.Scanner;
import util.exception.InvalidLoginException;

public class MainApp {

    public MainApp() {}
    
    public void runApp() {
        final int LOGIN_OPERATION = 1;
        final int EXIT_OPERATION = 2;
        
        while(true) {
            int response = 0;
            displayMessage(getWelcomeMessage());
            
            while(response != LOGIN_OPERATION && response != EXIT_OPERATION) {
                response = getUserResponse();
            }
            
            if (response == LOGIN_OPERATION) {
                try {
                    executeLogin();
//                    libraryOperationModule = new LibraryOperationModule(libraryOperationRemote);
                    executeMainAction();
                }
                catch (InvalidLoginException e) {
                    // Log: Invalid Login Exception
                }
                
            }
            else if (response == EXIT_OPERATION) {
                System.out.println();
                System.out.println("Goodbye!");
                break;
            }
            
            System.out.println();
        }
    }
    
    private void executeLogin() throws InvalidLoginException {
        
    }
    
    private void executeMainAction() {
        final int REGISTRATION_OPERATION = 1;
        final int LIBRARY_OPERATION = 2;
        final int ADMINISTRATION_OPERATION = 3;
        final int LOGOUT = 4;
                
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=REGISTRATION_OPERATION && response!=LIBRARY_OPERATION && response!=ADMINISTRATION_OPERATION && response!= LOGOUT) {
                response = getUserResponse();
            }
            
            if (response == REGISTRATION_OPERATION) {
                
            }
            else if (response == LIBRARY_OPERATION) {
                System.out.println();
//                libraryOperationModule.bootUpLibraryProgram();
            }
            else if (response == ADMINISTRATION_OPERATION) {
                
            }
            else if (response == LOGOUT) {
                System.out.println();
                displayMessage("You have successfully logout!");
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
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
    
    private String getMainMenu() {
        return "*** ILS :: Main ***\n\n" +
//               "You are login as " + currentStaffEntity.getFullName + "\n\n" +
               "You are login as XXXXXX" + "\n\n" + 
               "1: Registration Operation\n" +
               "2: Library Operation\n" +
               "3: Administration Operation\n" +
               "4: Logout\n";
    }
    
    private String getWelcomeMessage() {
        return "*** Welcome to Library Admin Terminal ***\n\n" +
               "1: Login\n" +
               "2: Exit\n";
    }
}
