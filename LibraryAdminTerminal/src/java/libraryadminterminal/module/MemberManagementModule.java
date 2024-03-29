package libraryadminterminal.module;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.exception.MemberEntityException;
import util.exception.MemberExistsException;
import util.exception.MemberNotFoundException;

public class MemberManagementModule {
    private static final int ADD_MEMBER = 1;
    private static final int VIEW_MEMBER_DETAILS = 2;
    private static final int UPDATE_MEMBER = 3;
    private static final int DELETE_MEMBER = 4;
    private static final int VIEW_ALL_MEMBERS = 5;
    private static final int BACK = 6;
    
    private MemberEntityControllerRemote memberEntityControllerRemote;
    
    public MemberManagementModule() {}

    public MemberManagementModule(MemberEntityControllerRemote memberEntityControllerRemote) {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
    }
    
    public void enterMemberManageMentMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=ADD_MEMBER && response!=VIEW_MEMBER_DETAILS && response!=UPDATE_MEMBER 
                    && response!=DELETE_MEMBER && response!=VIEW_ALL_MEMBERS && response!=BACK) {
                response = getUserResponse();
            }
            
            try {
                if (response == ADD_MEMBER) {
                    addMember();
                }
                else if (response == VIEW_MEMBER_DETAILS) {
                    viewMemberDetails();
                }
                else if (response == UPDATE_MEMBER) {
                    updateMember();
                }
                else if (response == DELETE_MEMBER) {
                    deleteMember();
                }
                else if (response == VIEW_ALL_MEMBERS) {
                    viewAllMembers();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (MemberNotFoundException | MemberExistsException | MemberEntityException ex) {
                displayMessage(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                displayMessage("You have not entered a valid numerical value.\n");
            }
            finally {
                System.out.println();
            }
            
        }
    }
    
    private void addMember() throws MemberExistsException, MemberEntityException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Member Management :: Add Member ***\n");
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
        newMember = memberEntityControllerRemote.createNewMember(newMember);
        displayMessage("Member has been added successfully!");
    }
    
    private void viewMemberDetails() throws MemberNotFoundException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Member Management :: View Member Details ***\n");
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        
        MemberEntity staff = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
        displayMessage(formatMemberDetail(staff));
    }
    
    private void updateMember() throws MemberNotFoundException, MemberExistsException, MemberEntityException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Member Management :: Update Member ***\n");
        System.out.print("Enter member id> ");
        long memberId = Long.valueOf(scanner.nextLine().trim());
        MemberEntity member = memberEntityControllerRemote.retrieveMemberById(memberId);
        
        System.out.print("Enter Identity Number (blank if no change)> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Security Code (blank if no change)> ");
        String securityCode = scanner.nextLine().trim();
        System.out.print("Enter First Name (blank if no change)> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name (blank if no change)> ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter Gender (blank if no change)> ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter Age (blank if no change)> ");
        String age = scanner.nextLine().trim();
        System.out.print("Enter Phone (blank if no change)> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address (blank if no change)> ");
        String address = scanner.nextLine().trim();
        
        member.updateMemberEntity(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
        memberEntityControllerRemote.updateMember(member);
        displayMessage("Member updated successfully!\n");
    }
    
    private void deleteMember() throws MemberNotFoundException, MemberEntityException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Member Management :: Delete Member ***\n");
        System.out.print("Enter member id> ");
        long memberId = Long.valueOf(scanner.nextLine().trim());
        
        MemberEntity memberToRemove = memberEntityControllerRemote.retrieveMemberById(memberId);
        
        System.out.printf("Confirm Delete Member %s (Member ID: %d) (Enter 'Y' to Delete)> ", memberToRemove.getFullName(), memberToRemove.getMemberId());
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            displayMessage("Deleting...");
            memberEntityControllerRemote.deleteMember(memberToRemove.getMemberId());
            displayMessage("Member deleted successfully!\n");
        }
        else {
            displayMessage("Member NOT deleted!\n");
        }
    }
    
    private void viewAllMembers() {
        List<MemberEntity> memberList = memberEntityControllerRemote.retrieveAllMembers();
        String table = formatTableRow("Id", "Identity Number", "Security Code", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
        for (MemberEntity member : memberList) {
            table += "\n" + formatTableRow(member.getMemberId().toString(), member.getIdentityNumber(), member.getSecurityCode(), 
                member.getFirstName(), member.getLastName(), member.getGender(), member.getAge().toString(), 
                member.getPhone(), member.getAddress());
        }
        displayMessage(table);
    }
    
    private String formatMemberDetail(MemberEntity member) {
        String header = formatTableRow("Id", "Identity Number", "Security Code", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
        String row = formatTableRow(member.getMemberId().toString(), member.getIdentityNumber(), member.getSecurityCode(), 
                member.getFirstName(), member.getLastName(), member.getGender(), member.getAge().toString(), 
                member.getPhone(), member.getAddress());
        return header + "\n" + row;
    }
    
    private String formatTableRow(String id, String identityNumber, String securityCode, String firstName, String lastName, String gender, String age, String phone, String address) {
        return String.format("%-5s| %-50s| %-50s| %-50s| %-50s| %-7s| %-4s| %-50s| %-255s", id, identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
    }
    
    private String getMainMenu() {
        return "*** ILS :: Administration Operation :: Member Management ***\n\n" +
               ADD_MEMBER + ": Add Member\n" +
               VIEW_MEMBER_DETAILS + ": View Member Details\n" +
               UPDATE_MEMBER + ": Update Member\n" +
               DELETE_MEMBER + ": Delete Member\n" +
               VIEW_ALL_MEMBERS + ": View All Members\n" + 
               BACK + ": Back\n";
    }
    
    private void displayMessage(String message) {
        System.out.println(message);
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
}
