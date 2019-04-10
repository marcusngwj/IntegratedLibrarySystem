package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LoanEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import libraryadminterminal.module.AdministrationOperationModule;
import libraryadminterminal.module.LibraryOperationModule;
import libraryadminterminal.module.RegisterationOperationModule;
import util.exception.InvalidLoginException;
import util.helper.DateHelper;

public class MainApp {
    private StaffEntity currentStaff;
    
    private RegisterationOperationModule registerationOperationModule;
    private LibraryOperationModule libraryOperationModule;
    private AdministrationOperationModule administrationOperationModule;
    
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    
    private LoanEntityControllerRemote loanEntityControllerRemote;
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;

    public MainApp() {}
    
    public MainApp(StaffEntityControllerRemote staffEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, LoanEntityControllerRemote loanEntityControllerRemote, FineEntityControllerRemote fineEntityControllerRemote, ReservationEntityControllerRemote reservationEntityControllerRemote) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        
        this.loanEntityControllerRemote = loanEntityControllerRemote;
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
    }
    
    public void runApp() {
        final int LOGIN_OPERATION = 1;
        final int EXIT_OPERATION = 2;
        
        while(true) {
            int response = 0;
            
            if (isLibraryOpen()) {
                displayMessage(getWelcomeMessage());
            }
            else {
                displayMessage(getLibraryIsClosedMessage());
                break;
            }
            
            while(response != LOGIN_OPERATION && response != EXIT_OPERATION) {
                response = getUserResponse();
            }
            
            if (response == LOGIN_OPERATION) {
                try {
                    executeLogin();
                    executeMainAction();
                }
                catch (InvalidLoginException ex) {
                    displayMessage(ex.getMessage());
                }
                
            }
            else if (response == EXIT_OPERATION) {
                System.out.println();
                displayMessage("Goodbye!");
                break;
            }
            
            System.out.println();
        }
    }
    
    private void executeLogin() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println();
        System.out.println("*** ILS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        currentStaff = staffEntityControllerRemote.staffLogin(username, password);
        displayMessage("Login successful!\n");
    }
    
    private void executeMainAction() {
        final int REGISTRATION_OPERATION = 1;
        final int LIBRARY_OPERATION = 2;
        final int ADMINISTRATION_OPERATION = 3;
        final int LOGOUT = 4;
        
        registerationOperationModule = new RegisterationOperationModule(memberEntityControllerRemote);
        libraryOperationModule = new LibraryOperationModule(memberEntityControllerRemote, bookEntityControllerRemote, loanEntityControllerRemote, fineEntityControllerRemote, reservationEntityControllerRemote);
        administrationOperationModule = new AdministrationOperationModule(staffEntityControllerRemote, memberEntityControllerRemote, bookEntityControllerRemote);
                
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            try {
                while (response!=REGISTRATION_OPERATION && response!=LIBRARY_OPERATION && response!=ADMINISTRATION_OPERATION && response!= LOGOUT) {
                    response = getUserResponse();
                }

                if (response == REGISTRATION_OPERATION) {
                    System.out.println();
                    registerationOperationModule.enterRegisterationOperationMode();
                }
                else if (response == LIBRARY_OPERATION) {
                    System.out.println();
                    libraryOperationModule.enterLibraryOperationMode();
                }
                else if (response == ADMINISTRATION_OPERATION) {
                    administrationOperationModule.enterAdministrationOperationMode();
                }
                else if (response == LOGOUT) {
                    System.out.println();
                    displayMessage("You have successfully logout!");
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (Exception ex) {
                displayMessage("Unknown error occured");
            }
            
            System.out.println();
        }
    }
    
    private boolean isLibraryOpen() {
        Date now = DateHelper.getCurrentDate();      
        boolean isSaturday = DateHelper.isGivenDay(now, DateHelper.SATURDAY);
        boolean isSunday = DateHelper.isGivenDay(now, DateHelper.SUNDAY);
        boolean isBefore9am = DateHelper.isBeforeGivenHour(now, 9);
        boolean isAfter5pm = DateHelper.isAfterGivenHour(now, 17);
        
        return !(isSaturday || isSunday || isBefore9am || isAfter5pm);
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
    
    private void displayMessage(String message) {
        System.out.println(message);
    }
    
    private String getMainMenu() {
        return "*** ILS :: Main ***\n\n" +
               "You are login as " + currentStaff.getFullName() + "\n\n" +
               "1: Registration Operation\n" +
               "2: Library Operation\n" +
               "3: Administration Operation\n" +
               "4: Logout\n";
    }
    
    private String getLibraryIsClosedMessage() {
        return "*** Welcome to Library Admin Terminal ***\n\n" +
               "Sorry, the library loan desk is currently closed.\n" +
               "Opening Hours: Monday to Friday, from 09:00 to 17:00.\n";
    }
    
    private String getWelcomeMessage() {
        return "*** Welcome to Library Admin Terminal ***\n\n" +
               "1: Login\n" +
               "2: Exit\n";
    }
}
