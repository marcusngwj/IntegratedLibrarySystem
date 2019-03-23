package libraryadminterminal.module;

import java.util.Scanner;

public class StaffManagementModule {
    private static final int ADD_STAFF = 1;
    private static final int VIEW_STAFF_DETAILS = 2;
    private static final int UPDATE_STAFF = 3;
    private static final int DELETE_STAFF = 4;
    private static final int VIEW_ALL_STAFF = 5;
    private static final int BACK = 6;

    public StaffManagementModule() {}
    
    public void enterStaffManagementMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=ADD_STAFF && response!=VIEW_STAFF_DETAILS && response!=UPDATE_STAFF 
                    && response!=DELETE_STAFF && response!=VIEW_ALL_STAFF && response!=BACK) {
                response = getUserResponse();
            }
            
            if (response == ADD_STAFF) {
                
            }
            else if (response == VIEW_STAFF_DETAILS) {
                
            }
            else if (response == UPDATE_STAFF) {
                
            }
            else if (response == DELETE_STAFF) {
                
            }
            else if (response == VIEW_ALL_STAFF) {
                
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
        return "*** ILS :: Administration Operation :: Staff Management ***\n\n" +
               ADD_STAFF + ": Add Staff\n" +
               VIEW_STAFF_DETAILS + ": View Staff Details\n" +
               UPDATE_STAFF + ": Update Staff\n" +
               DELETE_STAFF + ": Delete Staff\n" +
               VIEW_ALL_STAFF + ": View All Staff\n" + 
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
}
