package libraryadminterminal.module;

import ejb.session.stateless.ReservationEntityControllerRemote;
import entity.ReservationEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.ReservationNotFoundException;
import util.helper.DateHelper;

public class ReservationManagementModule {
    private static int VIEW_RESERVATION = 1;
    private static int DELETE_RESERVATION = 2;
    private static int BACK = 3;
    
    private ReservationEntityControllerRemote reservationEntityControllerRemote;

    public ReservationManagementModule() {}

    public ReservationManagementModule(ReservationEntityControllerRemote reservationEntityControllerRemote) {
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
    }
    
    public void enterReservationMode() {
        while (true) {
            int response = 0;
            
            displayMessage(getMainMenu());
            
            while (response!=VIEW_RESERVATION && response!=DELETE_RESERVATION && response!=BACK) {
                response = getUserResponse();
            }
            
            try {
                if (response == VIEW_RESERVATION) {
                    viewReservations();
                }
                else if (response == DELETE_RESERVATION) {
                    deleteReservation();
                }
                else if (response == BACK) {
                    break;
                }
                else {
                    displayMessage("Invalid option, please try again!\n");
                }
            }
            catch (ReservationNotFoundException ex) {
                displayMessage(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                displayMessage("Please enter a valid numeric input.");
            }
            finally {
                System.out.println();
            }
        }
    }
    
    private void viewReservations() throws NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Manage Reservations :: View Reservations for Book ***\n");
        System.out.print("Enter Book ID> ");
        Long bookId = Long.valueOf(scanner.nextLine().trim());
        
        List<ReservationEntity> reservationList = reservationEntityControllerRemote.retrieveReservationsByBookId(bookId);
        displayReservationTable(bookId, reservationList);
    }
    
    private void deleteReservation() throws ReservationNotFoundException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("*** ILS :: Library Operation :: Manage Reservations :: Delete Reservation ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = Long.valueOf(scanner.nextLine().trim());
        reservationEntityControllerRemote.deleteReservation(reservationId);
        displayMessage("Reservation deleted successfully!");
    }
    
    private void displayReservationTable(Long bookId, List<ReservationEntity> reservationList) {
        System.out.println("Current Reservations for Book ID " + bookId + " :\n");
        
        String header = String.format("%-5s| %-12s| %-9s| %-100s", "Id", "Reserved On", "MemberId", "Member Name");
        
        String table = "";
        for (ReservationEntity reservation : reservationList) {
            table += "\n" + String.format("%-5s| %-12s| %-9s| %-100s", reservation.getReservationId(), DateHelper.format(reservation.getCreatedOn()), reservation.getMember().getMemberId(), reservation.getMember().getFullName());
        }
        
        System.out.print(header);
        System.out.println(table);
    }
    
    private String getMainMenu() {
        return "*** ILS :: Library Operation :: Manage Reservations ***\n\n" +
               VIEW_RESERVATION + ": View Reservations for Book\n" +
               DELETE_RESERVATION + ": Delete Reservations\n" +
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
