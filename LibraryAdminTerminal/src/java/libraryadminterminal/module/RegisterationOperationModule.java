package libraryadminterminal.module;

import entity.MemberEntity;
import java.util.Scanner;

public class RegisterationOperationModule {
    private static final int REGISTER_NEW_MEMBER = 1;
    private static final int BACK = 2;

    public RegisterationOperationModule() {}
    
    public void enterRegisterationOperationMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=REGISTER_NEW_MEMBER && response!=BACK) {
                response = getUserResponse();
            }
            
            if (response == REGISTER_NEW_MEMBER) {
                registerNewMember();
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
    
    private void registerNewMember() {
        Scanner scanner = new Scanner(System.in);
        String identityNumber = "";
        String securityCode = "";
        String firstName = "";
        String lastName = "";
        String gender = "";
        Integer age = 0;
        String phone = "";
        String address = "";
        
        System.out.println();
        System.out.println("*** ILS :: Registration Operation :: Register New Member ***\n");

        
//        MemberEntity newMember = new MemberEntity(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
//        newMember = memberEntityControllerRemote.createNewMember();
        displayMessage("Member has been registered successfully!\n");
    }
    
    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
    
    private String getMainMenu() {
        return "*** ILS :: Registration Operation ***\n\n" +
               REGISTER_NEW_MEMBER + ": Register New Member\n" +
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
}
