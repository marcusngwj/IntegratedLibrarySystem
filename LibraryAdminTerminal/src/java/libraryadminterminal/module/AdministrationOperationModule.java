package libraryadminterminal.module;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdministrationOperationModule {
    private static final int MEMBER_MANAGEMENT = 1;
    private static final int BOOK_MANAGEMENT = 2;
    private static final int STAFF_MANAGEMENT = 3;
    private static final int BACK = 4;
    
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    
    private StaffManagementModule staffManagementModule;
    private MemberManagementModule memberManagementModule;
    private BookManagementModule bookManagementModule;

    public AdministrationOperationModule() {}
    
    public AdministrationOperationModule(StaffEntityControllerRemote staffEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        
        this.staffManagementModule = new StaffManagementModule(staffEntityControllerRemote);
        this.memberManagementModule = new MemberManagementModule(memberEntityControllerRemote);
        this.bookManagementModule = new BookManagementModule(bookEntityControllerRemote);
    }
    
    public void enterAdministrationOperationMode(){
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=MEMBER_MANAGEMENT && response!=BOOK_MANAGEMENT && response!=STAFF_MANAGEMENT && response!=BACK) {
                response = getUserResponse();
            }
            
            if (response == MEMBER_MANAGEMENT) {
                memberManagementModule.enterMemberManageMentMode();
            }
            else if (response == BOOK_MANAGEMENT) {
                bookManagementModule.enterBookManagementMode();
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
        int input = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        try {
            input = scanner.nextInt();
        }
        catch (InputMismatchException ex) {
            displayMessage("Please enter a valid numerical number.");
        }
        
        return input;
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
