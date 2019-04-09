package libraryadminterminal.module;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.MemberEntityException;
import util.exception.MemberExistsException;

public class RegisterationOperationModule {
    private static final int REGISTER_NEW_MEMBER = 1;
    private static final int BACK = 2;
    
    private MemberEntityControllerRemote memberEntityControllerRemote;

    public RegisterationOperationModule() {}

    public RegisterationOperationModule(MemberEntityControllerRemote memberEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
    }
    
    public void enterRegisterationOperationMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=REGISTER_NEW_MEMBER && response!=BACK) {
                response = getUserResponse();
            }
            
            try {
                if (response == REGISTER_NEW_MEMBER) {
                    registerNewMember();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (MemberExistsException | MemberEntityException ex) {
                displayMessage(ex.getMessage());
            }
            finally {
                System.out.println();
            }
        }
    }
    
    private void registerNewMember() throws MemberExistsException, MemberEntityException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Registration Operation :: Register New Member ***\n");
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        String securityCode = scanner.nextLine().trim();
        System.out.print("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter Gender> ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter Age> ");
        String age = scanner.nextLine().trim();
        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        String address = scanner.nextLine().trim();

        MemberEntity newMember = new MemberEntity(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
        newMember = memberEntityControllerRemote.persistNewMemberEntity(newMember);
        displayMessage("Member has been registered successfully!\n");
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
        return "*** ILS :: Registration Operation ***\n\n" +
               REGISTER_NEW_MEMBER + ": Register New Member\n" +
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
}
