package libraryadminterminal.module;

import java.util.Scanner;

public class AdministrationOperationModule {
    private static final int MEMBER_MANAGEMENT = 1;
    private static final int BOOK_MANAGEMENT = 2;
    private static final int STAFF_MANAGEMENT = 3;
    private static final int BACK = 4;
    
    private StaffManagementModule staffManagementModule;

    public AdministrationOperationModule() {
        this.staffManagementModule = new StaffManagementModule();
    }
    
    public void enterAdministrationOperationMode(){
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=MEMBER_MANAGEMENT && response!=BOOK_MANAGEMENT && response!=STAFF_MANAGEMENT && response!=BACK) {
                response = getUserResponse();
            }
            
            if (response == MEMBER_MANAGEMENT) {
                
            }
            else if (response == BOOK_MANAGEMENT) {
                
            }
            else if (response == STAFF_MANAGEMENT) {
                staffManagementModule.enterStaffManagementMode();
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
        return "*** ILS :: Administration Operation ***\n\n" +
               MEMBER_MANAGEMENT + ": Member Management\n" +
               BOOK_MANAGEMENT + ": Book Management\n" +
               STAFF_MANAGEMENT + ": Staff Management\n" +
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
}
