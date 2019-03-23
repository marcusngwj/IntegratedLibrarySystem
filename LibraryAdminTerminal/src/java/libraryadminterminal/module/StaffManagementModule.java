package libraryadminterminal.module;

import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.StaffExistsException;
import util.exception.StaffNotFoundException;

public class StaffManagementModule {
    private static final int ADD_STAFF = 1;
    private static final int VIEW_STAFF_DETAILS = 2;
    private static final int UPDATE_STAFF = 3;
    private static final int DELETE_STAFF = 4;
    private static final int VIEW_ALL_STAFF = 5;
    private static final int BACK = 6;
    
    private StaffEntityControllerRemote staffEntityControllerRemote;

    public StaffManagementModule() {}
    
    public StaffManagementModule(StaffEntityControllerRemote staffEntityControllerRemote) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
    }
    
    public void enterStaffManagementMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=ADD_STAFF && response!=VIEW_STAFF_DETAILS && response!=UPDATE_STAFF 
                    && response!=DELETE_STAFF && response!=VIEW_ALL_STAFF && response!=BACK) {
                response = getUserResponse();
            }
            
            try {
                if (response == ADD_STAFF) {
                    addStaff();
                }
                else if (response == VIEW_STAFF_DETAILS) {
                    viewStaffDetail();
                }
                else if (response == UPDATE_STAFF) {
                    updateStaff();
                }
                else if (response == DELETE_STAFF) {
                    deleteStaff();
                }
                else if (response == VIEW_ALL_STAFF) {
                    viewAllStaff();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (StaffNotFoundException | StaffExistsException ex) {
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
    
    private void addStaff() throws StaffExistsException {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        String username = "";
        String password = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Add Staff ***\n");
        System.out.print("Enter first name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter last name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if (firstName.length()>0 && lastName.length()>0 && username.length()>0 && password.length()>0) {
            displayMessage("\nProcessing...");
            StaffEntity newStaff = new StaffEntity(firstName, lastName, username, password);
            newStaff = staffEntityControllerRemote.persistNewStaffEntity(newStaff);
            displayMessage("Staff has been added successfully!");
        }
        else {
            displayMessage("There were empty fields in your form. Please try again.");
        }
    }
    
    private void viewStaffDetail() throws StaffNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Staff Management :: View Staff Details ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        
        StaffEntity staff = staffEntityControllerRemote.retrieveStaffByUsername(username);
        displayMessage(formatStaffDetail(staff));
    }
    
    private void updateStaff() throws StaffNotFoundException, StaffExistsException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Update Staff ***\n");
        System.out.print("Enter staff id> ");
        long staffId = Long.valueOf(scanner.nextLine().trim());
        StaffEntity staff = staffEntityControllerRemote.retrieveStaffById(staffId);
        
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            staff.setFirstName(input);
        }
        
        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            staff.setLastName(input);
        }
        
        System.out.print("Enter Username (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            staff.setUsername(input);
        }
        
        System.out.print("Enter Password (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            staff.setPassword(input);
        }
        
        displayMessage("Updating...");
        staffEntityControllerRemote.updateStaff(staff);
        displayMessage("Staff updated successfully!\n");
    }
    
    
    private void deleteStaff() throws StaffNotFoundException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println();
        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Delete Staff ***\n");
        System.out.print("Enter staff id> ");
        long staffId = Long.valueOf(scanner.nextLine().trim());
        
        StaffEntity staffToRemove = staffEntityControllerRemote.retrieveStaffById(staffId);
        
        System.out.printf("Confirm Delete Staff %s (Staff ID: %d) (Enter 'Y' to Delete)> ", staffToRemove.getFullName(), staffToRemove.getStaffId());
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            displayMessage("Deleting...");
            staffEntityControllerRemote.deleteStaff(staffToRemove.getStaffId());
            displayMessage("Staff deleted successfully!\n");
        }
        else {
            displayMessage("Staff NOT deleted!\n");
        }
    }
    
    private void viewAllStaff() {
        List<StaffEntity> staffList = staffEntityControllerRemote.retrieveAllStaffs();
        String table = formatTableRow("Id", "First Name", "Last Name", "Username", "Password");
        for (StaffEntity staff : staffList) {
            table += "\n" + formatTableRow(staff.getStaffId().toString(), staff.getFirstName(), staff.getLastName(), staff.getUsername(), staff.getPassword());
        }
        displayMessage(table);
    }
    
    private String formatStaffDetail(StaffEntity staff) {
        String header = formatTableRow("Id", "First Name", "Last Name", "Username", "Password");
        String row = formatTableRow(staff.getStaffId().toString(), staff.getFirstName(), staff.getLastName(), staff.getUsername(), staff.getPassword());
        return header + "\n" + row;
    }
    
    private String formatTableRow(String id, String firstName, String lastName, String username, String password) {
        return String.format("%-5s| %-50s| %-50s| %-50s| %-50s", id, firstName, lastName, username, password);
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
    
    private int getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.nextInt();
    }
}
